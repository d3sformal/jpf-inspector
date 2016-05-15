*The JPF Inspector tool does not currently (as of May 2016) work with recent versions of JPF. This will be fixed very soon.*

# JPF Inspector

**JPF Inspector** is a tool for inspection and control of [Java Pathfinder](http://babelfish.arc.nasa.gov/trac/jpf/) execution. It supports breakpoints and single-step execution (forward and backward) at different levels of granularity, and it allows the user to examine and modify program state (threads, call stacks, and heap objects). Unlike with standard debuggers (GDB), it is also possible to control thread scheduling explicitly.

**Java Pathfinder** (JPF) is a framework for formal checking of Java programs. Its core consists of a virtual machine for Java bytecode, running itself on Java; this allows JPF to instrument the code and provide its own functionality for critical instructions. JPF is very extensible and many modules exist for various kinds of verification such as symbolic execution. JPF Inspector is one such JPF modul that focuses on debugging capability.


### History

Development of this tool started in the scope of the  [Google Summer of Code](http://socghop.appspot.com) program in 2010 and continued in 2011 by the then-student [Pavel Jančík](http://d3s.mff.cuni.cz/~jancik/)  and the mentor [Pavel Parízek](http://d3s.mff.cuni.cz/people/parizek/). Some modifications have been made since then but the tool was largely abandoned.

Between 2011 and 2016, JPF kept evolving, going from version 6 to version 8 and moving to more current versions of Java. However, the JPF Inspector code was not brought along and ceased to work with the current versions of JPF and could not be used anymore.

JPF Inspector is currently being updated to integrate with the recent versions of JPF and new functionality is being added as part of [a 2016 Google Summer of Code project](https://summerofcode.withgoogle.com/projects/#4977357207109632).

### Contact

* Original author: Pavel Jančík <pavel.jancik (at) d3s.mff.cuni.cz>
* Project mentor: Pavel Parizek <parizek (at) d3s.mff.cuni.cz
* 2016 update author: Petr Hudeček <petrhudecek2010@gmail.com>

### Other repositories

The code for the JPF Inspector, as it was at the end of 2011, is available at the Mercurial repository at [http://babelfish.arc.nasa.gov/hg/jpf/jpf-inspector](http://babelfish.arc.nasa.gov/hg/jpf/jpf-inspector). That is also where the Inspector's code will likely be hosted at the end of 2016. However, over the summer of 2016, this GitHub repository(`Soothsilver/gsoc-inspector`) is the primary repistory for the project.


## Description

Current features of the JPF Inspector tool include:
 * **Breakpoints.** They can be set to: a specific line of code, instruction at a given position, specific instruction type (invoke, return), read or write access of a variable, object creation and destruction, garbage collection (start and stop), transition boundary (next value choice). Each breakpoint can be in any of the following three states: enabled, disabled, and log. If a breakpoint in the log state is reached, a log message is printed and the execution is not interrupted.
 * **Single-step execution.** It is possible to tell JPF to execute the SuT in a single-step fashion. Both forward and backward direction is supported with the following levels of granularity: instruction and source code line (w/o stopping at nested method calls). Inspector also supports a running mode in which the next choice at each state is selected by the user.
 * **Program state inspection.** Inspector provides a mechanism for exploring program state, including values of heap objects and call stacks of individual threads. Navigation over references (object tree) is supported too - a custom expression language is used internally.
 * **Program state modification.** The user can modify program state to a limited degree, and then check how the modifications influence the program behavior.
 * **Record and replay.** Inspector allows to save all commands executed in the current session into a file and replay them later.
 * **Dynamic assertions.** It is possible to add simple assertions (conditional breakpoints associated with source code locations) dynamically.

JPF Inspector currently provides GUI that is implemented as a new panel for the `jpf-shell` framework.

## 2016 update

We anticipate that many of the following features will be added to the JPF Inspector over the summer of 2016:

* **Update the JPF Inspector.** The Inspector does not work with the current version of Java Pathfinder. The most important task is to bring it up-to-date. Also, the Inspector is tightly integrated with the JPF shell, so if the shell is not working, then update that as well.
* **Command-line interface.** Currently, the JPF Inspector depends on the JPF shell and only works from within its GUI interface. In this project, the Inspector will be modified so that it can run standalone, without JPF shell, and so that it can be run from the command line. This means that it may receive some options as arguments but will still mostly work interactively.
  * In addition, there will be a batch execution mode where the user may specify the commands to run on the debugged application. The output of the commands will be written out. I will discuss with my mentor the specifics of what use cases to optimize this for.
* **Symbolic execution support.** There will be special commands to facilitate symbolic execution. For example, the user will be able to print current values of symbolic inputs and to change them.
* **Refactoring.** The current codebase of the Inspector looks fine. However, this project will still improve on it. First, I will do some refactoring recommended by the  mentor. Then, I will focus on putting the project into a state where it can more easily withstand changes to the JPF and so keep working in spite of JPF’s continued evolution.
* **Documentation upgrade.** I will significantly improve user and programmer documentation for the Inspector. This will include the addition of wiki pages and tutorials. While improving the documentation of JPF itself is not within the scope of this project, I would like to contribute to that, as well, perhaps after the project completes.
* **Support for various JPF modules.** The focus will be on making the Inspector work well with the core and with symbolic execution. However, I will also see whether other existing JPF modules work well with the Inspector and if not, see if support for those modules can be added. I will ignore abandoned modules that are not worked on and that don’t work with the most recent version of JPF.
* **Improve the JPF Inspector GUI.** This will mostly consist of quality-of-life improvements, for example, adding buttons and shortcuts for the most common commands. In addition, I could implement a program state explorer (including a treeview display of heap objects), a breakpoint manager and a source code view.
* **Smaller improvements.**
  * **Fix minor bugs.** The project mentor will provide a list of several small bugs that should be fixed. These are minor issues - for example, the debugger’s output is sometimes not formatted correctly.
  * **New features.** I will implement new commands in the Inspector (for example, more complex conditional breakpoints, or backtracking to a choice point with specific conditions) and several other minor features proposed by the mentor. Among these features may also be extensibility of JPF Inspector.
* **JPF Introspection.** This will include adding new commands that enable the user to view the current state of JPF internal data structures: for example, they could examine the state of choice generators and listeners. This will be useful both in debugging applications and the JPF itself.



## User guide

The [JPF Inspector User guide](documentation/md/userguide.md) describes the currently supported commands and extension points (API) of the Inspector tool. It includes some examples and a screenshot of the Inspector user interface.



