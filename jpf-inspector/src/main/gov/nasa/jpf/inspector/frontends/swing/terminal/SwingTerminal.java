//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.frontends.swing.terminal;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.inspector.utils.InspectorConfiguration;
import gov.nasa.jpf.shell.ShellManager;
import jline.ConsoleOperations;
import jline.ConsoleReader;
import jline.Terminal;

/**
 * JLine SWING terminal. The terminal is shown as scrollable text panel {@link JEditorPane}.
 * 
 * The terminal has to be created explicitly by constructor. Standard {@see Terminal#setupTerminal()} will not create this terminal.
 * 
 * The terminal creates own {@link ConsoleReader}. To get user input call {@code SwingTerminal.getConsoleReader().readline()}
 * 
 * The {@link #getUserTextPrintStream()} print stream can be used to print additional output to the console (just above the prompt line).
 * 
 */
public final class SwingTerminal extends Terminal {
  private static final boolean DEBUG = false;
  private static final int SINGLE_BYTE_MASK = 0xFF;

  private final JEditorPane console; // Panel where writer s printed and where user writes commands.
  private final JScrollPane scroll; // Scrolling envelope of the console panel

  private boolean echoInput = true; // Marks whether print user input into writer

  /**
   * Transfers pressed keys from KeyListeners to JLine.
   * Stream uses special encoding each character/int is transmitted as sequence of 4 bytes,
   * least significant byte is sent first
   */
  private final PipeHolder pipeKeyListener2JLine;

  private ConsoleReader creader = null;
  private final TextComponentFeeder interpreter;

  private final PrintStream outUserTextPrintStream; // Stream used by users to log(print writer) into console (just before the prompt line)
  private final PrintStream outSimplePrintStream;

  /**
   * @return Creates new terminal with white background and black text.
   */
  public static SwingTerminal getSwingTerminalWhite () {
    return new SwingTerminal(Color.BLACK, Color.WHITE);
  }

  /**
   * @return Creates new terminal with black background and white text.
   */
  public static SwingTerminal getSwingTerminalBlack () {
    return new SwingTerminal(Color.WHITE, Color.BLACK);
  }

  /**
   * Constructor where user can specify used colors.
   * 
   * @param foreground Color of the text.
   * @param backgound Color of the background.
   */
  private SwingTerminal(Color foreground, Color backgound) {
    this(new JTextPane());
    Font defaultFont = console.getFont();

    Font newFont;
    if (fontExists("Courier New")) {
      newFont = new Font("Courier New", defaultFont.getStyle(), defaultFont.getSize());
    } else {
      newFont = new Font(Font.MONOSPACED, defaultFont.getStyle(), defaultFont.getSize());
    }
    console.setFont(newFont);
    console.setBackground(backgound);
    console.setForeground(foreground);
    console.setEditable(false);
    DefaultCaret dc = new DefaultCaret();
    dc.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    dc.setVisible(true);
    console.setCaret(dc);
    console.setCaretColor(foreground);
    console.addFocusListener(new ConsoleFocusListener());

    // Disable the beep on Windows computers
    if (InspectorConfiguration.getInstance().shouldPreventBeeps()) {
      try {
        UIManager.setLookAndFeel(new NoBeepMetalLookAndFeel());
      } catch (UnsupportedLookAndFeelException e) {
        // Beep not disabled.
      }
    }
  }

  /**
   * Creates SwingTerminal with user defined panel (console).
   * 
   * @param console
   *        Console to use
   */
  private SwingTerminal(JEditorPane console) {
    this.console = console;

    scroll = new JScrollPane();
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.getViewport().add(console);

    interpreter = new TextComponentFeeder();

    pipeKeyListener2JLine = new PipeHolder();

    try {
      creader = new ConsoleReader(pipeKeyListener2JLine.getInputStream(), new PromptWriter(), null, this);
      creader.setBellEnabled(false);
    } catch (IOException e) {
      e.printStackTrace();
    }

    KeyListener listener = new ConsoleKeyListener(creader, pipeKeyListener2JLine.getOutpoutStream());
    console.addKeyListener(listener);


    outUserTextPrintStream = new PrintStream(new JLineUserTextOutputStream(interpreter, console));
    outSimplePrintStream = new PrintStream(new JLineSimpleOutputStream(console));
  }

