package gov.nasa.jpf.shell.util.hyperlinks;

import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.util.LinkDestination;
import java.io.File;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class StacktraceHyperlinkPattern implements HyperlinkPattern{

  private static final Pattern stackpattern = 
          Pattern.compile("at ([\\w\\.]+)\\((\\w+\\.java):(\\d+)\\)");

  public Pattern getPattern() {
    return stackpattern;
  }

  public Object getResult(String str, MatchResult m) {
    String pckg = m.group(1);
    pckg = pckg.substring(0, pckg.lastIndexOf(".",pckg.lastIndexOf(".")-1)+1);
    pckg = pckg.replace('.', File.separatorChar);

    String file = m.group(2);

    String sourcePath = HyperlinkFileCache.getSourcePath(pckg + file);
    if (sourcePath == null){
      return null;
    }else{
      return new LinkDestination(sourcePath, new Integer(m.group(3)));
    }

  }

  public void onClick(Object result) {
    ShellManager.getManager().printLinkCommand((LinkDestination)result);
  }


  public String getTooltip(Object result){
    return "Open file: " + result;
  }

  public int getModifiers() {
    return 0;
  }

}
