package sooth;

import gov.nasa.jpf.vm.Verify;

@SuppressWarnings("ALL")
public class VerifyTest {
  public static void main(String[] args) {

      boolean b = Verify.getBoolean();
      if (b) {
        int c = getFive() + getFive();
        System.out.println("Yes.");
      }
    else {
        int c = getFive() + getFive();
        System.out.println("No.");
      }
    }
  public static int getFive() {
    return 5;
  }
}