  @SuppressWarnings("FinalizeDeclaration")
  @Override
  protected void finalize () throws Throwable {
    super.finalize();
    pipeKeyListener2JLine.closePipe();
  }

  public JEditorPane getPane () {
    return console;
  }

  /**
   * Gets console reader which could be used to read user commands.
   * 
   * @return Gets console reader which could be used to read user commands.
   */
  public ConsoleReader getConsoleReader () {
    return creader;
  }

  /**
   * ScrollPane envelope given JTextPalen. Use this scroll object to add into swing component.
   * 
   * @return Object used to display console (don't uses JTextPane directly)
   */
  public JScrollPane getScrollPanel () {
    return scroll;
  }

  /**
   * Stream where you can log/write writer which should be shown in the Shell Panel. The writer is written just before prompt line.
   * 
   * @return Stream where you can log/write writer which should be shown in the Shell Panel.
   */
  public PrintStream getUserTextPrintStream () {
    return outUserTextPrintStream;
  }
  /**
   * Stream where you can log/write writer which should be shown in the Shell Panel. The writer is written just before prompt line.
   *
   * @return Stream where you can log/write writer which should be shown in the Shell Panel.
   */
  public PrintStream getSimplePrintStream () {
    return outSimplePrintStream;
  }

  // ***************************************************************************
  // Methods of the Terminal class
  // ***************************************************************************

  @Override
  public void disableEcho () {
    echoInput = false;
  }

  @Override
  public void enableEcho () {
    echoInput = true;
  }

  @Override
  public boolean getEcho () {
    return false;
  }

  @Override
  public int getTerminalHeight () {
    return scroll.getHeight() / console.getFontMetrics(console.getFont()).getHeight();
  }

  @Override
  public int getTerminalWidth () {
    return scroll.getWidth() / console.getFontMetrics(console.getFont()).getWidths()['a'];
  }

  @Override
  public void initializeTerminal () throws Exception {
  }

  @Override
  public boolean isANSISupported () {
    return false;
  }

  @Override
  public boolean isEchoEnabled () {
    return echoInput;
  }

  @Override
  public boolean isSupported () {
    return true;
  }

  @Override
  public InputStream getDefaultBindings () {
    InputStream is = Terminal.class.getResourceAsStream("keybindings.properties");
    if (is == null) {
      System.out.println("Error obtaining keybindings.properties");
    }
    return is;
  }

  @Override
  public int readCharacter (InputStream in) throws IOException {
    int character = 0;

    for (int i = 0; i < 4; i++) {
      int inByte = in.read();
      character |= (inByte &  SINGLE_BYTE_MASK) << (8 * i);
    }

    return character;
  }

  @SuppressWarnings("SameParameterValue")
  private static boolean fontExists (String fontFamilyName) {

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontNames = ge.getAvailableFontFamilyNames();
    for (String fontName : fontNames) {
      if (fontName.equalsIgnoreCase(fontFamilyName)) {
        return true;
      }
    }
    return false;

  }

  /**
   * Writes character/pressed keys from JLine terminal to prompt.
   */
  private final class PromptWriter extends Writer {

    @Override
    public synchronized void write (char[] cbuf, int off, int len) throws IOException {
      interpreter.addPromptText(new String(cbuf, off, len), console);
    }

    @Override
    public synchronized void write (String str) throws IOException {
      interpreter.addPromptText(str, console);
    }

    @Override
    public synchronized void write (String str, int off, int len) throws IOException {
      interpreter.addPromptText(str.substring(off, off + len), console);
    }

    @Override
    public synchronized void flush () throws IOException {
    }

    @Override
    public synchronized void close () throws IOException {
    }

  }

  /**
   * Handles the user input (pressed keys), converts them and sends them into the jLine console (through stream)
   */
  private static class ConsoleKeyListener implements KeyListener {
    private final OutputStream output;

    private final int JL_PREV_CHAR; // Left
    private final int JL_NEXT_CHAR; // Right
    private final int JL_PREV_WORD; // Crtl + Left
    private final int JL_NEXT_WORD; // Ctrl + Right
    private final int JL_MOVE_TO_BEG; // Home
    private final int JL_MOVE_TO_END; // End
    private final int JL_NEXT_HISTORY; // Arrow
    private final int JL_PREV_HISTORY; // Arrow
    private final int JL_PASTE; // Ctrl+V, Shift+Ins
    private final int JL_COMPLETE; // Tab (Ctrl+Space)
    private final int JL_DELETE_PREV_CHAR; // Backspace
    private final int JL_DELETE_NEXT_CHAR; // Delete

