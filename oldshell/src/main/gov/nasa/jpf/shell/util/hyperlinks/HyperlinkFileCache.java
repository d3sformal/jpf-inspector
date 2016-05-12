package gov.nasa.jpf.shell.util.hyperlinks;

import gov.nasa.jpf.shell.ShellManager;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Serves as a means to hopefully keep calls to the file system to a minimum
 */
public class HyperlinkFileCache {

  //Cache of links
  private static final HashMap<String, String> knownLinks = new HashMap<String, String>();
  private static final HashSet<String> deadLinks = new HashSet<String>();

  public static String getSourcePath(String link){
    //Check out "cache"
    if (deadLinks.contains(link))
      return null;
    if (knownLinks.containsKey(link))
      return knownLinks.get(link);

    //Start searching
    String[] sourcePaths = ShellManager.getManager().getConfig().getStringArray(
            "sourcepath");
    for (String path : sourcePaths) {
      File f = new File(path + File.separator + link);
      if (f.isFile()){
        knownLinks.put(link, f.getAbsolutePath());
        return f.getAbsolutePath();
      }
    }

    deadLinks.add(link);
    return null;
  }

}
