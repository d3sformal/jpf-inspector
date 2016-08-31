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

package core;

import gov.nasa.jpf.vm.Verify;

/**
 * @author Alf
 * 
 */
@SuppressWarnings("ALL") public class Example02 {

  public void divide () {
    int j = Verify.random(5);
    int i = 10;

    j--;
    assert (j != 0);

    System.out.println("i/j = " + i + "/" + j + " = " + (i / j));
  }

  public static void main (String[] args) {
    Example02 ex = new Example02();
    ex.divide();
  }

}
