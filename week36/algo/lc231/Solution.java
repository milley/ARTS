package com.milley.poweroftwo;

/**
 * @author milley
 */
public class Solution {
    public static boolean isPowerOfTwo(int n) {
        if ((n & (n - 1)) == 0 && (n != 0) && (n != Integer.MIN_VALUE)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isPowerOfTwo(1));
        System.out.println(isPowerOfTwo(16));
        System.out.println(isPowerOfTwo(218));
        System.out.println(isPowerOfTwo(-2147483648));
    }
}
