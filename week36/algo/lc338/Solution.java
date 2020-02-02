package com.milley.countbits;

/**
 * @author milley
 */
public class Solution {
    public static int[] countBits(int num) {
        int[] array = new int[num + 1];
        for (int i = 1; i <= num; i++) {
            array[i] += array[i & (i - 1)] + 1;
        }
        return array;
    }

    private static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        printArray(countBits(2));
        printArray(countBits(5));
    }
}
