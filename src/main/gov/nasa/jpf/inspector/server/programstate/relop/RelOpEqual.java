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

package gov.nasa.jpf.inspector.server.programstate.relop;

import gov.nasa.jpf.vm.ElementInfo;

/**
 * @author Alf
 * 
 */
class RelOpEqual extends RelOpEqualBase {

  @Override
  public String getNormalizedText () {
    return "==";
  }

  @Override
  public boolean compare (boolean left, boolean right) {
    return left == right;
  }

  @Override
  public boolean compare (char left, char right) {
    return left == right;
  }

  @Override
  public boolean compare (String left, String right) {
    return left.equals(right);
  }

  @Override
  public boolean compare (ElementInfo left, ElementInfo right) {
    assert (left != null);
    assert (right != null);
    return left.getObjectRef() == right.getObjectRef();
  }

  @Override
  public boolean compare (double left, double right) {
    return left == right;
  }

  @Override
  public boolean compare (long left, long right) {
    return left == right;
  }

}
