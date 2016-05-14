package gov.nasa.jpf.shell.panels.searchgraph;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.control.*;
import java.awt.*;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.commands.VerifyCommand;
import gov.nasa.jpf.shell.listeners.VerifyCommandListener;
import gov.nasa.jpf.shell.util.DistinctColorPool;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.commons.collections15.Transformer;

public class SearchGraphDisplay extends JPanel
        implements VerifyCommandListener, GraphMouseListener<NodeInfo> {

  private static final Stroke SELECTED_STROKE
                                = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                                                  BasicStroke.JOIN_MITER, 10.0f,
                                                  new float[]{9, 3},
                                                  0.0f);

  private static final Stroke PLAIN_STROKE = new BasicStroke();


	private SearchGraphTracker tl = new SearchGraphTracker();
	private VisualizationViewer<NodeInfo, EdgeInfo> graphview;
	private HashSet<NodeInfo> nodePath = new HashSet<NodeInfo>();
	private HashSet<EdgeInfo> edgePath = new HashSet<EdgeInfo>();

	private ArrayList<GraphMouseListener<NodeInfo>> listeners = new ArrayList();

	public SearchGraphDisplay(){

		setLayout(new GridLayout());
		add(new JButton("Click here to run JPF to generate a Search Graph"){{
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					ShellManager.getManager().fireCommand(VerifyCommand.class);
				}
			});
		}});

		//Add our listener
		ShellManager.getManager().addCommandListener(VerifyCommand.class, this);
	}

	public void preCommand(VerifyCommand command) {}

	public void afterJPFInit(VerifyCommand command) {
		command.getJPF().addSearchListener(tl);
		command.getJPF().addVMListener(tl);
	}


	public void postCommand(VerifyCommand command) {
		//Make the graph
		Layout<NodeInfo, EdgeInfo> layout = new TreeLayout<NodeInfo, EdgeInfo>(new DelegateForest<NodeInfo, EdgeInfo>(tl.getGraph()));
		graphview =  new VisualizationViewer(layout, new Dimension(300,300));
		//Add the listeners
		for (GraphMouseListener<NodeInfo> graphMouseListener : listeners) {
			graphview.addGraphMouseListener(graphMouseListener);
		}

    //Colors in the nodes
		graphview.addGraphMouseListener(this);

		//Customize how it displays verticies and edges
    DistinctColorPool colorPool = new DistinctColorPool(tl.getThreadCount());

    //Verticies
		graphview.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		graphview.getRenderContext().setVertexLabelTransformer(new Node2String());
    graphview.getRenderContext().setVertexDrawPaintTransformer(new Node2Outline());
    graphview.getRenderContext().setVertexShapeTransformer(new VertexLabelAsShapeRenderer<NodeInfo, EdgeInfo>(graphview.getRenderContext()));
		graphview.getRenderContext().setVertexFillPaintTransformer(new Node2Color(colorPool));
    graphview.getRenderContext().setVertexStrokeTransformer(new Node2Stroke());
    graphview.setVertexToolTipTransformer(new Node2Tip());

    //Edges
    Edge2Color e2c = new Edge2Color();
    graphview.getRenderContext().setEdgeLabelTransformer(new Edge2String());
    graphview.getRenderContext().setArrowFillPaintTransformer(e2c);
    graphview.getRenderContext().setArrowDrawPaintTransformer(e2c);
    graphview.getRenderContext().setEdgeFillPaintTransformer(e2c);
    graphview.getRenderContext().setEdgeDrawPaintTransformer(e2c);
		graphview.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
    graphview.getRenderContext().setEdgeStrokeTransformer(new Edge2Stroke());

		//Customize how it handles input events
		PluggableGraphMouse gm = new PluggableGraphMouse();
    gm.add(new PickingGraphMousePlugin());
		gm.add(new TranslatingGraphMousePlugin());
		gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(),0,1.1f,1/1.1f));
		graphview.setGraphMouse(gm);

		removeAll();
		add(new GraphZoomScrollPane(graphview));
	}

	public VisualizationViewer<NodeInfo, EdgeInfo> getGraphViewer(){
		return graphview;
	}

	/**
	 * Adds a listener for when nodes are selected on the graph
	 * @param ssl
	 */
	public void addGraphMouseListener(GraphMouseListener<NodeInfo> gml){
		listeners.add(gml);
		if (graphview != null)
			graphview.addGraphMouseListener(gml);
	}

	//-----------  GraphMouseListener<NodeInfo> methods
	public void graphPressed(NodeInfo n, MouseEvent me) {
		nodePath.clear();
    edgePath.clear();
		nodePath.add(n);
    Graph<NodeInfo, EdgeInfo> graph = graphview.getGraphLayout().getGraph();

    //This algorithm assumes that every node only has 1 parent.
    Collection<EdgeInfo> inEdges = graph.getInEdges(n);
    while(!inEdges.isEmpty()){
      for (EdgeInfo edge : inEdges) {
        edgePath.add(edge);
        NodeInfo node = graph.getSource(edge);
        nodePath.add(node);
        inEdges = graph.getInEdges(node);
      }
    }

    Collection<NodeInfo> predecessors = graph.getPredecessors(n);
    while(!predecessors.isEmpty()){
      for (NodeInfo nodeInfo : predecessors) {
        nodePath.add(nodeInfo);
        predecessors = graph.getPredecessors(nodeInfo);
      }
    }
		graphview.repaint();
	}

	public void graphClicked(NodeInfo v, MouseEvent me) {}
	public void graphReleased(NodeInfo v, MouseEvent me) {}

	public void exceptionDuringVerify(Exception ex) { /*Do nothing */ }
	//------------  End GraphhMouseListener<NodeInfo> methods

  // ------------- Begind Transformation Classes --------
	private static class Node2String implements Transformer<NodeInfo, String>{
		public String transform(NodeInfo n) {
			return n.getNodeText();
		}
	}

	private class Node2Color implements Transformer<NodeInfo,Paint>{
    DistinctColorPool dcp;
    public Node2Color(DistinctColorPool dcp){this.dcp = dcp;}
		public Paint transform(NodeInfo n){
      return dcp.getColor(n.getThread());
    }
	}

  private static class Node2Tip implements Transformer<NodeInfo, String> {
    public String transform(NodeInfo i) {
      return i.getToolTip();
    }
  }

  private class Node2Outline implements Transformer<NodeInfo, Paint> {
    public Paint transform(NodeInfo n) {
      return nodePath.contains(n) ? Color.RED.darker() : Color.BLACK;
    }
  }

  private class Node2Stroke implements Transformer<NodeInfo, Stroke> {
    public Stroke transform(NodeInfo n) {
      return nodePath.contains(n) ? SELECTED_STROKE : PLAIN_STROKE;
    }
  }


  private class Edge2Color implements Transformer<EdgeInfo, Paint>{
    public Paint transform(EdgeInfo n){
      return edgePath.contains(n) ? Color.RED.darker() : Color.BLACK;
    }
  }

  private class Edge2Stroke implements Transformer<EdgeInfo, Stroke> {
    public Stroke transform(EdgeInfo i) {
      return edgePath.contains(i) ? SELECTED_STROKE : PLAIN_STROKE;
    }
  }

  private static class Edge2String implements Transformer<EdgeInfo, String>{
    public String transform(EdgeInfo e){
      return e.getEdgeText();
    }
  }

}
