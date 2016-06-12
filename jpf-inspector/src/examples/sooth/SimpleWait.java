package sooth;

public class SimpleWait {
  public static void main(String[] args) throws InterruptedException {
    System.out.println("Begin. ");
    int ITERATIONCOUNT = 220_000;
    for (int i = 0; i < ITERATIONCOUNT; i++) {
      i = i + 1;
    }
    System.out.println("Tick. ");
    for (int i = 0; i < ITERATIONCOUNT; i++) {
      i = i + 1;
    }
    System.out.println("Tick. ");
    for (int i = 0; i < ITERATIONCOUNT; i++) {
      i = i + 1;
    }
    System.out.println("Tick. ");
    for (int i = 0; i < ITERATIONCOUNT; i++) {
      ;
    }
    System.out.println("Tick. ");
    for (int i = 0; i < ITERATIONCOUNT; i++) {
      ;
    }
    System.out.println("Tick. ");
    for (int i = 0; i < ITERATIONCOUNT; i++) {
      ;
    }
    System.out.println("End. ");
  }
}
