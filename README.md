**JPF Inspector** is a debugging tool for *Java Pathfinder*. You may attach the JPF Inspector to a program run using the Java Pathfinder virtual machine to debug it and step through it. It supports breakpoints and single-step execution (forward and backward) at different levels of granularity, and it allows the user to examine and modify program state (threads, call stacks, and heap objects). Unlike with standard debuggers (GDB), it is also possible to control thread scheduling explicitly.

**[Java Pathfinder (JPF)](http://babelfish.arc.nasa.gov/trac/jpf/)** is a framework for formal checking of Java programs. Its core consists of a virtual machine for Java bytecode, running itself on Java; this allows JPF to instrument the code and provide its own functionality for critical instructions. JPF is very extensible and many modules exist for various kinds of verification such as symbolic execution. JPF Inspector is one such JPF module that focuses on debugging capability.

## Quick Start

This project has an extensive documentation at [**this repository's GitHub wiki**](https://github.com/d3sformal/jpf-inspector/wiki). 

You could start with the following articles:

* [User Guide](https://github.com/d3sformal/jpf-inspector/wiki/User-Guide)
* [Tutorial](https://github.com/d3sformal/jpf-inspector/wiki/Tutorial)

The User Guide also contains instructions on how to install JPF Inspector into your system.
