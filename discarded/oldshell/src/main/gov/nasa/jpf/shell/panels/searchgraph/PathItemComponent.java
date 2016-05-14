package gov.nasa.jpf.shell.panels.searchgraph;

import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkDecorator;
import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import java.util.Map;

public interface PathItemComponent {
  String getPathText();
  Map<HyperlinkPattern, HyperlinkDecorator> getHyperlinkPatterns();
}
