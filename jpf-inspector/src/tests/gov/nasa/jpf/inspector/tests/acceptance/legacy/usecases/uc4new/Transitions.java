package gov.nasa.jpf.inspector.tests.acceptance.legacy.usecases.uc4new;


@SuppressWarnings("ALL")
public class Transitions {
  public static String lastOperation = "NO OPERATION YET";
  public static void main(String[] args) throws InterruptedException {
    Deadlocker one = new Deadlocker();
    event("MAIN: BEFORE START");
    one.start();
    event("MAIN: BEFORE JOIN");
    one.join();
    event("MAIN: BEFORE TERMINATE");
  }
  public static void event(String text) {
    lastOperation = text;
    System.out.println(text);
  }
}
class Deadlocker extends Thread {
  @Override
  public void run() {
    Transitions.event("AUXILIARY: RUNNING");
  }
}