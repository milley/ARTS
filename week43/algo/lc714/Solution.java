package com.milley.besttimetobuyandsellstockwithtransactionfee;

public class Solution {
    public static int maxProfit(int[] prices, int fee) {
        int N = prices.length;
        if (N == 0 || prices == null) {
            return 0;
        }

        int[][] profit = new int[N][2];
        profit[0][0] = 0;
        profit[0][1] = -prices[0];

        for (int i = 1; i < N; ++i) {
            profit[i][0] = Math.max(profit[i - 1][1] + prices[i] - fee,
                    profit[i - 1][0]);
            profit[i][1] = Math.max(profit[i - 1][0] - prices[i], profit[i - 1][1]);
        }

        int max = Integer.MIN_VALUE;
        max = Math.max(max, profit[N - 1][0]);

        return max;
    }

    public static void main(String[] args) {
        int[] prices = {1, 3, 2, 8, 4, 9};
        System.out.println(maxProfit(prices, 2));
//        int[] prices = {1,3,7,5,10,3};
//        System.out.println(maxProfit(prices, 3));
    }
}
