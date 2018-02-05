package com.ywheel.bigdata.sort.algorithm;

/**
 * Created by ywheel on 2018/1/31.
 */
public class MergeSort implements Sort {

    @Override
    public int[] sort(int[] data) {
        if (data == null) {
            return data;
        }
        return doSort(data, 0, data.length - 1);
    }

    private int[] doSort(int[] data, int low, int high) {
        int mid = (low + high) / 2;
        if (low < high) {
            doSort(data, low, mid);
            doSort(data, mid + 1, high);
            merge(data, low, mid, high);
        }
        return data;
    }

    private void merge(int[] data, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= high) {
            if (data[i] < data[j]) {
                temp[k++] = data[i++];
            } else {
                temp[k++] = data[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = data[i++];
        }

        while (j <= high) {
            temp[k++] = data[j++];
        }

        for (int k2 = 0; k2 < temp.length; k2++) {
            data[k2 + low] = temp[k2];
        }
    }

    @Override
    public String name() {
        return "MergeSort";
    }
}
