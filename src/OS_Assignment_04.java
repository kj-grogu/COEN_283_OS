

import java.io.*;
import java.util.*;

public class OS_Assignment_04 {
  /*
  * Q1. (40 Points)
Write  a  program  that  simulates  a  paging  system  using  the  aging  algorithm.  The  number  of  page
frames is a parameter. The sequence of page references should be read from a file. For a given input
file, plot the number of page faults per 1000 memory references as a function of the number of page
frames available.
  * */

  private static final String fileName = "pageSequence.txt";

  public static class Memory {
    int[][] pageFrameTable;
    int pageCount;
    int maxPages;

    int pageReferedCount;

    int pageFaultCount;


    int totalPageFaultCount;

    Map<Integer, Integer> pageMap;

    Memory(int frames) {
      pageFrameTable = new int[frames][2];

      pageCount = pageFaultCount = pageReferedCount = 0;
      maxPages = frames;
      pageMap = new HashMap<>();

      for (int i = 0; i < pageFrameTable.length; i++) {
        pageFrameTable[i][0] = 0; // no set bits
        pageFrameTable[i][1] = -1; // default page is -1
      }
    }

    void referencePage(int page) {
      pageReferedCount++;
//      System.out.println("Page refered : " + page);
      int index = -1;
      if (pageMap.containsKey(page)) {
        index = pageMap.get(page);
//        System.out.println("No Page Fault  for page : " + page);
      } else {
        pageFaultCount++;
//        System.out.println("Page Fault count: " + pageFaultCount + " for page : " + page);
        if (pageCount < maxPages) {
          index = pageCount;
          pageCount++;
        } else {
          int min = Integer.MAX_VALUE;
          for (int i = 0; i < pageFrameTable.length; i++) {
            int b = pageFrameTable[i][0];
            if (b < min) {
              min = b;
              index = i;
            }
          }

//          System.out.println("Dropping page : " + pageFrameTable[index][1] + " as its the lowest : " + pageFrameTable[index][0]);
          pageFrameTable[index][0] = 0; // clear the bits of prev page
          pageMap.remove(pageFrameTable[index][1]); // remove the prev page from map
        }
      }

      pageMap.put(page, index);
      pageFrameTable[index][1] = page;
      pageFrameTable[index][0] = ((pageFrameTable[index][0]) | 256 & (0xff << 1));
      shiftAll();
//      printPageTable();
      if (pageReferedCount % 1000 == 0) {
        System.out.println(" PageFaultCount for these 1000 page references are : " + pageFaultCount);
        totalPageFaultCount += pageFaultCount;
        pageFaultCount = 0;
      }
    }

    private void shiftAll() {
      for (int i = 0; i < pageFrameTable.length; i++) {
        pageFrameTable[i][0] = (pageFrameTable[i][0] >> 1); // shift right
        pageFrameTable[i][0] = ((pageFrameTable[i][0]) & 0xff); // 1000000, left most bit is 1
      }
    }

    private void printPageTable() {
      String empty = "00000000";
      StringBuilder sbr = new StringBuilder();
      for (int i = 0; i < pageFrameTable.length; i++) {
        if (pageFrameTable[i][1] == -1) continue;
        sbr.append(Integer.toBinaryString(pageFrameTable[i][0]));
        while (sbr.length() < 8) {
          sbr.insert(0, "0");
        }
        System.out.println(sbr.toString() + " : " + pageFrameTable[i][1]);
        sbr.setLength(0);
      }
    }
  }

  private static List<Integer> getRandomNumbers(int min, int max, int size) {
    List<Integer> nos = new LinkedList<>();
    int n;
    for (int i = 0; i < size; i++) {
      n = (int) (Math.random() * (max - min)) + min;
      nos.add(n);
    }
    return nos;
  }

  private static void writeSequenceToFile(List<Integer> nos) {
    StringBuilder sbr = new StringBuilder();
    sbr.append(nos.get(0));
    for (int i = 1; i < nos.size(); i++)
      sbr.append("," + nos.get(i));
    try {
      File file = new File(fileName);
      if (file.createNewFile()) {
        System.out.println("File created: " + file.getName());
      } else {
        System.out.println("File already exists.");
      }

      FileWriter writer = new FileWriter(file);
      writer.write(sbr.toString());
      writer.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  private static List<Integer> readSequenceFromFile() {
    List<Integer> input = new LinkedList<>();
    try {
      File file = new File(fileName);
      if (file.createNewFile()) {
        System.out.println("File created: " + file.getName());
      } else {
        System.out.println("File already exists.");
      }

      BufferedReader br = new BufferedReader(new FileReader(file));

      try {
        StringBuilder sbr = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
          sbr.append(line);
        }

        String[] arr = sbr.toString().split(",");
        for (String i : arr) {
          input.add(Integer.parseInt(i));
        }

      } finally {
        br.close();
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    //System.out.println("Read back");
    //System.out.println(input);
    return input;
  }

  private static String getBitString(int b) {
    StringBuilder sbr = new StringBuilder();
    int last = 0;
    b = b & 0xff;
    while (b != 0) {
      last = b & 1;
      sbr.insert(0, last);
      b = (byte) (b >>> 1);
    }

    return sbr.toString();
  }

  public static void main(String[] args) {
    int min = 0;
    int max = 1000;
    int seqLen = 10000;
    List<Integer> nos = getRandomNumbers(min, max, seqLen);
    writeSequenceToFile(nos);

//    System.out.println("Enter the page frame size");
//    Scanner in = new Scanner(System.in);
//    int frameSize = in.nextInt();

    int frameSize = 100;
    Memory mem = new Memory(frameSize);

    List<Integer> pageReferences = readSequenceFromFile();

    for (int p : pageReferences) {
      mem.referencePage(p);
    }

    System.out.println("Total Page Faults : " + mem.totalPageFaultCount);
  }
}
