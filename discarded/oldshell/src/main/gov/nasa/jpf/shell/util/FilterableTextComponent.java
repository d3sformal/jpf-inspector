package gov.nasa.jpf.shell.util;

import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;

public class FilterableTextComponent extends HyperlinkEditorPane {

  //Maps an owner to the style associated with it
  private HashMap<Object, Style> object2style = new HashMap<Object, Style>();

  //Owners in this set are displayed
  private HashSet<Object> shown = new HashSet<Object>();

  //Maps an owner to patterns and it decorator
  private HashMap<Object, HashMap<HyperlinkPattern, HyperlinkDecorator>> linkinfo = new HashMap<Object, HashMap<HyperlinkPattern, HyperlinkDecorator>>();

  private List<FilterableText> content = new LinkedList<FilterableText>();

  public FilterableTextComponent(){
    setEditable(false);
  }

  public void rebuildText(){
    clearContent();

    //We need to minimize the amount of inserts that we do.
    Iterator<FilterableText> i = content.iterator();
    Object previousOwner = null;
    StringBuilder txt = new StringBuilder();
    for (FilterableText current : content) {
      if (current.getOwner().equals(previousOwner)) {
        txt.append(current.getText());
      }else{
        insertText(new FilterableText(previousOwner, txt.toString()));
        previousOwner = current.getOwner();
        txt.setLength(0);
      }
    }

  }

  public void appendText(Object owner, String txt){
    FilterableText ft = new FilterableText(owner, txt);
    content.add(ft);

    if (shown.contains(owner)) {
      insertText(ft);
    }

  }

  public void clearContent(){
    try {
      getDocument().remove(0, getDocument().getLength());
    } catch (BadLocationException ex) {
      ShellManager.getManager().getLogger().log(Level.SEVERE,
                                               "Couldn't clear text",
                                               ex);
    }

  }

  /**
   * The real meat and bones of this Component. Actually does the work of
   * adding the string into the document with the proper styles and
   * hyperlink patterns.
   * @param owner
   * @param txt
   */
  private synchronized void insertText(FilterableText ft){
    HyperlinkEditorDocument d = (HyperlinkEditorDocument) getDocument();
    try {
      d.insertString(d.getLength(), ft.getText(), object2style.get(ft.getOwner()),
                     linkinfo.get(ft.getOwner()));
    } catch (BadLocationException ex) {
      ShellManager.getManager().getLogger().log(Level.SEVERE,
              "Couldn't insert text", ex);
    }
  }


  public Style getStyle(Object o){
    Style s = object2style.get(o);
    if (s == null) {
      HyperlinkEditorDocument d = (HyperlinkEditorDocument )getDocument();
      s = d.addStyle(DEFAULT_KEYMAP, d.getDefaultStyle());
      object2style.put(o, s);
    }
    return s;
  }

  public void addHyperlinkPattern(Object owner, HyperlinkPattern pattern){
    if (linkinfo.containsKey(owner) == false) {
      linkinfo.put(owner, new HashMap<HyperlinkPattern, HyperlinkDecorator>());
    }
    HashMap<HyperlinkPattern, HyperlinkDecorator> get = linkinfo.get(owner);
    if (get.containsKey(owner) == false) {
      get.put(pattern, null);
    }
  }

  /**
   * The style to apply to non-mouseovered links that have the given owner
   * and hyperlinkpattern.
   * @param owner
   * @param hp
   * @return
   */
  public void setHyperlinkDecorator(Object owner, HyperlinkPattern hp,
                                    HyperlinkDecorator decorator) {
    if (owner == null){throw new NullPointerException("Owner is null");}
    if (hp == null){throw new NullPointerException("HyperlinkPattern is null");}
    if (decorator == null){throw new NullPointerException("decorator is null");}

    if (linkinfo.containsKey(owner) == false){
      linkinfo.put(owner, new HashMap<HyperlinkPattern, HyperlinkDecorator>());
    }
    linkinfo.get(owner).put(hp, decorator);
    setHyperlinkDecorator(hp, decorator);
  }

  /**
   * Sets the text to be displayed. Once set all of the text is hidden.
   * @param ftext
   */
  public void setContent(List<FilterableText> content) {
    this.content = new LinkedList(content);
    hideAll();
    rebuildText();
  }

  public void hideAll(){
    shown.clear();
  }

  public void showAll(){
    for (FilterableText filterableText : content) {
      shown.add(filterableText.getOwner());
    }
    rebuildText();
  }

  public void setVisible(Object owner, boolean visible){
    setVisible(owner, visible, true);
  }

  public void setVisible(Object owner, boolean visible, boolean rebuild){
    if (shown.contains(owner) == visible) { return; }//No change being made

    if (visible) {
      shown.add(owner);
    }else{
      shown.remove(owner);
    }

    if (rebuild) {
      rebuildText();
    }
  }
}
