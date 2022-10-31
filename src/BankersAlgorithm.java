import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
Program a simulation of the banker’s algorithm. 
Your program should cycle through each of the bank clients asking for a request and evaluating whether it is safe or unsafe. 
Output a log of requests and decisions to a file.
 */

public class BankersAlgorithm {

  // N = number of processess;
  static int PROCESS;

  // R = Types  of Resources;
  static int RESOURCES;

  // Logger
  static Logger logger;

  public static boolean calculateStateOfSystem(int[] freedUpResources,
                                               int[][] currentAllocationByProcess,
                                               int[][] maxRequiredConsumptionByProcess) {

    int[][] requiredResourcesByProcess = new int[PROCESS][RESOURCES];

    for (int pi = 0; pi < PROCESS; pi++) {
      requiredResourcesByProcess[pi] = getRequiredResourcesForProcess(pi, currentAllocationByProcess, maxRequiredConsumptionByProcess);
    }

    boolean[] finished = new boolean[PROCESS];
    Arrays.fill(finished, false);
    List<Integer> executionOrder = new LinkedList<>();

    int finishedCount = 0;

    while (finishedCount < PROCESS) {
      boolean executable = false;
      logger.info("Iteration : " + finishedCount);
      for (int pi = 0; pi < PROCESS; pi++) {
        if (finished[pi]) continue;

        boolean resourcesSatisfied = true;

        for (int ri = 0; ri < RESOURCES && resourcesSatisfied; ri++) {
          resourcesSatisfied = requiredResourcesByProcess[pi][ri] <= freedUpResources[ri];
        }

        if (!resourcesSatisfied)
          continue;

        // As the current process's resources requirement can be satisfied, the process will finish and free up the resources
        for (int ri = 0; ri < RESOURCES; ri++) {
          freedUpResources[ri] += currentAllocationByProcess[pi][ri];
        }

        executionOrder.add(pi);
        finished[pi] = true;
        executable = true;
        finishedCount++;
        logger.info("Freed up process : " + pi);
        logger.info("Available Resources : " + Arrays.toString(freedUpResources));
      }

      if (!executable) {
        logger.info("No further execution of process's is possible the system is in unsafe state");
        return false;
      }
    }


    logger.info("The system is in safe state, with below execution order");
    logger.info("ExecutionOrder " + executionOrder);
    return true;
  }

  private static int[] getRequiredResourcesForProcess(int pi,
                                                      int[][] currentAllocationByProcess,
                                                      int[][] maxAllowedConsumptionByProcess) {

    int[] requiredResources = new int[RESOURCES];

    for (int ri = 0; ri < RESOURCES; ri++) {
      requiredResources[ri] = maxAllowedConsumptionByProcess[pi][ri] - currentAllocationByProcess[pi][ri];
    }

    return requiredResources;
  }


  private static void SafeStateTest() {
    PROCESS = 5;
    RESOURCES = 3;
    logger.info("================================================");
    logger.info("SafeStateTest");
    logger.info("PROCESS : " + PROCESS);
    logger.info("RESOURCES : " + RESOURCES);

    int[] availableResources = new int[]{3, 3, 2};
    logger.info("available Resources to CPU (Banker): " + Arrays.toString(availableResources));

    int[][] maxRequiredConsumptionByProcess = new int[][]{
        {17, 15, 13},
        {19, 10, 12},
        {13, 12, 12},
        {12, 12, 12},
        {14, 13, 13}
    };

    for (int pi = 0; pi < PROCESS; pi++) {
      logger.info("maxRequiredConsumptionByProcess " + pi + " : " + Arrays.toString(maxRequiredConsumptionByProcess[pi]));
    }

    int[][] currentAllocationByProcess = new int[][]{
        {12, 10, 10},
        {10, 11, 10},
        {13, 10, 12},
        {12, 11, 11},
        {10, 10, 12}
    };

    for (int pi = 0; pi < PROCESS; pi++) {
      logger.info("currentAllocationByProcess " + pi + " : " + Arrays.toString(currentAllocationByProcess[pi]));
    }

    boolean isSafeState = calculateStateOfSystem(availableResources, currentAllocationByProcess, maxRequiredConsumptionByProcess);
    logger.info("System state :  " + (isSafeState ? "safe" : "unsafe"));
    logger.info("================================================");
  }

  private static void UnsafeStateTest() {
    PROCESS = 5;
    RESOURCES = 1;

    int[] availableResources = new int[]{2};

    int[][] maxRequiredConsumptionByProcess = new int[][]{
        {15},
        {17},
        {19},
        {15},
        {14}
    };

    int[][] currentAllocationByProcess = new int[][]{
        {12},
        {10},
        {13},
        {12},
        {10}
    };

    boolean isSafeState = calculateStateOfSystem(availableResources, currentAllocationByProcess, maxRequiredConsumptionByProcess);
    logger.info("System state :  " + (isSafeState ? "safe" : "unsafe"));
  }

  public static void main(String[] args) throws Exception {

    String path = "/Users/bhartiprakash/Documents/Santa Clara/Courses/Quarter I/COEN_283_OS/COEN283_Assignment_PQ3/Bankers.log";
    logger = Logger.getLogger("BankersLog");
    logger.info("Log file created at : " + path);
    Logger logger = Logger.getLogger("BankersLog");
    FileHandler fileHandler = new FileHandler(path);
    logger.addHandler(fileHandler);
    fileHandler.setFormatter(new SimpleFormatter());
    UnsafeStateTest();
    SafeStateTest();
  }
}