package gov.nasa.jpf.inspector.tests.acceptance.sut;

import gov.nasa.jpf.vm.Verify;

@SuppressWarnings("ALL")
public class FieldAccessPath {

  static int AccessedField;

  public static void main(String[] args) throws InterruptedException {

    AccessedField = 4;

    Thread thread = new Thread("Child Thread") {
      @Override
      public void run() {
        breakpoint();
        AccessedField = 10;
        breakpoint();
        AccessedField = 20;
        breakpoint();
      }
    };

    thread.start();


    breakpoint();

    AccessedField = 8;

    breakpoint();

    thread.join();

  }

  private static void breakpoint() {

  }
}
