package gov.nasa.jpf.inspector.tests.acceptance.sut;

@SuppressWarnings("ALL")
public class Callstack {
  public static void main(String[] args){
    int outside = 1;
    outside = 2;
    int x = 4;
    x =  x + 2;

    int uselessthing1 = 3; middle();


    int outside_after = 3;
    outside_after = 4;
  }
  public static void middle() {
    int middle = 5;
    middle = 6;

    int x = 8;

    int uselessthing2 = 3; inside();

    x = 4;

    int middle_after = 7;
    middle_after = 8;
  }

  private static void inside() {
    int inside = 9;
    inside = 10;
    inside = 11;
    if (true) {
      return;
    } else {
      inside = 12;
    }
  }
}