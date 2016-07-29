To get started with JPF using jpf-inspector, you may do this:

1. In the directory jpf-core, run "ant build"
2. In the directory jpf-shell, run "ant build"
3. In the directory jpf-inspector, run "ant build"
4. In the directory HelloWorld, compile the example program by running "javac HelloWorld.java".
5. Change the "site.properties" file in the root directory to reflect the actual directory you're running it from (on the first line).
6. From the root directory, run "java -jar ./jpf-core/build/RunJPF.jar ./HelloWorld/helloworld.jpf"

Make sure you run these commands from the given directories.