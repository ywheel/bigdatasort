package com.ywheel.bigdata.sort.algorithm;

/**
 * Created by ywheel on 2018/1/31.
 */
public class BubbleSort implements Sort {

    @Override
    public int[] sort(int[] data) {
        if (data == null) {
            return data;
        }

        int temp = 0;
        int size = data.length;
        for(int i = 0 ; i < size-1; i ++) {
            for(int j = 0 ;j < size-1-i ; j++) {
                if(data[j] > data[j+1]) {
                    temp = data[j];
                    data[j] = data[j+1];
                    data[j+1] = temp;
                }
            }
        }
        return data;
    }

    @Override
    public String name() {
        return "BubbleSort";
    }
}
