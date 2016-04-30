package runnable;

/**
 * This example shows a simple deadlock.
 */
public class Deadlock implements Runnable {
  /**
   * A name for the thread.
   */
  String name;

  /**
   * A fererence to the other Deadlock object running as a seperate thread.
   */
  Deadlock other;

  public Deadlock (String name) {
    this.name = name;
  }

  public static void main (String[] args) {
    Deadlock o1 = new Deadlock("A");
    Deadlock o2 = new Deadlock("B");

    o1.other = o2;
    o2.other = o1;

    Thread t1 = new Thread(o1);
    Thread t2 = new Thread(o2);

    t1.start();
    t2.start();
  }

  public void run () {
    while (true) {
      System.out.println(name + " cycle start");
      synchronized (this) {
        other.foo();
      }

      System.out.println(name + " cycle end");
    }
  }


  synchronized void foo () {
    System.out.println(name + ".foo() was called");
  }
}