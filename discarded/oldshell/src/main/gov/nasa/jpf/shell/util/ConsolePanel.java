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
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.util.hyperlinks.BasicHyperLinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.JavaSourceFileHyperlinkPattern;
import gov.nasa.jpf.shell.util.hyperlinks.StacktraceHyperlinkPattern;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * A Panel that emulates a console allowed to input/output/err streams.
 * This panel will output all text received from the input/err streams as
 * text on in a {@link JavaOutputPane} allowing for stack trace highlighting.
 * It also allows for text to be sent through the console.
 */
public class ConsolePanel extends ShellPanel{

	private static final String UN_HOOKED = "NOT_HOOKED";
	private static final String HOOKED = "HOOKED";

	private CardLayout layout = new CardLayout();

	//Filter Controls
	JCheckBox showSTDIN = new JCheckBox("STDIN", true);
	JCheckBox showSTDOUT = new JCheckBox("STDOUT", true);
	JCheckBox showSTDERR = new JCheckBox("STDERR", true);
  HashMap<JCheckBox, Object> checkbox2stream = new HashMap<JCheckBox, Object>();

	//Input panel
	private StreamDisplay streamDisplay = new StreamDisplay();
  private JavaSourceFileHyperlinkPattern jshp = new JavaSourceFileHyperlinkPattern();
  private StacktraceHyperlinkPattern sthp = new StacktraceHyperlinkPattern();
	private JTextArea inputField;
	private KeyListener inputListener;

  boolean hooked = false;
  boolean captureStreams;
  
