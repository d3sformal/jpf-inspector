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

/**
 * A data object that describes the result of a string-to-attribute conversion. Implementors of
 * {@link StringToAttributeConverter} will return instances of this object to describe to the Inspector server whether
 * they managed to convert a string to an attribute and if not, why not.
 */
public final class AttributeConversionResult {
  private final boolean success;
  private final Object createdAttribute;
  private final String reasonForRefusal;

  /**
   * Returns true if the converter transformed the input string into an attribute object.
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * Returns the attribute object created by the converted, or null if the conversion failed.
   */
  public Object getCreatedAttribute() {
    return createdAttribute;
  }

  /**
   * If the converted supplied a reason for why the string could not be converted, this will return that reason. If not,
   * or if the conversion succeeded, then this will return null.
   */
  public String getReasonForRefusal() {
    return reasonForRefusal;
  }

  private AttributeConversionResult(boolean success, Object createdAttribute, String reasonForRefusal) {
    this.success = success;
    this.createdAttribute = createdAttribute;
    this.reasonForRefusal = reasonForRefusal;
  }

  /**
   * Creates a new {@link AttributeConversionResult} that reports a successful conversion of the string.
   * @param newAttribute The attribute object that is result of the conversion.
   * @return The result that should be reported to JPF server.
   */
  public static AttributeConversionResult successful(Object newAttribute) {
    return new AttributeConversionResult(true, newAttribute, null);
  }

  /**
   * Creates a new {@link AttributeConversionResult} that reports a failed conversion without a reason given.
   * @return The result that should be reported to JPF server.
   */
  public static AttributeConversionResult failed() {
    return new AttributeConversionResult(false, null, null);
  }

  /**
   * Creates a new {@link AttributeConversionResult} that reports a failed conversion with a reason for why the
   * conversion failed.
   * @param reasonForFailure Explanation of the failure, for example "The given argument was 'dfdfg' but was supposed to be a number.".
   * @return The result that should be reported to JPF server.
   */
  public static AttributeConversionResult failed(String reasonForFailure) {
    return new AttributeConversionResult(false, null, reasonForFailure);
  }
}
