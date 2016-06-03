//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//  

package gov.nasa.jpf.inspector.server.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.Error;
import gov.nasa.jpf.Property;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.Transition;
import gov.nasa.jpf.report.Reporter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.search.SearchListener;

import java.util.List;

/**
 * Wrapper (decorator) for the search class to use generic Search in the Inspector.
 * Only limited functionality is supported.
 * 
 * You have to specify which search class wrap (which search to use).
 * Wrapped search class should by specified in the search.classwrapped configuration entry.
 * 
 * It means you should use
 * search.class=gov.nasa.jpf.inspector.server.jpf.SearchWrapper
 * search.classwrapped=YOUR ORIGINAL SEARCH CLASS
 * 
 * 
 */
public class SearchWrapper extends Search implements SearchInspectorExtension {
  private JPFInspector inspector;
  private final Search search; // Wrapped search instance

  static {
    // Checks whether never version of the Search class does not have new methods
    // If check fails use
    // Eclipse -> Source -> Generate delegate methods -> Search ->
    // to generate delegates for new methods ... and adds @Override
    if (Search.class.getMethods().length != 49) {
      System.err.println("New version of Search class. Update SearchWrapper appropriately. (See comments)\n\t New search has "
          + Search.class.getMethods().length + " methods.");
      assert (false);
    }
  }

  public SearchWrapper (Config config, VM vm) {
    super(config, vm);

    Class<?>[] searchArgTypes = {
      Config.class,
      VM.class };
    Object[] searchArgs = {
      config,
      vm };
    search = config.getEssentialInstance("search.classwrapped", Search.class, searchArgTypes, searchArgs);
  }

  /**
   * @return Gets wrapped object
   */
  public Search getWrapSearch () {
    return getWrappedSearch(Search.class);
  }

  /**
   * @return Gets wrapped object with type checks. If provided type is incompatible null is returned.
   */
  public <T> T getWrappedSearch (Class<T> type) {
    if (type.isInstance(search)) {
      return type.cast(search);
    }
    return null;
  }

  @Override
  public void setInspector (JPFInspector inspector) {
    this.inspector = inspector;
  }

  @Override
  public void terminate () {
    search.terminate();
    if (inspector != null) {
      inspector.getStopHolder().terminating();
    }
  }

  @Override
  public Config getConfig () {
    return search.getConfig();
  }

  @Override
  public void search () {
    search.search();
  }

  @Override
  public void addProperty (Property newProperty) {
    search.addProperty(newProperty);
  }

  @Override
  public void removeProperty (Property oldProperty) {
    search.removeProperty(oldProperty);
  }

  @Override
  public void addListener (SearchListener newListener) {
    search.addListener(newListener);
  }

  @Override
  public boolean hasListenerOfType (Class<?> type) {
    return search.hasListenerOfType(type);
  }

  @Override
  public void removeListener (SearchListener removeListener) {
    search.removeListener(removeListener);
  }

  @Override
  public List<Error> getErrors () {
    return search.getErrors();
  }

  @Override
  public String getLastSearchConstraint () {
    return search.getLastSearchConstraint();
  }

  @Override
  public Error getLastError () {
    return search.getLastError();
  }

  @Override
  public VM getVM () {
    return search.getVM();
  }

  @Override
  public boolean isEndState () {
    return search.isEndState();
  }

  @Override
  public boolean hasNextState () {
    return search.hasNextState();
  }

  @Override
  public boolean isNewState () {
    return search.isNewState();
  }

  @Override
  public boolean isVisitedState () {
    return search.isVisitedState();
  }

  @Override
  public int getDepth () {
    return search.getDepth();
  }

  @Override
  public String getSearchConstraint () {
    return search.getSearchConstraint();
  }

  @Override
  public Transition getTransition () {
    return search.getTransition();
  }

  @Override
  public int getStateId () {
    return search.getStateId();
  }

  @Override
  public boolean requestBacktrack () {
    return search.requestBacktrack();
  }

  @Override
  public boolean supportsBacktrack () {
    return search.supportsBacktrack();
  }

  @Override
  public boolean supportsRestoreState () {
    return search.supportsRestoreState();
  }

  @Override
  public int getDepthLimit () {
    return search.getDepthLimit();
  }

  @Override
  public void error (Property property) {
    search.error(property);
  }

  @Override
  public void notifySearchConstraintHit (String constraintId) {
    search.notifySearchConstraintHit(constraintId);
  }

  @Override
  public void setIgnoredState (boolean b) {
    search.setIgnoredState(b);
  }

  @Override
  public int getStateDepth (int stateId) {
    return search.getStateDepth(stateId);
  }

  @Override
  public Error getCurrentError () {
    return search.getCurrentError();
  }

  @Override
  public int getNumberOfErrors () {
    return search.getNumberOfErrors();
  }

  @Override
  public int getPurgedStateId () {
    return search.getPurgedStateId();
  }

  @Override
  public boolean hasErrors () {
    return search.hasErrors();
  }

  @Override
  public boolean isDone () {
    return search.isDone();
  }

  @Override
  public boolean isErrorState () {
    return search.isErrorState();
  }

  @Override
  public boolean isIgnoredState () {
    return search.isIgnoredState();
  }

  @Override
  public boolean isProcessedState () {
    return search.isProcessedState();
  }

  @Override
  public void resetProperties () {
    search.resetProperties();
  }

  @Override
  public void setReporter (Reporter reporter) {
    search.setReporter(reporter);
  }

  @Override
  public boolean transitionOccurred () {
    return search.transitionOccurred();
  }

  public static void main (String[] args) {
    System.out.println("Current version of Search class has " + Search.class.getMethods().length + " methods.");
  }
}
