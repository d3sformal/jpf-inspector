package gov.nasa.jpf.inspector.tests.acceptance.features.suppression;

@SuppressWarnings("ALL")
public class ThreadDisablingExample {
  static Object mutex1 = "mutex 1";
  static Object mutex2 = "mutex 2";
  public static void main(String[] args) {
    Thread t1 = new Thread("First Thread") {
      @Override
      public void run() {
        System.out.println("Thread 1 Enter");
        synchronized (mutex1) {
          synchronized (mutex2) {
            // Critical section.
          }
        }
      }
    };

    Thread t2 = new Thread("Second Thread") {
      @Override
      public void run() {
        System.out.println("Thread 2 Enter");
        synchronized (mutex2) {
          synchronized (mutex1) {
            // Critical section.
          }
        }
      }
    };

    // Disable a thread here.
    breakpoint();
    t1.start();
    t2.start();
    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();;
    }
    // Deadlock might have occured.
  }
  private static void breakpoint() {

  }
}
