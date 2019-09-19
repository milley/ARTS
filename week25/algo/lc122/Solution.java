package com.milley.besttimetobuyandsellstockii;

/**
 * @author milley
 */
public class Solution {
    public static int maxProfit(int[] prices) {
        int sum = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                sum += prices[i] - prices[i - 1];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        //int[] prices = new int[]{7, 1, 5, 3, 6, 4};
        int[] prices = new int[]{1, 2, 3, 4, 5};
        System.out.println(maxProfit(prices));
    }
}
