package gov.nasa.jpf.shell.panels.searchgraph;

import gov.nasa.jpf.shell.util.FilterableTextComponent;
import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.*;
import gov.nasa.jpf.shell.panels.searchgraph.*;


import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.util.FilterableText;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SearchGraphPanel extends ShellPanel implements GraphMouseListener<NodeInfo>, ListSelectionListener, VerifyCommandListener{

	//Graph Layout
	private JSplitPane xsplit;
	private JSplitPane ysplit;

	//make our text panels.
  private SelectedEdgesPanel selectedEdgesPanel = new SelectedEdgesPanel();
	private DefaultListModel path = new DefaultListModel();
  private PathList pathview = new PathList(path);
	private SearchGraphDisplay graphview;

  //Default settings, these get overriden by the JPF config
	private Config config = ShellManager.getManager().getConfig();
  private boolean printInsn = config.getBoolean("et.print_insn", true);
  private boolean printSrc  = config.getBoolean("et.print_src",  true);
	private boolean printMth  = config.getBoolean("et.print_mth",  false);
  private boolean skipInit  = config.getBoolean("et.skip_init",  true);


	public SearchGraphPanel(){
		super("Search Graph", null, "View the paths taken by jpf.");
		ShellManager.getManager().addCommandListener(VerifyCommand.class, this);
    graphview = new SearchGraphDisplay();

		selectedEdgesPanel.setPreferredSize(new Dimension(300,150));

    final JCheckBox initBox = new JCheckBox("Show Initialization", !skipInit);
    initBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ie) {
        FilterableTextComponent ftc = selectedEdgesPanel.getFilterableTextComponent();
        boolean selected = initBox.isSelected();
        ftc.setVisible(SearchGraphTracker.INIT_INSTRUCTION, selected, false);
        ftc.setVisible(SearchGraphTracker.INIT_SOURCE, selected, false);
        ftc.setVisible(SearchGraphTracker.INIT_METHOD, selected, false);
        ftc.rebuildText();
      }
    });

    final JCheckBox insnBox = new JCheckBox("Show Instructions", printInsn);
    insnBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ie) {
        selectedEdgesPanel.getFilterableTextComponent().setVisible(
                SearchGraphTracker.INSTRUCTION, insnBox.isSelected(), true);
      }
    });

    final JCheckBox mthBox = new JCheckBox("Show Methods", printMth);
    insnBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ie) {
        selectedEdgesPanel.getFilterableTextComponent().setVisible(
                SearchGraphTracker.METHOD, mthBox.isSelected(), true);
      }
    });

    final JCheckBox srcBox = new JCheckBox("Show Source", printSrc);
    insnBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ie) {
        selectedEdgesPanel.getFilterableTextComponent().setVisible(
                SearchGraphTracker.SOURCE, srcBox.isSelected(), true);
      }
    });

    selectedEdgesPanel.addCheckBox(initBox);
    selectedEdgesPanel.addCheckBox(insnBox);
    selectedEdgesPanel.addCheckBox(mthBox);
    selectedEdgesPanel.addCheckBox(srcBox);

		//----- Ugly Swing Code ----
		//Split across the top
		xsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JScrollPane pathScroller = new JScrollPane(pathview);
    TitledBorder titledBorder = new TitledBorder("View Log");
    titledBorder.setTitlePosition(TitledBorder.TOP);
    titledBorder.setTitleJustification(TitledBorder.CENTER);
		xsplit.setBottomComponent(graphview);
		xsplit.setTopComponent(pathScroller);
		graphview.addGraphMouseListener(this);

		ysplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		ysplit.setTopComponent(xsplit);
		ysplit.setBottomComponent(selectedEdgesPanel);

		//Just make the split as large as possible
		setLayout(new GridLayout());
		add(ysplit);
		resetDividerPositions();

    //Add a selection listener to the pathList
    pathview.addListSelectionListener(this);
	}

	public void graphPressed(NodeInfo clickedNode, MouseEvent me) {
 		path.clear();
    Graph<NodeInfo, EdgeInfo> graph = graphview.getGraphViewer()
                                      .getGraphLayout().getGraph();
    path.add(0, clickedNode);
    Collection<NodeInfo> predecessors = graph.getPredecessors(clickedNode);
    while(predecessors.isEmpty() == false){
      NodeInfo n = predecessors.iterator().next();
      path.add(0, n);
      predecessors = graph.getPredecessors(n);
    }
    pathview.setSelected(0, true);
		pathview.repaint();
	}

	public void graphClicked(NodeInfo v, MouseEvent me) {}
	public void graphReleased(NodeInfo v, MouseEvent me) {}

  /**
   * This displays the instructions of the transition displayed on the
   * left side on the bottom panel.
   * @param e
   */
	public void valueChanged(ListSelectionEvent e) {
    LinkedList<FilterableText> c = new LinkedList<FilterableText>();

    Graph<NodeInfo, EdgeInfo> graph = graphview.getGraphViewer().getGraphLayout().getGraph();
    for (NodeInfo ei : pathview.getSelectedValues()) {
      for (EdgeInfo edgeInfo : graph.getInEdges(ei)) {
        c.addAll(edgeInfo.getInstructionContent());
      }
    }
    selectedEdgesPanel.getFilterableTextComponent().setContent(c);
		selectedEdgesPanel.getFilterableTextComponent().setCaretPosition(0);
		selectedEdgesPanel.repaint();
	}

	public final void resetDividerPositions(){
    Dimension minimumSize = graphview.getMinimumSize();
    graphview.setMinimumSize(new Dimension((int) minimumSize.getWidth(), 200));
		ysplit.setDividerLocation(0.8);
    xsplit.setDividerLocation(0.5);
	}


	public void postCommand(VerifyCommand command) {
		resetDividerPositions();
    repaint();
	}

	public void afterJPFInit(VerifyCommand command) { }
	public void preCommand(VerifyCommand command) {}

	public void exceptionDuringVerify(Exception ex) {
		//Do nothing...
	}

}