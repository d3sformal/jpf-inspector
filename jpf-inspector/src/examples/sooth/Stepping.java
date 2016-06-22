package sooth;

@SuppressWarnings("ALL")
public class Stepping {
  public static void main(String[] args){
    int outside = 1;
    outside = 2;

    middle();


    int outside_after = 3;
    outside_after = 4;
  }
  public static void middle() {
    int middle = 5;
    middle = 6;

    inside();

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
