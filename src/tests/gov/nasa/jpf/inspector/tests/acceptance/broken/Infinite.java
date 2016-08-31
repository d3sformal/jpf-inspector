package gov.nasa.jpf.inspector.tests.acceptance.broken;
@SuppressWarnings("ALL")
public class Infinite {
  public static void main(String[] args) {
    while (true) {
      // Do nothing in this infinite loop.
    }
  }
}
