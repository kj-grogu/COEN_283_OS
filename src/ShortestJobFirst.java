import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestJobFirst {

//Job defination:
  private static class Job {
    private int index, arrivalTime, burst;
    private int completionTime, waitTime, turnAroundTime;

    public Job(int ii, int arvT, int burstT) {
      index = ii;
      arrivalTime = arvT;
      burst = burstT;
    }

    public void computeWaitAndTurnAround(int ct) {
      completionTime = ct;
      turnAroundTime = completionTime - arrivalTime;
      waitTime = turnAroundTime - burst;
    }

    @Override
    public String toString() {
      return "Id: " + (this.index + 1 )+ ", AT : "  +this.arrivalTime + ", BT : " + this.burst + ", CT : " + this.completionTime + ", TT : " + this.turnAroundTime + ", WT : " + this.waitTime;
  	}
  }
  public static void main(String[] args) {
	// Process Input:
    // int[] processBurst = new int[]{7, 4, 1, 4, 1, 2};
    // int[] processArrivalTime = new int[]{0, 2, 4, 5, 16, 18};
    int[] processBurst = new int[]{7, 4, 1, 4};
    int[] processArrivalTime = new int[]{0, 2, 4, 5};
	System.out.println("===================================================");
	System.out.println("Jobs To Run: ");
	System.out.println("===================================================");
	for(int jb=0;jb<processBurst.length;jb++){
		System.out.println("Job "+ (jb+1) + " | Burst Time: "+ processBurst[jb] + " | Arival Time: "+ processArrivalTime[jb]);
	}
	System.out.println("===================================================");

//    int[] processBurst = new int[]{7, 1};
//    int[] processArrivalTime = new int[]{2, 5};


//    int[] processBurst = new int[]{7};
//    int[] processArrivalTime = new int[]{5};


    int N = processBurst.length;

    Job[] jobs = new Job[N];
    for (int i = 0; i < N; i++) {
      jobs[i] = new Job(i, processArrivalTime[i], processBurst[i]);
    }

	//Defination of MinHeap:
    PriorityQueue<Job> minHeap = new PriorityQueue<>(new Comparator<Job>() {
      @Override
      public int compare(Job o1, Job o2) {
        if (o1.burst == o2.burst) // if burst are same, pick the job that arrived first
          return o1.arrivalTime - o2.arrivalTime;

        return o1.burst - o2.burst;
      }
    });

    List<Job> completedJobs = new ArrayList<>();
    int clockTime = 0;
    for (int i = 0; i < N; ) {

      Job j = jobs[i]; // offer the first job into the minHeap
      clockTime = j.arrivalTime; // start the clock from the arrival of the first job
      minHeap.offer(j);
      i++; // move to the next job

	  //MinHeap processing the jobs:
      while (!minHeap.isEmpty()) {
        Job finishedJob = minHeap.poll();
        completedJobs.add(finishedJob);
        clockTime += finishedJob.burst;
        finishedJob.computeWaitAndTurnAround(clockTime);

        // add all the jobs into the minHeap that have arrived untill Now (endT)
        while (i < N && jobs[i].arrivalTime <= clockTime) {
          minHeap.offer(jobs[i]);
          i++;
        }
      }

    }
//Output formatting for the SJF process execution Algorithm
    double sumWaitTime = 0.0;
    double sumCompletionTime = 0.0;
    double sumTurnAroundTime = 0.0;
	System.out.println("Jobs After Execution: ");
	System.out.println("===================================================");
	for (Job j: completedJobs) {
		sumWaitTime += j.waitTime;
		sumCompletionTime += j.completionTime;
		sumTurnAroundTime += j.turnAroundTime;
		System.out.println(j);
    }

	double avgWaitTime = sumWaitTime / N;
	double avgCompletionTime = sumCompletionTime / N;
	double avgTurnAroundTime = sumTurnAroundTime / N;

	System.out.println("---------------------------------------------------");
	System.out.println("Total Jobs : " + N);
	System.out.println("Avg Wait Time : " + avgWaitTime);
	System.out.println("Avg Completion Time : " + avgCompletionTime);
	System.out.println("Avg TurnAround Time : " + avgTurnAroundTime);
	System.out.println("===================================================");
}
}