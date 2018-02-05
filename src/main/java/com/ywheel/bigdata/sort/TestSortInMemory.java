package com.ywheel.bigdata.sort;

import com.ywheel.bigdata.sort.prepare.DataGenerator;
import com.ywheel.bigdata.sort.algorithm.*;

import java.io.*;
import java.util.*;

/**
 * Test sort with enough memory.
 * Created by ywheel on 2018/1/31.
 */
public class TestSortInMemory {

    /**
     * Helper method. Load the data from file.
     * @param fileName
     * @param length
     * @return
     * @throws IOException
     */
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

    public static void main(String[] args) throws IOException {
        int length = 100000000;
        String fileName = "/Users/ywheel/workplace/bigdatasort/test-files/randomnumber_" + length + ".txt";
        DataGenerator dataGenerator = new DataGenerator(fileName, length);
        long startTime = System.currentTimeMillis();
        dataGenerator.generateAndOutput();;
        long endTime = System.currentTimeMillis();
        System.out.println("Total use " + (endTime - startTime)
                + "(ms) to generate " + length + " random number.");

        List<Sort> sortList = new ArrayList<Sort>();
        //sortList.add(new BubbleSort());
        //sortList.add(new HeapSort());
        //sortList.add(new InsertSort());
        sortList.add(new MergeSort());
        sortList.add(new QuickSort());
        //sortList.add(new SelectSort());

        // Test running time
        for (Sort sort : sortList) {
            int[] data = loadDataFromFile(fileName, length); // data should be same and random every time
            startTime = System.currentTimeMillis();
            sort.sort(data);
            endTime = System.currentTimeMillis();
            System.out.println(sort.name() + ": Total use " + (endTime - startTime)
                    + "(ms) to sort " + length + " random number.");
        }
    }
}
