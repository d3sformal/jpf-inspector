package sooth;

@SuppressWarnings("ALL")
public class Quick {
  public static void main(String[] args) {
    testArrayProblem();

    int five = getFive();
    int ten = five + five;
    if (ten == 2 * five) {
      System.out.println("All is ok.");
    } else {
      System.out.println("Something went wrong.");
    }
  }

  private static void testArrayProblem() {

  }

  public static int getFive() {
    return 5;
  }
}
