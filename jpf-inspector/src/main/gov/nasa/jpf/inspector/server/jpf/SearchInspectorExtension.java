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


import gov.nasa.jpf.search.Search;

/**
 * When using JPF Inspector, your search class MUST implement this interface. Only search classes should
 * implement this interface.
 *
 * See {@link DFSearchInspector} for a reference implementation, or use {@link SearchWrapper} if you don't
 * want to create your own Search subclass.
 */
public interface SearchInspectorExtension {

  /**
   * Implementor should use this method to remember the reference to JPF Inspector.
   * @param inspector The running JPF Inspector.
   */
  void setInspector(JPFInspector inspector);
  
  /**
   * The implementor's terminate method (overriden from the class {@link Search}) must
   * contain the code:
   * <pre>
   * {@code
   * if (inspector != null) {
   *   inspector.getStopHolder().terminating();
   * }
   * }
   * </pre>
   */
  void terminate();
}