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

package sooth;

@SuppressWarnings("ALL")
public class Fail {
  public static void nomain(String[] args) {
    testArrayProblem();

    int five = getFive();
    int ten = five + five;
    if (ten == 2 * five) {
      System.out.println("All is ok.");
    } else {
      System.out.println("Something went wrong.");
    }
  }

  private static void testArrayProblem() {

  }

  public static int getFive() {
    return 5;
  }
}
