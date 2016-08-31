### Scenario
Hello. In this tutorial, we'll explain how to use the Inspector on an example application. We will be debugging the class `sooth.TutorialApplication`. This will be our `TutorialApplication.jpf` file:

```
@using jpf-inspector
shell=.shell.basicshell.BasicShell
target = sooth.TutorialApplication
```
This means that we'll be using the Inspector (first line), using the graphical shell (second line) and debugging the chosen class (third line).

The class itself looks like this:
```Java
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
```
### Setting up
First, we'll download `jpf-core`, `jpf-shell` and `jpf-inspector` from their respective repositories. Then we'll run `ant build` inside each of them to compile them. We'll also need to create a `site.properties` file as per the user guides ([see here](User Guide#launching-jpf-inspector)).
 
 Also, we must make sure that the `TutorialApplication.java` file is in the JPF sourcepath.

Once that is done, we can launch the Inspector using this command:

```
$ java -jar ./jpf-core/build/RunJPF.jar TutorialApplication.jpf
```

The shell window should pop out.

### Test run

Now, we'll run a simple verification without using debugging features. To run the verification, you have three options:

* click the green Verify button (with the tick mark);
* click the "continue" button (in the middle of the row of buttons); or
* execute the command `run` from the console in the tab _JPF Inspector_.

Then, when we switch to the tab _JPF Inspector_, the console will be showing something like this:
```
INFO: JPF created and connected, SuT is started
INFO: JPF Terminating

cmd>
```
This means that JPF started, did something and then terminated. When look at the tab _Verify output_, we'll see that a deadlock was encountered:

```
gov.nasa.jpf.vm.NotDeadlockedProperty
deadlock encountered:
  thread java.lang.Thread:{id:0,name:main,status:WAITING,priority:5,isDaemon:false,lockCount:0,suspendCount:0}
  thread sooth.TutorialApplication$Thread1:{id:1,name:Thread-1,status:BLOCKED,priority:5,isDaemon:false,lockCount:0,suspendCount:0}
  thread sooth.TutorialApplication$Thread2:{id:2,name:Thread-2,status:BLOCKED,priority:5,isDaemon:false,lockCount:0,suspendCount:0}
```
Why, though? Let's debug to find out!

### Setting up a breakpoint
We'll want to start in the method `main`, not at the very start of the application because we don't care about any initializers. We have several options of how we can do this. In this case, we know that the first statement of the method is `lock1 = new Object()`, i.e. an assignment to a static field.

There is a hit condition named `field_write` which triggers whenever something would be written to the specified field. Let's run the following command:

```
cmd>create breakpoint field_write=*.TutorialApplication:lock1
```
This means "stop whenever an instruction would write a value into the field `lock1` of a class whose qualified name ends with `.TutorialApplication`."

We may verify that the breakpoint was successfully created by running `show breakpoint`. We can also view the breakpoint in the tab _Breakpoints_.
```
cmd>show breakpoint
1 :  state=ENABLED hits=0 hitsTotal=0 field_write=*.TutorialApplication:lock1

cmd>
```
Now let's see what happens when we start verification:
```
cmd>run
INFO: JPF created and connected, SuT is started
INFO: Breakpoint hit: [1 :  state=ENABLED hits=1 hitsTotal=1 field_write=*.TutorialApplication:lock1]

cmd>
```
### Examining program state
The breakpoint was hit and execution was stopped. But where? Let's see:
```
cmd>thread_pc
0 : sooth/TutorialApplication.java:23:    lock1 = new Object();
	sooth.TutorialApplication:main:3:putstatic sooth.TutorialApplication.lock1

cmd>
```
The command `thread_pc` (or `thpc` for short) prints the current instruction for all threads. In this case, we see we are at line 23 just before the instruction `putstatic`.

JPF Inspector does not allow you to view bytecode of methods or lines directly, but from a decompiler, we can see that the method `main` begins with the following bytecode:
```
NEW java/lang/Object
DUP
INVOKESPECIAL java/lang/Object.<init> ()V
PUTSTATIC sooth/TutorialApplication.lock1 : Ljava/lang/Object;
```
This is where the `3` in the previous listing comes from: we are just before the instruction with index `3` in this method (instructions are zero-indexed). This means that the object should already be on the stack. Let's print the top stack frame of the stack of the current thread now:

```
cmd>print #stackFrame
sooth.TutorialApplication.main - (in file sooth/TutorialApplication.java:23) - lock1 = new Object();
Stack slots
	0 : args (java.lang.String[]) = []
	1 : ???-RawView-no Name or Type provided (int) = 0
	2 : ???-RawView-no Name or Type provided (int) = 0
	3 : ???-RawView-no Name or Type provided (int) = 347

cmd>
```
On the stack, we first get parameters, then local variables, then temporary variables. In this case, the newly created object is a temporary variable, an operand, here with index `3`. The integer `347` is its index on the heap. Indeed, we can confirm this:

