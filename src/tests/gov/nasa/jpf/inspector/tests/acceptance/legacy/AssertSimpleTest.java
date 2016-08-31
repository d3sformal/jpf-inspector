package gov.nasa.jpf.inspector.tests.acceptance.legacy;

import gov.nasa.jpf.vm.Verify;

public class AssertSimpleTest {
  public static void main(String[] args) {
    int i = Verify.getInt(0, 4);
    System.out.println(i);
    return; // <- must be at line 9
  }
}
