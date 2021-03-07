package com.milley.besttimetobuyandsellstockiii;


public class Solution {
    public static int maxProfit(int[] prices) {
		// Solution1
        /*int N = prices.length;
        if (N == 0 || prices == null) {
            return 0;
        }

        int K = 2;
        int[][][] profit = new int[N][2][K + 1];
        for (int i = 0; i <= K; ++i) {
            profit[0][0][i] = 0;
            profit[0][1][i] = -prices[0];
        }

        for (int i = 1; i < N; ++i) {
            for (int j = 0; j <= K; ++j) {
                profit[i][0][j] = j != 0 ? Math.max(profit[i - 1][1][j - 1] + prices[i],
                        profit[i - 1][0][j]) : profit[i - 1][0][j];
                profit[i][1][j] = Math.max(profit[i - 1][0][j] - prices[i], profit[i - 1][1][j]);
            }
        }

        int max = Integer.MIN_VALUE;
        for (int i = 0; i <= K; ++i) {
            max = Math.max(max, profit[N - 1][0][i]);
        }

        return max;*/

		// Solution2
        int N = prices.length;
        int K = 3;
        int X = 2;

        int[][][] profit = new int[N][K][X];

        profit[0][0][0] = 0;
        profit[0][0][1] = -prices[0];

        profit[0][1][0] = 0;;
        profit[0][1][1] = -prices[0];

        profit[0][2][0] = 0;
        profit[0][2][1] = -prices[0];

        for (int i = 1; i < N; ++i) {
            profit[i][0][0] = profit[i - 1][0][0];
            profit[i][0][1] = Math.max(profit[i - 1][0][0] - prices[i], profit[i - 1][0][1]);

            profit[i][1][0] = Math.max(profit[i - 1][1][0], profit[i - 1][0][1] + prices[i]);
            profit[i][1][1] = Math.max(profit[i - 1][1][1], profit[i - 1][1][0] - prices[i]);


            profit[i][2][0] = Math.max(profit[i - 1][2][0], profit[i - 1][1][1] + prices[i]);
            profit[i][2][1] = Math.max(profit[i - 1][2][1], profit[i - 1][2][0] - prices[i]);
        }

        return Math.max(profit[N - 1][0][0], Math.max(profit[N - 1][1][0], profit[N - 1][2][0]));
    }

    public static void main(String[] args) {
        //int[] prices = {3, 3, 5, 0, 0, 3, 1, 4};
        //int[] prices = {1,2,3,4,5};
        //int[] prices = {7,6,4,3,1};
        int[] prices = {1};
        System.out.println(maxProfit(prices));
    }
}
