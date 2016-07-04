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

package core;// Taken from jpf-core examples
/* WHITESPACE IS SIGNIFICANT HERE: AUTOMATED TESTS USE LINE NUMBERS OF SPECIFIC STATEMENTS IN THIS FILE.
 * This is the famous Bridge-crossing puzzle. The aim is to see what the minimum
 * amount of time is for 4 people to cross with flash-light (torch): 10.5.2.1.
 * The answer is 17 for the given configuration.
 *
 * Purpose of this example is to show how to use the 'Verify' API in test
 * applications (usually drivers)
 *
 * One can find this solution with JPF in BFS search mode - DFS will not
 * work since this is an infinite-state program (time keeps increasing).
 *
 * If one uses the ignoreIf(cond) call the branches that will lead to
 * solutions worst than the current will be cut-off and this will allow
 * DFS also to complete - and BFS to terminate faster.
 *
 * When setting the native flag in main one can also save information
 * from one run to the next using JPF's native peer methods -
 * see JPF_Crossing.java for the code of getTotal() and setTotal(int).
 */

import gov.nasa.jpf.vm.Verify;

@SuppressWarnings("ALL") class Constants {

  public static final boolean east = true;
  public static final boolean west = false;
}

@SuppressWarnings("ALL") class Torch {

  public static boolean side = Constants.east;

  public String toString() {
    if (side == Constants.east) {
      return "east";
    } else {
      return "west";
    }
  }
}

@SuppressWarnings("ALL") class Bridge {

  static Person[] onBridge = new Person[2];
  static int numOnBridge = 0;

  public static boolean isFull() {
    return numOnBridge != 0;
  }

  public static int Cross() {
    int time = 0;
    Torch.side = !Torch.side;

    if (numOnBridge == 1) {
      onBridge[0].side = Torch.side;
      time = onBridge[0].time;

    //System.out.println("Person " + onBridge[0] +
    //                     " moved to " + Torch.side +
    //               " in time " + time);
    } else {
      assert onBridge[0] != null : "Argh, null " + numOnBridge;
      assert onBridge[1] != null;

      onBridge[0].side = Torch.side;
      onBridge[1].side = Torch.side;

      if (onBridge[0].time > onBridge[1].time) {
        time = onBridge[0].time;
      } else {
        time = onBridge[1].time;
      }

    //System.out.println("Person " + onBridge[0] +
    //                 " and Person " + onBridge[1] +
    //                 " moved to " + Torch.side +
    //                 " in time " + time);
    }

    return time;
  }

  public static void clearBridge() {
    if (numOnBridge == 0) {
      return;
    } else if (numOnBridge == 1) {
      onBridge[0] = null;
      numOnBridge = 0;
    } else {
      onBridge[0] = null;
      onBridge[1] = null;
      numOnBridge = 0;
    }
  }

  public static void initBridge() {
    onBridge[0] = null;
    onBridge[1] = null;
    numOnBridge = 0;
  }

  public static boolean tryToCross(Person th) {
    if ((numOnBridge < 2) && (onBridge[0] != th) && (onBridge[1] != th)) {
      onBridge[numOnBridge++] = th;

      return true;
    } else {
      return false;
    }
  }
}

@SuppressWarnings("ALL") class Person {
  // person to cross the bridge

  int id;
  public int time;
  public boolean side;

  public Person(int i, int t) {
    time = t;
    side = Constants.east;
    id = i;
  }

  public void move() {
    if (side == Torch.side) {
      if (!Verify.getBoolean()) {
        Bridge.tryToCross(this);
      }
    }
  }

  public String toString() {
    return "" + id;
  }
}

@SuppressWarnings("ALL") public class Crossing {

  public static native void setTotal(int time);

  // due to these natives the code only runs in JPF
  public static native int getTotal();

  public static void main(String[] args) {
    //Verify.beginAtomic();

    // when natives are used one can record the minimal value found so
    // far to get a better one on the next execution - this
    // requires the -mulitple-errors option and then the last error
    // found must be replayed
    boolean isNative = false;

    if (isNative) {
      setTotal(30); // set to value larger than solution (17)
    }

    int total = 0;
    boolean finished = false;
    Bridge.initBridge();

    Person p1 = new Person(1, 1);
    Person p2 = new Person(2, 2);
    Person p3 = new Person(3, 5);
    Person p4 = new Person(4, 10);
    //Verify.endAtomic();

    while (!finished) {
      //Verify.beginAtomic();
      p1.move();
      p2.move();
      p3.move();
      p4.move();

      if (Bridge.isFull()) {
        total += Bridge.Cross();

        if (isNative) {
          Verify.ignoreIf(total > getTotal());
        } else {
          Verify.ignoreIf(total > 17); //with this DFS will also find error
        }

        Bridge.clearBridge();

        //printConfig(p1,p2,p3,p4,total);
        finished = !(p1.side || p2.side || p3.side || p4.side);
      }

    //Verify.endAtomic();
    }

    //Verify.beginAtomic();

    if (isNative) {
      if (total < getTotal()) {
        System.out.println("new total " + total);
        setTotal(total);
        assert (total > getTotal());
      }
    } else {
      System.out.println("total time = " + total);
      assert (total > 17);
    }

  //Verify.endAtomic();
  }

  static void printConfig(Person p1, Person p2, Person p3, Person p4,
          int total) {
    if (p1.side == Constants.east) {
      System.out.print("p1(" + p1.time + ")");
    }

    if (p2.side == Constants.east) {
      System.out.print("p2(" + p2.time + ")");
    }

    if (p3.side == Constants.east) {
      System.out.print("p3(" + p3.time + ")");
    }

    if (p4.side == Constants.east) {
      System.out.print("p4(" + p4.time + ")");
    }

    System.out.print(" - " + total + " -> ");

    if (p1.side == Constants.west) {
      System.out.print("p1(" + p1.time + ")");
    }

    if (p2.side == Constants.west) {
      System.out.print("p2(" + p2.time + ")");
    }

    if (p3.side == Constants.west) {
      System.out.print("p3(" + p3.time + ")");
    }

    if (p4.side == Constants.west) {
      System.out.print("p4(" + p4.time + ")");
    }

    System.out.println();
  }
}
