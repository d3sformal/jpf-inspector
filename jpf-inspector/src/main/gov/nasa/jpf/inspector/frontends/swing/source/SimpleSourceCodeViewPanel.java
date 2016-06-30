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

import gov.nasa.jpf.inspector.common.Constants;
import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.util.Source;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * The source code view panel allows the user to view the source code of the file where the
 * currently executed instruction resides.
 *
 * This is an older implementation that uses a simple {@link JEditorPane} and could function as a fallback
 * in case {@link RSyntaxTextArea} somehow doesn't work.
 *
 * However, {@link RSyntaxTextArea} seems to work reliably so this panel could conceivably be deleted altogether.
 */
@SuppressWarnings("unused")
public class SimpleSourceCodeViewPanel extends AuxiliaryInspectorPanel {
  private final JLabel currentInformationLabel;
  private final JEditorPane sourceCodeTextArea;

  public SimpleSourceCodeViewPanel() {
    super("Source", null, "View the source code of the currently executed instruction.");
    setLayout(new BorderLayout());
    sourceCodeTextArea = new JEditorPane();
    sourceCodeTextArea.setFont(Constants.fontMonospaced);
    add(new JScrollPane(sourceCodeTextArea), BorderLayout.CENTER);
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
        if (i == currentInstruction.getLineNumber()) {
          thisLine += " <<< CURRENT LINE";
        }
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
    if (sourcecode != null) {
      sourceCodeTextArea.setText(sourcecode);
    }
  }
}
