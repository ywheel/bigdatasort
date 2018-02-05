package com.ywheel.bigdata.sort.algorithm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ywheel on 2018/1/31.
 */
public class MergeSortTest {

    private MergeSort sorter;

    @Before
    public void setup() {
        sorter = new MergeSort();
    }

    @Test
    public void testSort() {
        int[] data = {2,6,4,3,8,12,9,0};
        int[] expect = {0,2,3,4,6,8,9,12};
        int[] result = sorter.sort(data);
        Assert.assertArrayEquals(expect, result);
    }
}
