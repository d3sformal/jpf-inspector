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

public class TutorialApplication {
  static int problematicNumber = 333;

  public static void main(String[] args) throws InterruptedException {
    lock1 = new Object();
    lock2 = new Object();
    DeadlockSimulator deadlockSimulator = new DeadlockSimulator();
    int magicNumber = 111 * 3;
    if (problematicNumber == magicNumber) {
      deadlockSimulator.causeDeadlock();
    }
  }
  static class DeadlockSimulator {
    public void causeDeadlock() throws InterruptedException {
      Thread1 thread1 = new Thread1();
      Thread2 thread2 = new Thread2();
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
    }
  }
  static class Thread1 extends Thread {
    @Override
    public void run() {
      synchronized (lock1) {
        synchronized (lock2) {
          // Do nothing.
        }
      }
    }
  }
  static class Thread2 extends Thread {
    @Override
    public void run() {
      synchronized (lock2) {
        synchronized (lock1) {
          // Do nothing.
        }
      }
    }
  }
  static Object lock1;
  static Object lock2;
}
