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

package gov.nasa.jpf.inspector.utils.parser;

import org.antlr.runtime.RecognitionException;

/**
 * Internal class. Is used to traverse errors between parser and parser facade.
 * The ANTLR-generated parsers throw a RecognitionException when they encoutner a parse error
 * and this class merely wraps that exception as a RuntimeException (the original exception is not unchecked).
 */
public class RecognitionRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 20100602L;

  private final RecognitionException recognitionException;

  public RecognitionRuntimeException (String msg, RecognitionException recognitionException) {
    super(msg);
    this.recognitionException = recognitionException;
  }

  public RecognitionException getRecognitionException () {
    return recognitionException;
  }

}