    /**
     * @param cr Console used to resolve key mappings.
     * @param output Stream where send pressed keys.
     */
    public ConsoleKeyListener (ConsoleReader cr, OutputStream output) {
      assert (output != null);
      this.output = output;

      // Initialize Key binding
      if (cr == null) {
        // Defaults taken from keybindings.properties
        JL_PREV_CHAR = 2;
        JL_NEXT_CHAR = 6;
        JL_PREV_WORD = 7;
        JL_NEXT_WORD = 3;
        JL_MOVE_TO_BEG = 1;
        JL_MOVE_TO_END = 5;
        JL_NEXT_HISTORY = 14;
        JL_PREV_HISTORY = 16;
        JL_PASTE = 22;
        JL_COMPLETE = 9;
        JL_DELETE_PREV_CHAR = 8;
        JL_DELETE_NEXT_CHAR = 128;
      } else {
        JL_PREV_CHAR = cr.getKeyForAction(ConsoleOperations.PREV_CHAR);
        JL_NEXT_CHAR = cr.getKeyForAction(ConsoleOperations.NEXT_CHAR);
        JL_PREV_WORD = cr.getKeyForAction(ConsoleOperations.PREV_WORD);
        JL_NEXT_WORD = cr.getKeyForAction(ConsoleOperations.NEXT_WORD);
        JL_MOVE_TO_BEG = cr.getKeyForAction(ConsoleOperations.MOVE_TO_BEG);
        JL_MOVE_TO_END = cr.getKeyForAction(ConsoleOperations.MOVE_TO_END);
        JL_NEXT_HISTORY = cr.getKeyForAction(ConsoleOperations.NEXT_HISTORY);
        JL_PREV_HISTORY = cr.getKeyForAction(ConsoleOperations.PREV_HISTORY);
        JL_PASTE = cr.getKeyForAction(ConsoleOperations.PASTE);
        JL_COMPLETE = cr.getKeyForAction(ConsoleOperations.COMPLETE);
        JL_DELETE_PREV_CHAR = cr.getKeyForAction(ConsoleOperations.DELETE_PREV_CHAR);
        JL_DELETE_NEXT_CHAR = cr.getKeyForAction(ConsoleOperations.DELETE_NEXT_CHAR);
      }
    }

    /**
     * Writes int to output stream
     * 
     * @param singleCharacter Value to write into output
     */
    private void write (int singleCharacter) {
      try {
        for (int i = 0; i < 4; i++) {
          output.write(singleCharacter & SINGLE_BYTE_MASK);
          singleCharacter >>>= 8;
        }
      } catch (IOException e) {
        // Cannot write - ignore
      }
    }

    /**
     * Handles special non letter keys (like Home, End, Arrows, ...)
     */
    @Override
    public void keyPressed (KeyEvent e) {
      if (DEBUG) {
        System.out.println("KeyPressed: " + e + "\n\t    e.getKeyCode() ->" + e.getKeyCode() + "\n\te.getModifiers() ->" + e.getModifiers());
      }
      int keycode = e.getKeyCode();
      if (keycode == KeyEvent.VK_LEFT) {
        if ((e.getModifiers() | KeyEvent.CTRL_MASK) == e.getModifiers()) {
          // CTRL pressed
          write(JL_PREV_WORD);
          e.consume();
        } else {
          write(JL_PREV_CHAR);
          e.consume();
        }
      } else if (keycode == KeyEvent.VK_RIGHT) {
        if ((e.getModifiers() | KeyEvent.CTRL_MASK) == e.getModifiers()) {
          // CTRL pressed
          write(JL_NEXT_WORD);
          e.consume();
        } else {
          write(JL_NEXT_CHAR);
          e.consume();
        }
      } else if (keycode == KeyEvent.VK_BEGIN) {
        write(JL_MOVE_TO_BEG);
        e.consume();
      } else if (keycode == KeyEvent.VK_END) {
        write(JL_MOVE_TO_END);
        e.consume();
      } else if (keycode == KeyEvent.VK_DOWN) {
        write(JL_NEXT_HISTORY);
        e.consume();
      } else if (keycode == KeyEvent.VK_UP) {
        write(JL_PREV_HISTORY);
        e.consume();
      } else if (keycode == KeyEvent.VK_INSERT && ((e.getModifiers() | KeyEvent.SHIFT_MASK) == e.getModifiers())) {
        write(JL_PASTE);
        e.consume();
      }

      try {
        output.flush();
      } catch (IOException ignored) {
      }
    }

