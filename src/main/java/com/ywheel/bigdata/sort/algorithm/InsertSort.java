package com.ywheel.bigdata.sort.algorithm;

/**
 * Created by ywheel on 2018/1/31.
 */
public class InsertSort implements Sort {

    @Override
    public int[] sort(int[] data) {
        if (data == null) {
            return data;
        }
        int size = data.length;
        int temp = 0 ;
        int j =  0;

        for(int i = 0 ; i < size ; i++) {
            temp = data[i];
            for(j = i ; j > 0 && temp < data[j-1] ; j --) {
                data[j] = data[j-1];
            }
            data[j] = temp;
        }
        return data;
    }

    @Override
    public String name() {
        return "InsertSort";
    }
}
