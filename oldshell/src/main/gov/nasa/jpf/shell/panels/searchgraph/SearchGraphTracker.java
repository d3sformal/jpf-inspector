package gov.nasa.jpf.shell.panels.searchgraph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.Step;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.FieldInstruction;
import gov.nasa.jpf.jvm.bytecode.InstanceFieldInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.LockInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.util.Left;
import java.util.HashMap;

public class SearchGraphTracker extends ListenerAdapter{

  public static final Object INIT_INSTRUCTION = new Object();
  public static final Object INIT_SOURCE = new Object();
  public static final Object INIT_METHOD = new Object();

  public static final Object INSTRUCTION = new Object();
  public static final Object SOURCE = new Object();
  public static final Object METHOD = new Object();

	private DirectedSparseGraph<NodeInfo, EdgeInfo> graph = new DirectedSparseGraph<NodeInfo, EdgeInfo>();
  private NodeInfo root = new NodeInfo("Init");
	private NodeInfo currentNode = root;
  private EdgeInfo currentEdge = new EdgeInfo("Init");

  private HashMap<ChoiceGenerator<?>, CGNode> cg2node
                                  = new HashMap<ChoiceGenerator<?>, CGNode>();

  private Step lastStep;
  private MethodInfo lastMi;
  private String linePrefix;

  private MethodInfo miMain; // just to make init skipping more efficient
  private boolean stillInInit = !ShellManager.getManager().getConfig().getBoolean("et.skip_init", true);
  private Object srcOwner = !stillInInit ? SOURCE : INIT_SOURCE;
  private Object mthOwner = !stillInInit ? METHOD: INIT_METHOD;
  private Object insnOwner = !stillInInit ? INSTRUCTION: INIT_INSTRUCTION;

  private int highestThread = 0;

	public SearchGraphTracker(){
    //Start putting our graph together
		graph.addVertex(root);
	}

	@Override
	public void searchStarted(Search s){
		ClassInfo ci = s.getVM().getMainClassInfo();
    miMain = ci.getMethod("main([Ljava/lang/String;)V", false);
	}

	@Override
	public void choiceGeneratorSet(JVM vm){
    ChoiceGenerator<?> choiceGenerator = vm.getChoiceGenerator();
    if (cg2node.containsKey(choiceGenerator) == false) {//This is new CG
      CGNode cgNode = new CGNode(vm);
      graph.addVertex(cgNode);
      graph.addEdge(currentEdge, currentNode, cgNode, EdgeType.DIRECTED);
      cg2node.put(choiceGenerator, cgNode);
      currentNode = cgNode;
    }else{
      currentNode = cg2node.get(choiceGenerator);
    }
	}

	@Override
	public void choiceGeneratorAdvanced(JVM vm){
    currentEdge = new TransitionEdgeInfo(vm);
    lastStep = null; // in case we report by source line
    lastMi = null;
    linePrefix = null;
    highestThread = Math.max(highestThread, vm.getThreadNumber());
	}
	
  @Override
	public void stateBacktracked(Search s){
    if (!graph.containsEdge(currentEdge)) { //This is the first backtrack
      NodeInfo backtrackNode = new NodeInfo(s.isVisitedState() ? "Visited" : "Ignored");
      graph.addVertex(backtrackNode);
      graph.addEdge(currentEdge, currentNode, backtrackNode, EdgeType.DIRECTED);
    }else{
      //Ok we're backing up multiple times up the path now, go up 1
      //With this kind of graph there should only be 1 parent
      for (NodeInfo parent : graph.getPredecessors(currentNode)) {
        currentNode = parent;
      }
    }
    
		lastStep = null;
		lastMi = null;
	}

	@Override
	public void searchFinished(Search s){
    if (!graph.containsVertex(currentNode)) {
      String txt = s.getErrors().isEmpty() ? "End" : "Error";
      NodeInfo finalNode = new NodeInfo(txt);
      graph.addVertex(finalNode);
      graph.addEdge(currentEdge, currentNode, finalNode, EdgeType.DIRECTED);
    }
	}



