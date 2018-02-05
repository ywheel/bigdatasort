package com.ywheel.bigdata.sort.algorithm;

/**
 * Created by ywheel on 2018/1/31.
 */
public class SelectSort implements Sort {

    @Override
    public int[] sort(int[] data) {
        if (data == null) {
            return data;
        }

        int size = data.length;
        int temp = 0 ;
        for(int i = 0 ; i < size ; i++) {
            int k = i;
            for(int j = size -1 ; j > i ; j--) {
                if(data[j] < data[k]) {
                    k = j;
                }
            }
            temp = data[i];
            data[i] = data[k];
            data[k] = temp;
        }
        return data;
    }

    @Override
    public String name() {
        return "SelectSort";
    }
}
