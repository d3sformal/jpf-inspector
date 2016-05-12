package gov.nasa.jpf.shell.panels.searchgraph;

import gov.nasa.jpf.shell.util.FilterableText;
import java.util.LinkedList;
import java.util.List;


public class EdgeInfo{

  private String pathText = "";
  private LinkedList<FilterableText> instructions
          = new LinkedList<FilterableText>();

  public EdgeInfo(String pathText){
    this.pathText = pathText;
  }

  public void appendInstructionText(Object owner, String string) {
    instructions.add(new FilterableText(owner, string));
  }

	public class PathInfo{
		public StringBuffer getInfo(){
			StringBuffer buff = new StringBuffer();
			//buff.append("Choice: ").append(choice).append("\n");
			return buff;
		}

		@Override
		public String toString(){
			return getInfo().toString();
		}
	}

	@Override
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append(getPathText());
		s.append("\n===========\n");
    s.append(getInstructionContent().toString());
		return s.toString();
	}

  public String getPathText(){
    return pathText;
  }

  public String getEdgeText(){
    return "";
  }

  public List<FilterableText> getInstructionContent(){
    return instructions;
  }

}