  public ConsolePanel(String title, Icon icon, String tip){
    super(title, icon, tip);

    
    Config config = ShellManager.getManager().getConfig();
    captureStreams = config.getBoolean("shell.capture_streams", true);
    
    streamDisplay.addHyperlinkPattern(jshp);
    streamDisplay.addHyperlinkPattern(sthp);

		setLayout(layout);
		JPanel hookedPanel = new JPanel();

    hookedPanel.setLayout(new BorderLayout());
    JScrollPane scroller = new JScrollPane(streamDisplay);
		scroller.getViewport().setBackground(Color.WHITE);
    hookedPanel.add(scroller, BorderLayout.CENTER);

		inputField = new JTextArea("Type here to send text to the program's STDIN");
		inputField.setEditable(false);
    inputField.setFont(new Font(null, Font.ITALIC,12));
    inputField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent fe) {
        if (inputField.isEditable()) {
          inputField.setText("");
          inputField.setFont(new Font(null, Font.PLAIN,12));
          //We should only do this once.
          inputField.removeFocusListener(this);
        }
      }
    });

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new GridLayout());
    inputPanel.add(new JScrollPane(inputField));
		hookedPanel.add(inputPanel, BorderLayout.SOUTH);

		JPanel filterPanel = new JPanel();
		filterPanel.add(new JLabel("Showing Streams: "));
		filterPanel.add(showSTDOUT);
		filterPanel.add(showSTDERR);
		filterPanel.add(showSTDIN);

		ActionListener checkListener = new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				JCheckBox src = (JCheckBox)(ae.getSource());
        if (checkbox2stream.get(src) ==null) {
          ShellManager.getManager().getLogger().info(src.getText() + "is null");
        }
				streamDisplay.setVisible(checkbox2stream.get(src), src.isSelected());
			}
		};
		showSTDOUT.setEnabled(false);
		showSTDERR.setEnabled(false);
		showSTDIN.setEnabled(false);

		showSTDOUT.addActionListener(checkListener);
		showSTDERR.addActionListener(checkListener);
		showSTDIN.addActionListener(checkListener);

		hookedPanel.add(filterPanel, BorderLayout.NORTH);

		add(hookedPanel, HOOKED);
		add(unhookedComponent(), UN_HOOKED);
		layout.show(this, UN_HOOKED);
  }

  /**
   * Reads data from the out/err streams and displays them in the JavaOutputPane.
   * Sends input data out through the out stream. Is this naming backwards?  I
   * have no idea, it hurts just to think about it.
   * @param out stream to display in the JavaOutputPane as output
   * @param err stream to display in the JavaOutputPane as error
   * @param in  stream to send input data through.
   */
   public void hookSystemStreams(final InputStream out, final InputStream err, final OutputStream in){
     if (!captureStreams){
       return;
     }
     
		try{
      checkbox2stream.put(showSTDIN, in);
      checkbox2stream.put(showSTDERR, err);
      checkbox2stream.put(showSTDOUT, out);

			streamDisplay.reset();


			//Create a stream that we can pipe into the StreamDisplay
			final PipedOutputStream pipedOutputStream = new PipedOutputStream();
			PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);

      //Style the Hyperlink text
      streamDisplay.setHyperlinkDecorator(out, sthp, new BasicHyperLinkDecorator(Color.BLUE));
      streamDisplay.setHyperlinkDecorator(out, jshp, new BasicHyperLinkDecorator(Color.BLUE));
      streamDisplay.setHyperlinkDecorator(err, sthp, new BasicHyperLinkDecorator(Color.ORANGE));
      streamDisplay.setHyperlinkDecorator(err, jshp, new BasicHyperLinkDecorator(Color.ORANGE));
      streamDisplay.setHyperlinkDecorator(pipedInputStream, sthp, new BasicHyperLinkDecorator(Color.GREEN.darker()));
      streamDisplay.setHyperlinkDecorator(pipedInputStream, jshp, new BasicHyperLinkDecorator(Color.GREEN.darker()));

			//Take care of the data coming in from the streams
			streamDisplay.addStream(out, true);
			streamDisplay.addStream(err, true);
			streamDisplay.addStream(pipedInputStream, true);

      //Style the normal text
			Style stdoutstyle = streamDisplay.getStyle(out);
			StyleConstants.setForeground(stdoutstyle, Color.BLACK);
			showSTDOUT.setForeground(Color.BLACK);

			Style stderrstyle = streamDisplay.getStyle(err);
			StyleConstants.setForeground(stderrstyle, Color.RED);
			showSTDERR.setForeground(Color.RED);


			final Style stdinstyle = streamDisplay.getStyle(in);
			StyleConstants.setForeground(stdinstyle, Color.BLUE);
			showSTDIN.setForeground(Color.BLUE);


			inputField.setEditable(true);
      inputListener = new KeyAdapter() {
        PrintStream streamDisplayStream = new PrintStream(pipedOutputStream);
				PrintStream programInputStream = new PrintStream(in);

        @Override
        public void keyPressed(KeyEvent ke){
          if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ke.isShiftDown()) {
              inputField.append("\n");
            }else{
              String text = inputField.getText();
              streamDisplayStream.println(text);
              programInputStream.println(text);
              inputField.setText("");
            }
            ke.consume();
          }
        }
      };
			inputField.addKeyListener(inputListener);

		}catch(Exception ex){
			ShellManager.getManager().getShell().error(ex);
		}

    hooked = true;
		layout.show(this, HOOKED);

		showSTDOUT.setEnabled(true);
		showSTDERR.setEnabled(true);
		showSTDIN.setEnabled(true);
  }

  /**
   * Stops listening to all of the streams that this panel is hooked to.
   */
  public void unhookSystemStreams(){

    if (hooked){
      inputField.setEditable(false);
      inputField.removeKeyListener(inputListener);

      hooked = false;
    }
  }

	/**
	 * The component shown when the panel is first loaded before any streams
	 * are hooked onto.
	 * @return
	 */
	 protected JComponent unhookedComponent(){
		JLabel notHookedLabel = new JLabel("No streams are hooked", JLabel.CENTER);
		notHookedLabel.setOpaque(true);
		notHookedLabel.setBackground(Color.white);
		return notHookedLabel;
	}

}
