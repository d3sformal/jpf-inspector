package gov.nasa.jpf.shell.panels.searchgraph;

import java.util.Collection;

public interface StateSelectedListener {
	void stateSelected(NodeInfo n, Collection<NodeInfo> path);
}
