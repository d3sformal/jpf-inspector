package commands;

import gov.nasa.jpf.shell.ShellCommand;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class HelloWorldCommand extends ShellCommand{

  @Override
  public Icon getIcon(){
    return new ImageIcon(getClass().getResource("smiley.gif"));
  }

  @Override
  public void execute() {
    JOptionPane.showMessageDialog(null, "Hello, world!",
            "Hello from jpf-shell",
            JOptionPane.INFORMATION_MESSAGE);
  }

}
