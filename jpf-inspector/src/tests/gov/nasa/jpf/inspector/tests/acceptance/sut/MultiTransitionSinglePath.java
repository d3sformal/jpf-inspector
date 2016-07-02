package gov.nasa.jpf.inspector.tests.acceptance.sut;

import gov.nasa.jpf.vm.Verify;

@SuppressWarnings("ALL")
public class MultiTransitionSinglePath {
  public static void main(String[] args) {
    int a = 5;
    int transitionStart = Verify.getInt(1, 2);
    int b = a + 4;
    int c = b - 8;
    int transitionStart2 = Verify.getInt(1, 2);
    a = 3;
  }
}