	@Override
	public void instructionExecuted(JVM jvm){
    Instruction insn = jvm.getLastInstruction();
    MethodInfo mi = insn.getMethodInfo();

    if (stillInInit && mi == miMain) {
      stillInInit = false;
      srcOwner = SOURCE;
      mthOwner = METHOD;
      insnOwner = INSTRUCTION;
    }

    ThreadInfo ti = jvm.getLastThreadInfo();
    int nNoSrc = 0;

    if (linePrefix == null) {
      linePrefix = Integer.toString( ti.getId()) + " : ";
    }

    Step s = jvm.getLastStep(); // might have been skipped
    if ((s != null) && !s.equals(lastStep)) {
      String line = s.getLineString();
      if (line != null) {
        if (nNoSrc > 0){
          currentEdge.appendInstructionText(srcOwner,
                  "[" + nNoSrc + " insn w/o sources]\n");
        }

        if (!s.sameSourceLocation(lastStep)){
          currentEdge.appendInstructionText(srcOwner,
                  Left.format(s.getLocationString(),30));
          currentEdge.appendInstructionText(srcOwner, " : ");
          currentEdge.appendInstructionText(srcOwner, line.trim());
          currentEdge.appendInstructionText(srcOwner, "\n");
        }
        nNoSrc = 0;

      } else { // no source
        nNoSrc++;
      }
    }

    lastStep = s;

    if (mi != lastMi){
      ClassInfo mci = mi.getClassInfo();
      currentEdge.appendInstructionText(mthOwner, "      ");
      if (mci != null) {
        currentEdge.appendInstructionText(mthOwner, mci.getName());
        currentEdge.appendInstructionText(mthOwner, ".");
      }
      currentEdge.appendInstructionText(mthOwner, mi.getUniqueName());
      currentEdge.appendInstructionText(mthOwner, "\n");
      lastMi = mi;
    }

    currentEdge.appendInstructionText(insnOwner,linePrefix);
    currentEdge.appendInstructionText(insnOwner,"[");
    currentEdge.appendInstructionText(insnOwner,String.valueOf(insn.getInstructionIndex()));
    currentEdge.appendInstructionText(insnOwner,"] ");
    currentEdge.appendInstructionText(insnOwner,insn.toString());

    // annotate (some of) the bytecode insns with their arguments
    if (insn instanceof InvokeInstruction) {
      MethodInfo callee = ((InvokeInstruction)insn).getInvokedMethod();
      if ((callee != null) && callee.isMJI()) { // Huhh? why do we have to check this?
        currentEdge.appendInstructionText(srcOwner, " [native] ");
      }
    } else if (insn instanceof FieldInstruction) {
      currentEdge.appendInstructionText(srcOwner, " ");
      if (insn instanceof InstanceFieldInstruction){
        InstanceFieldInstruction iinsn = (InstanceFieldInstruction)insn;
        currentEdge.appendInstructionText(srcOwner, iinsn.getId(iinsn.getLastElementInfo()));
      } else {
        currentEdge.appendInstructionText(srcOwner, ((FieldInstruction)insn).getVariableId());
      }
    } else if (insn instanceof LockInstruction) {
      LockInstruction lockInsn = (LockInstruction)insn;
      int lockRef = lockInsn.getLastLockRef();
      String lockObjectDescr = jvm.getElementInfo(lockRef).toString();

      currentEdge.appendInstructionText(srcOwner, linePrefix);
      currentEdge.appendInstructionText(srcOwner, lockObjectDescr);
    }
    currentEdge.appendInstructionText(insnOwner, "\n");
  }

  public int getThreadCount() {
    return highestThread + 1;
  }

	public DirectedSparseGraph<NodeInfo, EdgeInfo> getGraph() {
		return graph;
	}
}