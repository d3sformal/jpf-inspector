//
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

package gov.nasa.jpf.inspector.frontends.swing.source;

import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.util.Source;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

/**
 * The source code view panel allows the user to view the source code of the file where the
 * currently executed instruction resides.
 */
public class SourceCodeViewPanel extends AuxiliaryInspectorPanel {
  private static final long serialVersionUID = -332650333255593274L;
  private static final java.awt.Color currentLineColor = new Color(193,247,213);
  private final JLabel currentInformationLabel;
  private final RSyntaxTextArea textArea;

  public SourceCodeViewPanel() {
    super("Source", null, "View the source code of the currently executed instruction.");
    setLayout(new BorderLayout());
    textArea = new RSyntaxTextArea();
    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    textArea.setCodeFoldingEnabled(true);
    textArea.setEnabled(true);
    RTextScrollPane scrollPane = new RTextScrollPane(textArea);
    scrollPane.setIconRowHeaderEnabled(true);
    add(scrollPane);
    currentInformationLabel = new JLabel("Current instruction: ???, line ???, file ???.");
    add(currentInformationLabel, BorderLayout.SOUTH);
  }

  @Override
  protected void commandExecutedOrCallbackReceived() {
    if (inspectorClient.isPaused()) {
      Instruction currentInstruction = inspectorClient.getServer().getCurrentInstruction();
      if (currentInstruction == null) {
        activeInstructionChanged("???", null, -1, null);
        return;
      }
      MethodInfo methodInfo = currentInstruction.getMethodInfo();
      if (methodInfo == null) {
        activeInstructionChanged(currentInstruction.getMnemonic(), null, currentInstruction.getLineNumber(), null);
        return;
      }
      ClassInfo classInfo = methodInfo.getClassInfo();
      String sourceFileName = classInfo.getSourceFileName();
      if (sourceFileName == null) {
        activeInstructionChanged(currentInstruction.getMnemonic(), null, currentInstruction.getLineNumber(), null);
        return;
      }
      Source source = Source.getSource(sourceFileName);
      if (source == null) {
        activeInstructionChanged(currentInstruction.getMnemonic(), sourceFileName, currentInstruction.getLineNumber(), null);
        return;
      }
      StringBuilder sourceBuilder = new StringBuilder();
      int lineCount = source.getLineCount();
      for (int i = 1; i <= lineCount; i++) {
        String thisLine= source.getLine(i);
        sourceBuilder.append(thisLine).append("\n");
      }
      activeInstructionChanged(currentInstruction.getMnemonic(), sourceFileName, currentInstruction.getLineNumber(), sourceBuilder.toString());
    }
  }

  private void activeInstructionChanged(String mnemonic, String filename, int line, String sourcecode) {
    currentInformationLabel.setText("Current instruction: " + mnemonic +
                                            (line >= 0 ? ", line " + line : "") +
                                            (filename != null ? ", file " + filename : "") +
                                            (sourcecode != null ? "": ", source unavailable"));
    if (sourcecode != null && !textArea.getText().equals(sourcecode)) {
      textArea.setText(sourcecode);
    }
    textArea.removeAllLineHighlights();
    int currentLine = line - 1;

    // Make sure the highlighted line is visible
    FoldManager foldManager = textArea.getFoldManager();
    for (int foldIndex = 0; foldIndex < foldManager.getFoldCount(); foldIndex++) {
      Fold fold = foldManager.getFold(foldIndex);
      if (fold.containsLine(currentLine)) {
        unfoldRecursively(fold, currentLine);
      }
    }

    // Highlight the line
    try {
      textArea.addLineHighlight(currentLine,currentLineColor);
      textArea.setCaretPosition(textArea.getLineStartOffset(currentLine));
    } catch (BadLocationException e) {
      // Ignore.
    }
  }

  private static void unfoldRecursively(Fold fold, int currentLine) {
    fold.setCollapsed(false);
    for (int i =0 ; i < fold.getChildCount(); i++) {
      Fold childFold = fold.getChild(i);
      if (childFold.containsLine(currentLine)) {
        unfoldRecursively(childFold, currentLine);
      }
    }
  }
}
