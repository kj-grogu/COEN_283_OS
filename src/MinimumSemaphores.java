import java.util.concurrent.Semaphore;

/*
 * Question 1: (20 points)
Assume you are given the following wait-graph that represents the relationship between multiple threads (s1,s2,s3,…). 
An arrow from one thread (Sy) to another (Sx) means that thread Sx must finish its computation before Sy starts. 
(For example: S1 has to wait for S2,S5,S8 to finish, S2 has to wait for s3,s6 to finish and so on.)
Use semaphores to enforce this relationship specified by the graph. 
Be sure to show the initial values and the locations of the semaphore operations. 
You will be marked based on finding the best solution with minimum number of semaphores.
 */
public class MinimumSemaphores {


  public static void main(String[] args) {
    Semaphore sm1 = new Semaphore(0); // 2
    Semaphore sm2 = new Semaphore(0); // 2
    Semaphore sm3 = new Semaphore(-1); // 2
    Semaphore sm4 = new Semaphore(-1);
    Semaphore sm5 = new Semaphore(0);
    Semaphore sm6 = new Semaphore(-2);
	Semaphore smdefault = new Semaphore(0);

    Thread S4 = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("S4 executing");
        sm1.release(2);
      }
    });

    Thread S3 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm1.acquire();
          System.out.println("S3 executing");
          sm3.release();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    Thread S7 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm1.acquire();
          System.out.println("S7 executing");
          sm2.release(2);
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    Thread S6 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm2.acquire();
          System.out.println("S6 executing");
          sm3.release();
          sm4.release();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    Thread S9 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm2.acquire();
          System.out.println("S9 executing");
          sm4.release();
          sm5.release();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    Thread S2 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm3.acquire();
          System.out.println("S2 executing");
          sm6.release();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    Thread S5 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm4.acquire();
          System.out.println("S5 executing");
          sm6.release();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    Thread S8 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm5.acquire();
          System.out.println("S8 executing");
          sm6.release();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    Thread S1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sm6.acquire();
          System.out.println("S1 executing");
          sm6.release();
        } catch (InterruptedException e) {
          System.out.println(e);
        }
      }
    });

    S1.start();
    S2.start();
    S3.start();
    S4.start();
    S5.start();
    S6.start();
    S7.start();
    S8.start();
    S9.start();
  }
}