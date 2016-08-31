package sooth;


@SuppressWarnings("ALL") public class Stack {
  public void a() {
    int a = 1;
    b();
  }
  public void b() {
    int b = 3;
    c();
  }
  public void c() {
    int c = 3;
    d();
  }
  public void d() {
    int d = 2 + 2;
  }
  public static void main(String[] args){
    Stack stack = new Stack();
    stack.a();
  }
}
