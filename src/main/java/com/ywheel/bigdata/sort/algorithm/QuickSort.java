package com.ywheel.bigdata.sort.algorithm;

import java.util.Arrays;

/**
 * Created by ywheel on 2018/1/31.
 */
public class QuickSort implements Sort {

    @Override
    public int[] sort(int[] data) {
        if (data == null) {
            return data;
        }
        // just use Arrays.sort
        Arrays.sort(data);
        return data;
    }

    @Override
    public String name() {
        return "QuickSort";
    }
}
