*The JPF Inspector tool does not currently (as of May 2016) work with recent versions of JPF. This will be fixed very soon.*

# JPF Inspector

**JPF Inspector** is a tool for inspection and control of [Java Pathfinder](http://babelfish.arc.nasa.gov/trac/jpf/) execution. It supports breakpoints and single-step execution (forward and backward) at different levels of granularity, and it allows the user to examine and modify program state (threads, call stacks, and heap objects). Unlike with standard debuggers (GDB), it is also possible to control thread scheduling explicitly.

**Java Pathfinder** (JPF) is a framework for formal checking of Java programs. Its core consists of a virtual machine for Java bytecode, running itself on Java; this allows JPF to instrument the code and provide its own functionality for critical instructions. JPF is very extensible and many modules exist for various kinds of verification such as symbolic execution. JPF Inspector is one such JPF modul that focuses on debugging capability.

**More information is available [in the wiki](http://github.com/Soothsilver/gsoc-inspector/wiki).**






## User guide

The [JPF Inspector User guide](documentation/md/userguide.md) describes the currently supported commands and extension points (API) of the Inspector tool. It includes some examples and a screenshot of the Inspector user interface.



