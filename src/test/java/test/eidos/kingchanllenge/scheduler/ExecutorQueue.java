package test.eidos.kingchanllenge.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*from  ja v  a2 s  .c o m*/
public class ExecutorQueue {
  public static void main(String[] args) {
    final BlockingQueue<Character> bq = new ArrayBlockingQueue<Character>(26);
    final ExecutorService executor = Executors.newFixedThreadPool(2);
    Runnable producer;
    producer = new Runnable() {
      public void run() {
        for (char ch = 'A'; ch <= 'F'; ch++) {
          try {
            bq.put(ch);
            System.out.println("putting:"+ch);
          } catch (InterruptedException ie) {
            assert false;
          }
        }
      }
    };
    executor.execute(producer);
    Runnable consumer;
    consumer = new Runnable() {
      public void run() {
        char ch = '\0';
        do {
          try {
            ch = bq.take();
            System.out.println("getting:"+ch);
          } catch (InterruptedException ie) {
          }
        } while (ch != 'F');
        executor.shutdownNow();
      }
    };
    executor.execute(consumer);
  }
}