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
public class RelationOperatorFactory {
  protected RelOpEqual relopEqual;
  protected RelOpNotEqual relopNotEqual;
  protected RelOpLess relopLess;
  protected RelOpLessEqual relopLessEqual;
  protected RelOpGreater relopGreater;
  protected RelOpGreaterEqual relopGreaterEqual;

  public RelationOperatorFactory () {
    relopEqual = new RelOpEqual();
    relopNotEqual = new RelOpNotEqual();
    relopLess = new RelOpLess();
    relopLessEqual = new RelOpLessEqual();
    relopGreater = new RelOpGreater();
    relopGreaterEqual = new RelOpGreaterEqual();
  }

  public RelationOperator getRelOpEqual () {
    return relopEqual;
  }

  public RelationOperator getRelOpNotEqual () {
    return relopNotEqual;
  }

  public RelationOperator getRelOpLessThan () {
    return relopLess;
  }

  public RelationOperator getRelOpLessEqual () {
    return relopLessEqual;
  }

  public RelationOperator getRelOpGreaterThan () {
    return relopGreater;
  }

  public RelationOperator getRelOpGreaterEqual () {
    return relopGreaterEqual;
  }

}
