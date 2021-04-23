package com.milley.besttimetobuyandsellstockwithcooldown;

public class Solution {
    public static int maxProfit(int[] prices) {
        int max_sell = 0, res = 0, res_prev = 0;
        for(int start = prices.length-1; start>=0;start--){
            max_sell = Math.max(max_sell, prices[start]+res_prev);
            res_prev = res;
            res = Math.max(res, max_sell - prices[start]);
        }
        return res;
    }

    public static void main(String[] args) {
        //int[] prices = {1,2,3,0,2};
        //int[] prices = {1};
        int[] prices = {1, 2, 4};
        System.out.println(maxProfit(prices));
    }
}
