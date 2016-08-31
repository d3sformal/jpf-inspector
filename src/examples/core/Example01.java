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
// Suitable for manual single stepping tests
//  See example of JPF Commands
//
// # After each forward step - "so" command - checks "Verify output" tab to see output from executed program.
// # Terminate at before compute02() method call
// # Create breakpoint at line 82
// #   create breakpoint position=Example01.java:82
// cr bp pos=Example01.java:82
// # Run and wait until breakpoint is hitted
// run
// # "Step over" compute02 call
// so
// # "Step over" print("End");
// so
// # Now we exit the main method, we stopped just after return instruction
// #  so to return back to the main method we undo the return instruction
// # back_step_in
// bsin
// # Step backward before compute01
// #    undo print("End")
// bso
// #    undo compute02()
// bso
// #    undo print("Before compute02");
// bso
// #    undo compute01();
// bso
// #    undo print("Before compute01");
// bso
// # Coninue in forward step -> execute print("Before compute01"); again
// so
//
// run

@SuppressWarnings("ALL")
public class Example01 {

  // Stores value and print it later
  static class Print {
    private final int i;

    public Print(int i) {
      this.i = i;
    }

    public void printValue() {
      // Invoke Special
      printValuePrivate();
    }

    private void printValuePrivate() {
      System.out.println("Stored value: " + i);
    }
  }

  // Uses INVOKEVIRTUAL call
  public static void compute01() {
    for (int i = 0; i < 10; i++) {
      Print p = new Print(i);
      p.printValue();
    }
  }

  // Uses INVOKEVIRTUAL call with CG
  public static void compute02() {
    for (int i = 0; i < 10; i++) {
      Print p = new Print(Verify.getInt(0, 2));
      p.printValue();
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    Verify.breakTransition("In JPF8, a reason is required.");
    for (int i = 0; i < 2; i++) {
      System.out.println("Before compute01");
      compute01();
    }
    System.out.println("Before compute02");
    compute02();
    System.out.println("End");
  }

}
