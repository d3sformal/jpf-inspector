package gov.nasa.jpf.shell.panels.searchgraph;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.util.LinkDestination;
import gov.nasa.jpf.shell.util.hyperlinks.BasicHyperLinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkFileCache;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import gov.nasa.jpf.shell.util.hyperlinks.StringMatcherHyperlinkPattern;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CGNode extends NodeInfo{

  private String nodeLabel = "";
  private String tip = "";
  private String pathtxt = "";
  private int thread = -1;

  BasicHyperLinkDecorator decorator = new BasicHyperLinkDecorator();
  HashMap<HyperlinkPattern, HyperlinkDecorator> links
                        = new HashMap<HyperlinkPattern, HyperlinkDecorator>();

  private static String getChoiceTypeLabel(Class<?> cls){
    String property = ShellManager.getManager().getConfig()
                        .getProperty("visualsearch.label." + cls.getName(), "");

    if (property == null || property.isEmpty()) {
      property =  cls.getName();
      property = property.substring(property.lastIndexOf(".") + 1);
    }
    return property;
  }

  public CGNode(JVM vm){
    super();
    int id = vm.getStateId();
    Class<?> choiceType = vm.getChoiceGenerator().getChoiceType();
    Instruction insn = vm.getChoiceGenerator().getInsn();
    nodeLabel = id + ":" + getChoiceTypeLabel(choiceType);
    tip = "<html>Choice Type: "
           + vm.getChoiceGenerator().getChoiceType().getName()
           + "<br><b>Thread:</b> "+ thread + "<br>Instruction: " + insn + "</html>";
    pathtxt = id + " : " + vm.getChoiceGenerator();
    pathtxt += "\n" + insn.getSourceLine();

    thread = vm.getThreadNumber();

    if (insn.getMethodInfo().getClassInfo() != null) {
      links.put(new StringMatcherHyperlinkPattern(insn.getSourceLine(), insn) {
        @Override
        public void onClick(Object result) {
          Instruction i = (Instruction)result;
          String f = i.getMethodInfo().getClassInfo().getSourceFileName();
          f = HyperlinkFileCache.getSourcePath(f);
          int line = i.getLineNumber();
          ShellManager.getManager()
                  .printLinkCommand(new LinkDestination(f, line));
        }

        @Override
        public int getModifiers(){
          return InputEvent.CTRL_MASK;
        }

        @Override
        public String getTooltip(Object result){
          Instruction i = (Instruction)result;
          String f = i.getMethodInfo().getClassInfo().getSourceFileName();
          f = HyperlinkFileCache.getSourcePath(f);
          int line = i.getLineNumber();
          LinkDestination d =  new LinkDestination(f, line);
          return "open a link to " + d;
        }
      }, decorator);
    }
  }

  @Override
  public String getNodeText(){
    return nodeLabel;
  }

  @Override
  public String getToolTip(){
    return tip;
  }

  @Override
  public String getPathText(){
    return pathtxt;
  }

  @Override
  public int getThread(){
    return thread;
  }

  @Override
  public Map<HyperlinkPattern, HyperlinkDecorator> getHyperlinkPatterns(){
    return links;
  }
}
