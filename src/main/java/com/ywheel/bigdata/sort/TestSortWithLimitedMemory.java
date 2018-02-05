package com.ywheel.bigdata.sort;

import com.ywheel.bigdata.sort.algorithm.*;

import java.io.*;
import java.util.*;

/**
 * Test sort with limited memory. e.g. 100MB
 * Created by ywheel on 2018/1/31.
 */
public class TestSortWithLimitedMemory {

    public static void splitFile(String input, String outputPath, int fileNumber,
                                 int totalCount, int method) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(input)));
        BufferedWriter[] writer = new BufferedWriter[fileNumber];
        for (int i=0; i < fileNumber; i++) {
            writer[i] = new BufferedWriter(new FileWriter(new File(
                    outputPath + "/_" + String.valueOf(i))));
        }
        int border = totalCount / fileNumber;
        String line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            count++;
            int number = Integer.parseInt(line);
            int fileIndex = getFileIndex(number, count, border, method);
            writer[fileIndex].write(line);
            writer[fileIndex].newLine();
        }
        reader.close();
        for (BufferedWriter w : writer) {
            w.close();
        }
    }

    public static int getFileIndex(int number, int count, int border, int method) {
        if (method == 1) {
            return (count - 1) / border;
        } else /* method == 2*/ {
            return (number - 1) / border;
        }
    }

    public static int[] loadDataFromFile(String fileName, int length) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line = null;
        int[] data = new int[length];
        for (int i=0; i < length; i++) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            data[i] = Integer.parseInt(line);
        }
        reader.close();
        return data;
    }

    public static void saveDataToFile(String fileName, int[] data) throws IOException {
        if (fileName == null || data == null) {
            return;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)));
        for (int i = 0; i < data.length; i++) {
            writer.write(String.valueOf(data[i]));
            writer.newLine();
        }
        writer.close();
    }

    /**
     *  method 1: split file according to the row number
     *  1. split file
     *  2. sort each file
     *  3. read them at the same time, select the min number to write out
     * @param fileName
     * @param outputPath
     * @param fileNumber
     * @param totalCount
     * @param countPerFile
     * @throws IOException
     */
    public static void method_1(String fileName, String outputPath, int fileNumber,
                               int totalCount, int countPerFile) throws IOException {
        long startTime = System.currentTimeMillis();
        splitFile(fileName, outputPath, fileNumber, totalCount, 1);
        Sort sorter = new QuickSort();
        for (int i = 0; i < fileNumber; i++) {
            // sort each file
            int[] data = loadDataFromFile(outputPath + "/_" + String.valueOf(i), countPerFile);
            sorter.sort(data);
            saveDataToFile(outputPath + "/1_sorted_" + String.valueOf(i), data);
        }
        // merge them to one
        TreeMap<Integer, BufferedReader> map = new TreeMap<Integer, BufferedReader>();
        // 1. read one line per file to init map
        BufferedReader[] reader = new BufferedReader[fileNumber];
        for (int i = 0; i < fileNumber; i++) {
            reader[i] = new BufferedReader(new FileReader(
                    new File(outputPath + "/1_sorted_" + String.valueOf(i))));
            String line = reader[i].readLine();
            if (line != null) {
                map.put(Integer.parseInt(line), reader[i]);
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputPath + "/1_final_sorted")));
        while (!map.isEmpty()) {
            // peek the min value, and put new one
            Map.Entry<Integer, BufferedReader> firstEntry = map.pollFirstEntry();
            writer.write(firstEntry.getKey().toString());
            writer.newLine();
            String line = firstEntry.getValue().readLine();
            if (line != null) {
                map.put(Integer.parseInt(line), firstEntry.getValue());
            }
        }
        writer.close();
        for (BufferedReader r : reader) {
            r.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Method 1: Total use " + (endTime - startTime)
                + "(ms) to sort lots of random numbers.");
    }

    /**
     *  method 2: split file according to the number value
     *  1. split file
     *  2. sort each file
     *  3. read them on by one and write out
     * @param fileName
     * @param outputPath
     * @param fileNumber
     * @param totalCount
     * @param countPerFile
     * @throws IOException
     */
    public static void method_2(String fileName, String outputPath, int fileNumber,
                               int totalCount, int countPerFile) throws IOException {
        long startTime = System.currentTimeMillis();
        splitFile(fileName, outputPath, fileNumber, totalCount, 2);
        Sort sorter = new QuickSort();
        for (int i = 0; i < fileNumber; i++) {
            // sort each file
            int[] data = loadDataFromFile(outputPath + "/_" + String.valueOf(i), countPerFile);
            sorter.sort(data);
            saveDataToFile(outputPath + "/2_sorted_" + String.valueOf(i), data);
        }
        // merge them to one
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputPath + "/2_final_sorted")));
        for (int i = 0; i < fileNumber; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(
                    new File(outputPath + "/2_sorted_" + String.valueOf(i))));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            reader.close();
        }
        writer.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Method 2: Total use " + (endTime - startTime)
                + "(ms) to sort lots of random numbers.");
    }

    public static void main(String[] args) throws IOException {
        int totalCount = 100000000;
        int fileNumber = 10; // just for testing, totalCount % fileNumber should be 0
        int countPerFile = totalCount / fileNumber;
        // just assume the file is already there
        String fileName = "/Users/ywheel/workplace/bigdatasort/test-files/randomnumber_" + totalCount + ".txt";
        String outputPath = "/Users/ywheel/workplace/bigdatasort/test-files/";

        method_1(fileName, outputPath, fileNumber, totalCount, countPerFile);
        method_2(fileName, outputPath, fileNumber, totalCount, countPerFile);

    }
}
