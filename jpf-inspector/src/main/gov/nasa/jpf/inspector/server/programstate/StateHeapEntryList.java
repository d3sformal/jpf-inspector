//
// Copyright (C) 2010-2011 Pavel Jančík
// Copyright (C) 2016 Petr Hudeček
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package gov.nasa.jpf.inspector.server.programstate;

import gov.nasa.jpf.inspector.common.pse.PSEHeapEntryList;
import gov.nasa.jpf.inspector.common.pse.PSEVariable;
import gov.nasa.jpf.inspector.common.pse.ProgramStateEntry;
import gov.nasa.jpf.inspector.exceptions.JPFInspectorException;
import gov.nasa.jpf.inspector.server.attributes.AttributesManager;
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
  public ProgramStateEntry toHierarchy3(AttributesManager attributeManager) throws JPFInspectorException {
    List<PSEVariable> heapEntryList = new ArrayList<>(heapEntries.size());
    for (StateElementInfo sei : heapEntries) {
      heapEntryList.add(sei.toHierarchy3(attributeManager));
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
