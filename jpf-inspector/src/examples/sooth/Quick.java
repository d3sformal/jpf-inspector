package sooth;

public class Quick {
  public static void main(String[] args) {
    int five = getFive();
    int ten = five + five;
    if (ten == 2 * five) {
      System.out.println("All is ok.");
    } else {
      System.out.println("Something went wrong.");
    }
  }
  public static int getFive() {
    return 5;
  }
}
