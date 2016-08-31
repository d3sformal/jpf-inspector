package gov.nasa.jpf.inspector.tests.acceptance.sut;

@SuppressWarnings("ALL")
public class ExpressionTest {
  static ExpressionTest mainTest;
  ExpressionTest instanceTest;

  int field = 5;

  public static void main(String[] args) {
    int five = 5;

    ExpressionTest test = new ExpressionTest();
    mainTest = test;
    mainTest.mk();

    int ten = five + five; // break here
  }

  static int[] arrayOfIntegers;

  public void mk() {
    instanceTest = this;
    int local = 8;
    int ten = local + 6; // break here
    ExpressionTest asLocal = this;

   }
  public static final class StaticClass {
    static int Hello = 5;
    static String Text = "Text Here";
  }
}
