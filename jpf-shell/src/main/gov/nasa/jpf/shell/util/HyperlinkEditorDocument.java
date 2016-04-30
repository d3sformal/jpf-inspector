/* Copyright (C) 2007 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration
 * (NASA).  All Rights Reserved.
 *
 * This software is distributed under the NASA Open Source Agreement
 * (NOSA), version 1.3.  The NOSA has been approved by the Open Source
 * Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
 * directory tree for the complete NOSA document.
 *
 * THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
 * KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
 * LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
 * SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
 * THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
 * DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
 */
package gov.nasa.jpf.shell.util;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.util.hyperlinks.BasicHyperLinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkDecorator;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import javax.swing.text.*;

/**
 * The document used by HyperlinkEditorPane.
 *
 * All HyperlinkEditorDocument's use the same cache for all findings.
 */
public class HyperlinkEditorDocument extends DefaultStyledDocument{

  public static final String ACTIVE_LINK_STYLE = "ACTIVE_LINK_STYLE ";
  public static final String INACTIVE_LINK_STYLE = "INACTIVE_LINK_STYLE";

  private Style defStyle;

  TreeMap<Range, Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator>> links =
    new TreeMap<Range, Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator>>();

  public HyperlinkEditorDocument(){
    super();
    
    Config conf = ShellManager.getManager().getConfig();
    
    defStyle = addStyle("defaultStyle", null);
    addStyle(ACTIVE_LINK_STYLE, defStyle);
    addStyle(INACTIVE_LINK_STYLE, defStyle);

    int fs = conf.getInt("shell.textfont.size", 13);
    if (fs > 0){
      StyleConstants.setFontSize(defStyle, fs);
    }

    String fn = conf.getString("shell.textfont.name", Font.MONOSPACED);
    if ( fn !=  null){
      StyleConstants.setFontFamily(defStyle, fn);
    }

		String lineheight = conf.getProperty("shell.textfont.lineheight", "0.2");
		try{
			StyleConstants.setLineSpacing(defStyle, new Float(lineheight));
		} catch(NumberFormatException nfe){
			ShellManager.getManager().getLogger().log(Level.SEVERE, "shell.textfont.lineheight="+lineheight+" must be a float.", nfe);
		}

    setParagraphAttributes(0, getLength(), defStyle, true);
  }

  public Style getDefaultStyle(){
    return defStyle;
  }

  public Range getRange(int position){
    for (Range range : links.keySet()) {
      if (range.containsInt(position)) {
        return range;
      }
    }
    return null;
  }

  public Tuple<HyperlinkPattern, Object> getHyperLink(int position){
    Range range = getRange(position);
    if (range == null) return null;
    Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator> t = links.get(range);
    if (t == null) {
      return null;
    }
    return new Tuple<HyperlinkPattern, Object>(t.a, t.b);
  }

  public Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator> getHyperLink(Range r){
    if (r == null) return null;
    return links.get(r);
  }

