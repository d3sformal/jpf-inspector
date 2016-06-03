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

package gov.nasa.jpf.inspector.server.programstate.relop;

/**
 * @author Alf
 * 
 */
class RelOpLessEqual extends RelOpComparableBase {

  /* @see gov.nasa.jpf.inspector.server.programstate.relop.RelationOperator#getNormalizedText() */
  @Override
  public String getNormalizedText () {
    return "<=";
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.relop.RelOpComparableBase#compare(double, double) */
  @Override
  public boolean compare (double left, double right) {
    return left <= right;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.relop.RelOpComparableBase#compare(long, long) */
  @Override
  public boolean compare (long left, long right) {
    return left <= right;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.relop.RelOpComparableBase#compare(char, char) */
  @Override
  public boolean compare (char left, char right) {
    return left <= right;
  }

  /* @see gov.nasa.jpf.inspector.server.programstate.relop.RelOpComparableBase#compare(java.lang.String, java.lang.String) */
  @Override
  public boolean compare (String left, String right) {
    return left.compareTo(right) <= 0;
  }

}
