//
// Copyright (C) 2011 United States Government as represented by the
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

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.pse.PSEHeapEntryList;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.jpf.JPFInspector;
import gov.nasa.jpf.inspector.utils.expressions.ClassName;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Heap;
import gov.nasa.jpf.vm.VM;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the heap entry list in the second hierarchy.
 */
public class StateHeapEntryList extends StateNode {
  private static final boolean DEBUG = false;

  private final List<StateElementInfo> heapEntries;


  /**
   * Initializes the instance.
   * @param inspector The server.
   * @param cn The class name filter.
   * @param vm The VM.
   */
  public StateHeapEntryList(JPFInspector inspector, ClassName cn, VM vm) {
    super(inspector);

    setStateExpr(PSEVariable.EXPRESSION_VARIABLE_HEAP + '[' + cn.getClassName() + ']');

    heapEntries = filterHeapEntries(vm.getHeap(), cn);
  }

  @Override
  public ProgramStateEntry toHierarchy3() throws JPFInspectorException {
    List<PSEVariable> heapEntryList = new ArrayList<>(heapEntries.size());
    for (StateElementInfo sei : heapEntries) {
      heapEntryList.add(sei.toHierarchy3());
    }
    return new PSEHeapEntryList(heapEntryList);
  }

  /**
   * Returns all objects of the given type on the heap.
   * 
   * @param heap Representation of the heap in JPF
   * @param filter Classes to search for in the heap.
   * @return Representation of picked up heap objects.
   */
  private List<StateElementInfo> filterHeapEntries (Heap heap, ClassName filter) {
    assert heap != null;
    assert filter != null;

    List<StateElementInfo> result = new ArrayList<>();

    for (ElementInfo dei : heap.liveObjects()) {
      // Filter items
      ClassInfo entryType = dei.getClassInfo();
      assert entryType != null;

      if (DEBUG) {
        System.out.println(PSEHeapEntryList.class.getSimpleName() + ".filterHeapEntries - heapEntryType=" + entryType + " ... " + entryType.getName());
      }

      if (!filter.isSameClass(entryType)) {
        continue; // Filtered out
      }

      assert heap.get(dei.getObjectRef()) == dei; // Really check for the same instance

      result.add(StateElementInfo.createElementInfoRepresentation(this, dei));
    }
    return result;
  }

}