  @Override
  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
    insertString(offs, str, a, new HashMap<HyperlinkPattern, HyperlinkDecorator>());
  }

  /**
   * Inserts text with the given style and a collection of hyperlinks to detect
   * the {@link BasicHyperLinkDecorator} will be used to decorate the links
   * found.
   * @param offs
   * @param str
   * @param a
   * @param patterns
   * @throws BadLocationException
   */
  public void insertString(int offs,
                          String str,
                          AttributeSet a,
                          Collection<HyperlinkPattern> patterns)
                          throws BadLocationException{
    HashMap<HyperlinkPattern, HyperlinkDecorator> patternmap
            = new HashMap<HyperlinkPattern, HyperlinkDecorator>();
    for (HyperlinkPattern hyperlinkPattern : patterns) {
      patternmap.put(hyperlinkPattern, new BasicHyperLinkDecorator());
    }
    insertString(offs, str, a, patternmap);
  }

  /**
   * Inserts a string of content.
   * @param offs the offset into the document to insert the content >= 0. All
   *             positions that track change at or after the given location will
   *             move.
   * @param str the string to insert
   * @param a   the attributes to associate with the inserted content. This may
   *            be null if there are no attributes.
   * @param map the hyperlink patters and decorators to apply during this
   *            insertion.
   * @throws BadLocationException
   */
  public void insertString(int offs,
                           String str,
                           AttributeSet a,
                           Map<HyperlinkPattern, HyperlinkDecorator> map)
                           throws BadLocationException{
    if (a == null) { a = new SimpleAttributeSet(); }
    super.insertString(offs, str, a);
    styleLines(offs, str == null ? 0 : str.length(), a, map);
  }

  @Override
  public void remove(int offset, int length) throws BadLocationException{
    super.remove(offset, length);
    setCharacterAttributes(offset, length, getDefaultStyle(), true);
    Range removeRange = new Range(offset, offset+length);
    ArrayList<Range> removeRanges = new ArrayList<Range>();
    for (Range r : links.keySet()) {
      if (r.overlaps(removeRange)){
        removeRanges.add(r);
      }
      else if(r.min > removeRange.max){
        //because next was removed the hyperlink text range gets shifted back
        //the number of charectors removed.
        r.min-=length;
        r.max-=length;
      }
    }
    for (Range range : removeRanges) {
      links.remove(range);
    }
  }

  private void styleLines(int offset, int length,
                          AttributeSet style,
                          Map<HyperlinkPattern, HyperlinkDecorator> patterns)
                          throws BadLocationException {
    int startLine = getDefaultRootElement().getElementIndex(offset);
    int endLine = getDefaultRootElement().getElementIndex(offset + length);
    for(int line = startLine; line <= endLine; line++)
      styleLine(line, style, patterns);
  }

  private void styleLine(int lineNum,
                          AttributeSet style,
                          Map<HyperlinkPattern, HyperlinkDecorator> patterns)
                          throws BadLocationException{

    int startOffset = getDefaultRootElement().getElement(lineNum).getStartOffset();
    int endOffset = getDefaultRootElement().getElement(lineNum).getEndOffset() - 1;
    setCharacterAttributes(startOffset, endOffset-startOffset, style, true);
    String line = getText(startOffset, endOffset - startOffset);
    if (line.isEmpty())
      return;
    //Check if there is a match
    if (patterns == null || patterns.isEmpty()) { return; }
    for (HyperlinkPattern hlp : patterns.keySet()) {
     Matcher m = hlp.getPattern().matcher(line);
     while ( m.find() ) {
       Object result = hlp.getResult(line.substring(m.start(), m.end()),
               m.toMatchResult());
       if ( result != null ) {
         int start = startOffset + m.start();
         int end = start + m.end() - m.start();
         Range range = new Range(start, end);
         HyperlinkDecorator dec = patterns.get(hlp);
         links.put(range, new Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator>(hlp, result, dec));
         setActive(range, false);
         break;
       }
     }
    }
  }

  public void setActive(int pos, boolean active){
    setActive(getRange(pos), active);
  }

  public void setActive(Range range, boolean active){
    SimpleAttributeSet as = new SimpleAttributeSet();
    as.addAttributes(active ? getActiveHyperlinkStyle() : getInactiveHyperlinkStyle());

    Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator> hyperLink = getHyperLink(range);
    HyperlinkPattern hp = hyperLink.a;
    Object r = hyperLink.b;
    HyperlinkDecorator dec = hyperLink.c;

    if (dec != null) {
      AttributeSet s = active ? dec.getActiveStyle(hp, r):dec.getInactiveStyle(hp, r);
      as.addAttributes(s);
    }

    setCharacterAttributes(range.start(), range.length(), as, true);
  }

  public Range getLinkRange(HyperlinkPattern hyperLink) {
    for (Entry<Range, Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator>> entry : links.entrySet()) {
      if (entry.getValue().a == hyperLink) {
        return entry.getKey();
      }
    }
    return null;
  }

  public TreeMap<Range, Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator>> getLinks(){
    return links;
  }

  public Style getActiveHyperlinkStyle() {
    return getStyle(ACTIVE_LINK_STYLE);
  }

  public Style getInactiveHyperlinkStyle() {
    return getStyle(INACTIVE_LINK_STYLE);
  }

}
