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

package gov.nasa.jpf.inspector.server.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ExpressionStateUnaryOperator<T> implements ExpressionStateInterface<T> {

  protected final T child;

  private List<T> childList = null;

  protected ExpressionStateUnaryOperator (T child) {
    this.child = child;
  }

  protected T getChild () {
    return child;
  }

  @Override
  public List<? extends T> getChilds () {
    if (childList == null) {
      List<T> result = new ArrayList<T>(1);
      result.add(child);
      childList = Collections.unmodifiableList(result);
    }
    return childList;
  }

}
