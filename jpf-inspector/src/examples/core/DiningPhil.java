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

package core;import gov.nasa.jpf.vm.Verify;

// Taken from jpf-core examples
@SuppressWarnings("ALL")
public class DiningPhil {

  static class Fork {
  }

  static class Philosopher extends Thread {

    Fork left;
    Fork right;

    public Philosopher(Fork left, Fork right) {
      this.left = left;
      this.right = right;
      start();
    }

    public void run() {
      // think!
      synchronized (left) {
        synchronized (right) {
          // eat!
        }
      }
    }
  }
  
  static final int N = 6;

  public static void main(String[] args) {

    int n = Verify.getInt(10,200);

    Verify.beginAtomic();
    Fork[] forks = new Fork[N];
    for (int i = 0; i < N; i++) {
      forks[i] = new Fork();
    }
    for (int i = 0; i < N; i++) {
      new Philosopher(forks[i], forks[(i + 1) % N]);
    }
    Verify.endAtomic();
  }
}