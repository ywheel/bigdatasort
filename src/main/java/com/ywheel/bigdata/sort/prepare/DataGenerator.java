package com.ywheel.bigdata.sort.prepare;


import java.io.IOException;
import java.util.Random;
import java.io.*;

/**
 * Created by ywheel on 2018/1/31.
 */
public class DataGenerator {

    private int length = 1000; // 1000 by default
    private int[] data;
    private String outputFileName;

    public DataGenerator(String outputFileName, int length) {
        this.outputFileName = outputFileName;
        this.length = length;
    }

    public DataGenerator(int length) {
        this.length = length;
    }

    public void generateAndOutput() throws IOException {
        if (this.outputFileName == null) {
            return;
        }
        int[] data = generateData();
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.outputFileName)));
        for (int i = 0; i < length; i++) {
            writer.write(String.valueOf(data[i]));
            writer.newLine();
        }
        writer.close();
    }

    public int[] generateData() {
        if (data == null) {
            data = new int[length];
            for (int i = 0; i < length; i++) {
                data[i] = i + 1;
            }
        }

        Random random = new Random();
        // random data
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(length);
            // swap
            int temp = data[index];
            data[index] = data[i];
            data[i] = temp;
        }

        return data;
    }
}
