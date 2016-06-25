package gov.nasa.jpf.inspector.frontends.swing.source;

import gov.nasa.jpf.inspector.frontends.swing.AuxiliaryInspectorPanel;
import gov.nasa.jpf.inspector.interfaces.InstructionPosition;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.util.Source;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * The source code view panel allows the user to view the source code of the file where the
 * currently executed instruction resides.
 */
public class SourceCodeViewPanel extends AuxiliaryInspectorPanel {
  private static final long serialVersionUID = -332650333255593274L;
  private final JLabel currentInformationLabel;
  private final JEditorPane sourceCodeTextArea;

  public SourceCodeViewPanel() {
    super("Source", null, "View the source code of the currently executed instruction.");
    setLayout(new BorderLayout());
    sourceCodeTextArea = new JEditorPane();
    sourceCodeTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    add(new JScrollPane(sourceCodeTextArea), BorderLayout.CENTER);
    currentInformationLabel = new JLabel("Current instruction: ???, line ???, file ???.");
    add(currentInformationLabel, BorderLayout.SOUTH);
  }

  @Override
  protected void commandExecutedOrCallbackReceived() {
    if (inspectorClient.isPaused()) {
      Instruction currentInstruction = inspectorClient.getServer().getCurrentInstruction();
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
