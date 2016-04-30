package runnable;


import java.util.Random;

public class LongVerify {
 static final int N = 100;
 static final int M = 3;

 static long sum; // something that causes field CGs
 static int[] values = new int[M];
 
 static final Object lock = new Object();
 
 public static class R implements Runnable {
   int id;
   Random random;
   int n;
   
   public R (int id){
     this.id = id;
     random = new Random(0);
   }
   
   public void run() {
 
     synchronized (lock){ // wait until all threads are created
       n = 0;
     }
     
     int r;
     while (gotNewValue( id, (r = random.nextInt(N)))){
       sum += r;
       Thread.yield();
     }
   }
 }
 
 // something to cause a sync CG
 static synchronized boolean gotNewValue(int id, int val){
   values[id]++;
//System.out.printf("%d = %d\n", id, values[id]);
  
   if (values[id] < N-1){
     return true;
     
   } else {

     for (int i=0; i<M; i++){
       if (values[i] < N-1){
         return false;
       }
     }
     
     // they all have reached the end
     throw new RuntimeException("gotcha");
   }

 }
 
 public static void main (String[] args){
   
   synchronized (lock){
    for (int i=0; i<M; i++){
      Thread t = new Thread( new R(i));
      t.start();
    }
   }
   
   // now unleash them all
 }

}
