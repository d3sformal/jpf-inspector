# JPF Inspector Home Page
**JPF Inspector** is a debugging tool for *Java Pathfinder*. You may attach the JPF Inspector to a program run using the Java Pathfinder virtual machine to debug it and step through it. It supports breakpoints and single-step execution (forward and backward) at different levels of granularity, and it allows the user to examine and modify program state (threads, call stacks, and heap objects). Unlike with standard debuggers (GDB), it is also possible to control thread scheduling explicitly.

**[Java Pathfinder (JPF)](http://babelfish.arc.nasa.gov/trac/jpf/)** is a framework for formal checking of Java programs. Its core consists of a virtual machine for Java bytecode, running itself on Java; this allows JPF to instrument the code and provide its own functionality for critical instructions. JPF is very extensible and many modules exist for various kinds of verification such as symbolic execution. JPF Inspector is one such JPF modul that focuses on debugging capability.

## Table of Contents

* *User Documentation*
  * **[User Guide](User-Guide)**
    * [Legacy User Guide](Legacy-User-Guide) (an older version of the guide)
    * [Tutorial: Debugging an example application](Tutorial)
    * [How-to: Add the Inspector to your application](User Guide#launching-jpf-inspector)
    * [How-to: Use the Inspector with Symbolic Pathfinder](Attributes-(SPF))
  * Advanced topics
    * [Configuration](JPF-Inspector-Configuration)
    * [Commands](Commands) (comprehensive list)
    * [Hit Conditions](Hit-Condition-Expression-Syntax)
    * [Expression Syntax](Expression-syntax)
    * [Breakpoints (advanced guide)](Breakpoints-(advanced-guide))
    * [Stepping (advanced guide)](Stepping-(advanced-guide))
    * [Attributes and symbolic execution (SPF)](Attributes)
  * GUI
    * [Panel: Console](Console-Panel)
    * [Panel: Breakpoint Manager](Breakpoint-Manager-Panel)
    * [Panel: Explorer](Explorer-Panel)
    * [Panel: Source](Source-Panel)
    * [Quick Commands](Quick-Commands)
  * Extensibility
    * [Aliases](Aliases)
    * [Custom Hit Conditions](Custom-Hit-Conditions)
    * [Custom Commands](Custom-Commands)
    * [Attribute Adaptors](Attributes-(extensibility))
* *Maintainer Documentation*
  * [History](History)
  * [JPF Startup sequence](JPF Startup)
  * [JPF Inspector Startup sequence](JPF Inspector Startup)
  * [JPF Inspector Architecture](JPF Inspector Architecture)
  * [Migration from JPF6 to JPF8](Migration)
  * [Naming and Code Style](Naming-and-Code-Style)
  * [Threads in JPF Inspector](Threads)
  * [Hierarchies](Hierarchies)
  * [Javadoc Class Documentation](Javadoc)

## Contact information

  * Original author: Pavel Jančík, pavel.jancik at d3s.mff.cuni.cz
  * Project mentor: Pavel Parízek, parizek at d3s.mff.cuni.cz
  * 2016 update author: Petr Hudeček, <petrhudecek2010@gmail.com>


## Other repositories

The code for the JPF Inspector, as it was at the end of 2011, is available at the Mercurial repository at [http://babelfish.arc.nasa.gov/hg/jpf/jpf-inspector](http://babelfish.arc.nasa.gov/hg/jpf/jpf-inspector). However, over the summer of 2016, this GitHub repository (`Soothsilver/gsoc-inspector`) is the primary repistory for the project.