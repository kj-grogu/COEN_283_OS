import java.util.LinkedList;
import java.util.Random;

/*
 * Question 4: (50 points)
Solve the Producer-Consumer problem using monitors instead of semaphores.
 */

class Monitor {
  private final int LIMIT = 10;
  private final int MAX = 20; // can be increased or decreased to maintain the max size of items produced or consumed
  private int pCount = 0;
  private int cCount = 0;
  private LinkedList<Integer> list = new LinkedList<>();
  private Object lock = new Object();
  

  public void produce() throws InterruptedException {
    int value = 0;
    while (true) {
      synchronized (lock) {
        while (list.size() == LIMIT) {
			lock.wait();  // To wait if the buffer is already full,
        }
        list.add(value++);
		System.out.println(" Produced : " + value);
		System.out.print(" List Size : " + list.size());
		pCount++;
        lock.notify();  // this notifies is used to wake up consumer if it was sleeping because there was no data
        // and now it can resume as producer has produced some data in buffer
		if(pCount > MAX)
			break;
      }
    }
  }

  public void consume() throws InterruptedException {
    Random random = new Random();
    while (true) {
      synchronized (lock) {
        while(list.size() ==0){
			lock.wait();  // TO wait if there is no data in buffer to be consumed yet
        }
        int remove = list.removeFirst();
        System.out.println(" Consumed : " + remove);
        System.out.print(" List Size : " + list.size());
		cCount++;
        lock.notify();  // notifies the producer that it has consumed data from the buffer, and that if producer was
        // waiting because the buffer was full, it can wake up and produce again as consumer has consumed data
		if(cCount > MAX)
			break;
      }
      Thread.sleep(random.nextInt(1000)); // to simulate that consumer is slow to consume
    }
  }
}

public class ProducerConsumerProblem {

  public static void main(String[] args) {
    Monitor monitor = new Monitor();
    Thread producer = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          monitor.produce();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    Thread consumer = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          monitor.consume();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    producer.start();
    consumer.start();

    try {
	producer.join();
      consumer.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