```
cmd>print #heap[347]
#heap[347] (java.lang.Object) = java.lang.Object@347
  No instance or static fields

cmd>
```
### Stepping
Now, this line seems okay so let's move to the next line. We could create a similar breakpoint for writing to `lock2` but since we're already so close, it will be easier to perform a _step_. Using the command `step_instruction` (or `sins` for short) or by clicking the button _Step Instruction_ in the toolbar, we can move one instruction forwards. Let's try it:

```
cmd>sins
INFO: SuT is stopped
	SuT (thread 0) will now execute sooth/TutorialApplication.java:24 (new java.lang.Object), source: lock2 = new Object();

cmd>
```
Yes! We moved to the fourth line. Let's switch to the _Source_ panel to confirm this:

[[TutorialSource.png]]

As you can see, we have now moved to line 24. The console does not have an easy way to poll arbitrary static objects, but we can use the _Explorer_ tab's _Statics_ node to see that the static field `lock1` was already assigned, but `lock2` not yet:

[[TutorialExplorer.png]]

This line also is uninteresting and contains 4 bytecode instructions. To skip over it faster, use the command `step_over` (or `so` for short) to skip all instruction on the current line. You may also the button in the toolbar.

```
cmd>step_over
INFO: SuT is stopped
	SuT (thread 0) will now execute sooth/TutorialApplication.java:25 (new sooth.TutorialApplication$DeadlockSimulator), source: DeadlockSimulator deadlockSimulator = new DeadlockSimulator();

cmd>
```
### Modifying program state
Okay, let's move faster now. We'll create another breakpoint:

```
cmd>create breakpoint it=cond

cmd>show breakpoint
1 :  state=ENABLED hits=1 hitsTotal=1 field_write=*:lock1
4 :  state=ENABLED hits=0 hitsTotal=0 instruction_type=condition
```
Notice how `it=cond` expanded to the hit condition's full form `instruction_type=condition`. This hit condition means "stop just before the next instruction of the specified type" and the type "condition" means "a conditional jump instruction".

Let's resume execution now:

```
cmd>continue
INFO: Breakpoint hit: [4 :  state=ENABLED hits=1 hitsTotal=1 instruction_type=condition]
	SuT (Thread=0) executes the sooth/TutorialApplication.java:27 - if_icmpne 43
```
We moved to line 27 where we can see an `if` statement. Notice we're not at the beginning of the line - we are already just before the actual jump instruction. Let's look at the current stack frame:
```
cmd>print #stackFrame
sooth.TutorialApplication.main - (in file sooth/TutorialApplication.java:27) - if (problematicNumber == magicNumber) {
Stack slots
	0 : args (java.lang.String[]) = []
	1 : deadlockSimulator (sooth.TutorialApplication$DeadlockSimulator) = sooth.TutorialApplication$DeadlockSimulator@352
	2 : magicNumber (int) = 333
	3 : ???-RawView-no Name or Type provided (int) = 333
	4 : ???-RawView-no Name or Type provided (int) = 333

cmd>
```
The top two values on the stack are already loaded: they are `333` and `333`. The block inside the `if` statement contains the suspicious call `deadlockSimulator.causeDeadlock();` which, we can suspect, causes the deadlock we are trying to debug. If this condition evaluated to `false`, maybe the deadlock could be avoided? Let's see and try to change the value of an operand:

```
cmd>set #stackSlot[4] = 20
Value set successfully.

cmd>print #stackFrame
sooth.TutorialApplication.main - (in file sooth/TutorialApplication.java:27) - if (problematicNumber == magicNumber) {
Stack slots
	0 : args (java.lang.String[]) = []
	1 : deadlockSimulator (sooth.TutorialApplication$DeadlockSimulator) = sooth.TutorialApplication$DeadlockSimulator@352
	2 : magicNumber (int) = 333
	3 : ???-RawView-no Name or Type provided (int) = 333
	4 : ???-RawView-no Name or Type provided (int) = 20

cmd>
```
### Conclusion
Okay, now I'm pretty sure the condition won't trigger. Let's resume the program again!
```
cmd>run
INFO: JPF Terminating

cmd>
```
Ok, and what about _Verify Output_?
```
JavaPathfinder core system v8.0 (rev 29+) - (C) 2005-2014 United States Government. All rights reserved.


====================================================== system under test
sooth.TutorialApplication.main()

====================================================== search started: 5.7.16 0:11

====================================================== results
no errors detected

====================================================== statistics
elapsed time:       00:35:08
states:             new=1,visited=0,backtracked=1,end=1
search:             maxDepth=1,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=0
heap:               new=352,released=12,maxLive=0,gcCycles=1
instructions:       3187
max memory:         131MB
loaded code:        classes=61,methods=1323

====================================================== search finished: 5.7.16 0:46
```
No errors detected.

There we go. We have identified a condition that leads to a deadlock.

This concludes this introductory tutorial. JPF Inspector contains many more features, including backstepping and disabling threads, that you might find useful while debugging application. Look at the [[User Guide]] first and then maybe browse through some of the more advanced topics on this website.

We hope that you'll find JPF Inspector useful and debugging a little less annoying.