    @Override
    public void keyReleased (KeyEvent e) {
    }

    @Override
    public void keyTyped (KeyEvent e) {
      if (DEBUG) {
        System.out.println("KeyTyped: " + e);
      }
      e.consume();
      if (e.getKeyChar() == ' ' && ((e.getModifiers() | KeyEvent.CTRL_MASK) == e.getModifiers())) {
        // CTRL+Space -> mapped to TAB to invoke completion
        write(JL_COMPLETE);
      } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        write(JL_DELETE_PREV_CHAR);
      } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
        write(JL_DELETE_NEXT_CHAR);
      } else {
        write(e.getKeyChar());
      }

      try {
        output.flush();
      } catch (IOException ignored) {
      }
    }

  }

  /**
   * /**
   * Shows cursor when focus gained. If {@link JTextPane} is the readOnly panel, then caret is not shown. Even if carret.setVisible() has been called then if
   * panel is show again then the caret is hidden.
   * 
   * This listener repairs this behavior.
   */
  private class ConsoleFocusListener implements FocusListener {

    @Override
    public void focusGained (FocusEvent e) {
      if (DEBUG) {
        System.out.println("DEBUG - SwingTerminal - ConsoleFocusListener.focusGained");
      }
      // Automatically set caret visible
      console.getCaret().setVisible(true);
    }

    @Override
    public void focusLost (FocusEvent e) {
    }
  }

  /**
   * This output stream is used only at the very beginning of the application, to give an introduction message to the user
   * before the prompt is first displayed.
   */
  private static class JLineSimpleOutputStream extends OutputStream {
    private static final Logger log = Debugging.getLogger(ShellManager.getManager().getConfig());

    private final JTextComponent console;

    @Override
    public void write (int b) throws IOException {
      if (log.isLoggable(Level.FINE)) {
        log.fine(this.getClass().getSimpleName() + "write(b=" + ((char) b) + ")");
      }
      char[] c = new char[1];
      c[0] = (char) b;
      TextComponentFeeder.addTextAtTheVeryEnd(new String(c), console);
    }

    @Override
    public void write (byte[] b, int off, int len) throws IOException {
      String str = new String(b, off, len);
      if (log.isLoggable(Level.FINE)) {
        log.fine(this.getClass().getSimpleName() + "write( str=" + str + ")");
      }
      TextComponentFeeder.addTextAtTheVeryEnd(str, console);
    }

    public JLineSimpleOutputStream(JTextComponent console) {
      this.console = console;
    }
  }
  /**
   * Stream which can be used to add/log text into JLineConsole (just before the prompt line)
   * 
   * @author Alf
   * 
   */
  private static class JLineUserTextOutputStream extends OutputStream {
    private static final Logger log = Debugging.getLogger(ShellManager.getManager().getConfig());

    private final TextComponentFeeder interpreter;
    private final JTextComponent console;

    public JLineUserTextOutputStream (TextComponentFeeder interpreter, JTextComponent console) {
      this.console = console;
      this.interpreter = interpreter;
    }

    @Override
    public void write (int b) throws IOException {
      if (log.isLoggable(Level.FINE)) {
        log.fine(this.getClass().getSimpleName() + "write(b=" + ((char) b) + ")");
      }
      char[] c = new char[1];
      c[0] = (char) b;
      interpreter.addUserText(new String(c), console);
    }

    @Override
    public void write (byte[] b, int off, int len) throws IOException {
      String str = new String(b, off, len);
      if (log.isLoggable(Level.FINE)) {
        log.fine(this.getClass().getSimpleName() + "write( str=" + str + ")");
      }
      interpreter.addUserText(str, console);
    }
  }
}
