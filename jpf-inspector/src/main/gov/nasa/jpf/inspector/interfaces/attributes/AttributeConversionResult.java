///
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
///

package gov.nasa.jpf.inspector.interfaces.attributes;

public class AttributeConversionResult {
  boolean success;
  Object createdAttribute;
  String reasonForRefusal;

  private AttributeConversionResult(boolean success, Object createdAttribute, String reasonForRefusal) {
    this.success = success;
    this.createdAttribute = createdAttribute;
    this.reasonForRefusal = reasonForRefusal;
  }

  public static AttributeConversionResult successful(Object newAttribute) {
    return new AttributeConversionResult(true, newAttribute, null);
  }
  public static AttributeConversionResult failed() {
    return new AttributeConversionResult(false, null, null);
  }
  public static AttributeConversionResult failed(String reasonForFailure) {
    return new AttributeConversionResult(false, null, reasonForFailure);
  }
}
