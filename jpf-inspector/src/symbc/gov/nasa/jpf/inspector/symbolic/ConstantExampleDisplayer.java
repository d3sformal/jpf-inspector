//
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

package gov.nasa.jpf.inspector.symbolic;

import gov.nasa.jpf.inspector.client.JPFInspectorClientInterface;
import gov.nasa.jpf.inspector.interfaces.attributes.AbstractAttributeToStringConverter;
import gov.nasa.jpf.inspector.interfaces.attributes.AbstractStringToAttributeConverter;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.RealConstant;
import gov.nasa.jpf.symbc.string.StringConstant;

public class ConstantExampleDisplayer extends AbstractAttributeToStringConverter {
  @Override
  public String attributeToString(Object attribute) {
    if (attribute instanceof IntegerConstant) {
      return "constant(" + ((IntegerConstant)attribute).value + ")";
    }
    else if (attribute instanceof StringConstant) {
      return "constant(\"" + ((StringConstant)attribute).value + "\")";
    }
    else if (attribute instanceof RealConstant) {
      return "constant(" + ((RealConstant)attribute).value + ")";
    }
    return null;
  }

  @Override
  public void initialize(JPFInspectorClientInterface inspector) {

  }
}
