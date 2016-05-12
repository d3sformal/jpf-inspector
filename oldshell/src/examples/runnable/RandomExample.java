package runnable;

import java.util.Random;

public class RandomExample {
	
	public static void main (String[] args) {

          Random random = new Random(42);      // (1)

          int a = random.nextInt(2);           // (2)
          System.out.println("a=" + a);

          //... lots of code here

          int b = random.nextInt(3);           // (3)
          System.out.println("  b=" + b);

          int c = a/(b+a -2);                  // (4)
          System.out.println("    c=" + c);
     }


}
