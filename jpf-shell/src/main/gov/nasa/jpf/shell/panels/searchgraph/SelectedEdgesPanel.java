package gov.nasa.jpf.shell.panels.searchgraph;

import gov.nasa.jpf.shell.util.FilterableTextComponent;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SelectedEdgesPanel extends JPanel {

  private JPanel checkBoxContainer = new JPanel();
  private FilterableTextComponent text = new FilterableTextComponent();

  public SelectedEdgesPanel(){
    super();

    setLayout(new BorderLayout());
    add(checkBoxContainer, BorderLayout.NORTH);
    add(new JScrollPane(text), BorderLayout.CENTER);
  }

  public void addCheckBox(JCheckBox checkbox){
    checkBoxContainer.add(checkbox);
  }

  public FilterableTextComponent getFilterableTextComponent(){
    return text;
  }

}
