package com.milley.ReverseInteger;

public class Solution {
    public static int reverse(int x) {
       long result = 0;
       while (x != 0) {
           int remainder = x % 10;
           result = result * 10 + remainder;
           x /= 10;
           if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
               return 0;
           }
       }
       return (int)result;
    }

    public static void main(String[] args) {
        System.out.println(reverse(1534236469));
        System.out.println(reverse(-2147483648));
        System.out.println(String.valueOf(Integer.MIN_VALUE).compareTo("-8463847412"));
    }
}
