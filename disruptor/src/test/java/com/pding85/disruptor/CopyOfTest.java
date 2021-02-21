package com.pding85.disruptor;

import java.util.Arrays;

public class CopyOfTest {

    public static void main(String[] args) {
        Integer[] array = new Integer[]{2,3};
        Integer[] copy = Arrays.copyOf(array, 4);
        System.out.println(copy);
    }
}
