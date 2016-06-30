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

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import gov.nasa.jpf.inspector.utils.Debugging;
import gov.nasa.jpf.shell.ShellManager;
import jline.ConsoleOperations;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adds text into the Editor panel.
 * 
 * @author Alf
 * 
 */
public class TextComponentFeeder {
  private static Logger log = Debugging.getLogger(ShellManager.getManager().getConfig());

  private int moveCursorLeft = 0;

  public static void addTextAtTheVeryEnd(String s, JTextComponent console) {
    Document doc = console.getDocument();
    try {
        doc.insertString(doc.getLength(), s, null);
    } catch (BadLocationException e) {
      // Problem with added - ignore this part of the text
      e.printStackTrace();
    }
  }
  /**
   * Adds text into line before the prompt line (the last line)
   * 
   * @param str Text to add
   */
  public synchronized void addUserText (String str, JTextComponent console) {
    if (log.isLoggable(Level.FINER)) {
      log.fine("addUserText:" + str);
    }
    Document doc = console.getDocument();
    try {
      // Before prompt index
      int lastLineStart = getIndexBeforeThePrompt(doc);

      // Append text
      doc.insertString(lastLineStart, str, null);

      // Update cursor position
      console.setCaretPosition(console.getCaretPosition() + str.length());

    } catch (BadLocationException e) {
      // Problem with added - ignore this part of the text
      e.printStackTrace();
    }
  }

  /**
   * 
   * @param doc Document to analyze.
   * @return Gets position after the last character before the command prompt line (the last but one line of the document) or end of the document if empty or single line document. (no prompt shown)
   * @throws BadLocationException This exception should not be thrown.
   */
  private static int getIndexBeforeThePrompt(Document doc) throws BadLocationException {
    String docText = doc.getText(0, doc.getLength());
    int lastLineStart = docText.lastIndexOf('\n');
    if (lastLineStart == -1) {
      // New line not found - the document is either empty or is single-line.
      lastLineStart = docText.length();
    } else if (lastLineStart > 0) {
      // The document contains at least one newline.
      // Add just before last new line (new line of the command prompt)
      if (docText.charAt(lastLineStart - 1) == '\r') {
        lastLineStart--;
      }
    }
    return lastLineStart;
  }

  /**
   * Adds text into last line, where prompt is shown
   * 
   * <br>Note: Not intended to be used by users.
   */
  synchronized void addPromptText(String response, JTextComponent console) {
    if (log.isLoggable(Level.FINE)) {
      log.fine("addPromptText:" + response);
    }
    try {
      Document doc = console.getDocument();

      // Special characters
      // ConsoleOperations.BACKSPACE - move cursor left
      // ConsoleOperations.RESET_LINE - clear whole prompt line
      // ConsoleOperations.KEYBOARD_BELL 0 - ignored character
      // If new character is old character is overwritten

      int promptLineStart = getIndexBeforeThePrompt(doc);
      if (doc.getLength() > promptLineStart) {
        promptLineStart++; // +1 - move to begin of the prompt line
      }

      StringBuilder sb = new StringBuilder(2);

      for (int i = 0; i < response.length(); i++) {
        char c = response.charAt(i);

        if (c == ConsoleOperations.RESET_LINE) {
          doc.remove(promptLineStart, doc.getLength() - promptLineStart);
          moveCursorLeft = 0;

        } else if (c == ConsoleOperations.BACKSPACE) {
          moveCursorLeft++;

        } else if (c == ConsoleOperations.KEYBOARD_BELL) {
          // Ignore bell.
        } else {
          // Standard character

          // Remove character after the cursor position
          if (moveCursorLeft > 0) {
            doc.remove(doc.getLength() - moveCursorLeft, 1);
            moveCursorLeft--;
          }

          sb.setLength(0);
          sb.append(c);
          doc.insertString(doc.getLength() - moveCursorLeft, sb.toString(), null);
        }
      }

      console.setCaretPosition(doc.getLength() - moveCursorLeft);

    } catch (BadLocationException e) {
      // Nothing to do
      e.printStackTrace();
    }
  }


}
