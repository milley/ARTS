package com.milley.besttimetobuyandsellstock;

public class Solution {
    public static int maxProfit(int[] prices) {
        if (prices.length <= 1) {
            return 0;
        }

        int min = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int val : prices) {
            if (val < min) {
                min = val;
            } else if (val - min > maxProfit) {
                maxProfit = val - min;
            }
        }

        return maxProfit;
    }

    public static void main(String[] args) {
        int prices[] = {7, 1, 5, 3, 6, 4};
        System.out.println(maxProfit(prices));
    }
}
