package com.milley.numberof1bits;

/**
 * @author milley
 */
public class Solution {
    public static int hammingWeight(int n) {
        int w = 0;
        while (n != 0) {
            n = n & (n - 1);
            w++;
        }
        return w;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt("00000000000000000000000000001011", 2);
        int n1 = Integer.parseInt("00000000000000000000000010000000", 2);
        //int n2 = Integer.parseInt("11111111111111111111111111111101", 2);
        System.out.println(hammingWeight(n));
        System.out.println(hammingWeight(n1));
        //System.out.println(hammingWeight(n2));
        // -10000000000000000000000000000000
        System.out.println(Integer.toString(Integer.MIN_VALUE, 2));
    }
}
