import java.io.*;
public class Hello2 {
    int test() {
      try {
        String s = "Hello World";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        File f = new File("a.txt");
        System.out.println(s);
        s = null;
        int x = s.length();
        return x;
      } catch(NullPointerException e) {
        System.out.println("Inside NullPointerException e");
        System.out.println("e");
      } catch(Exception e2) {
        System.out.println("Inside Exception e2");
        System.out.println("e2");
      }
      return 0;
    }
}
