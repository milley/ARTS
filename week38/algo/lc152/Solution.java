package com.milley.maximumproductsubarray;

/**
 * @author milley
 */
public class Solution {
    public static int maxProduct(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        int[][] dp = new int[2][2];
        dp[0][0] = dp[0][1] = nums[0];
        int res = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int x = i % 2;
            int y = (i - 1) % 2;
            dp[x][0] = Math.max(dp[y][0] * nums[i], Math.max(dp[y][1] * nums[i], nums[i]));
            dp[x][1] = Math.min(dp[y][0] * nums[i], Math.min(dp[y][1] * nums[i], nums[i]));
            res = Math.max(res, dp[x][0]);
        }
        return res;
    }

    public static void main(String[] args) {
        int[] input = new int[]{2, 3, -2, 4};
        System.out.println(maxProduct(input));
    }
}
