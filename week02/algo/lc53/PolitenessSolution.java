package com.milley.MaximumSubarray;

public class PolitenessSolution {
    /**
     * DP, O(n) Time, O(1) Space
     * @param nums
     * @return max value
     */
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int maxSum = nums[0];
        int curMax = nums[0];

        for (int i = 1; i < n; i++) {
            curMax = Math.max(curMax + nums[i], nums[i]);
            maxSum = Math.max(curMax, maxSum);
        }
        return maxSum;
    }

    /**
     * DP, O(n) Time, O(n) Space
     * @param nums
     * @return max value
     */
    public int maxSubArrayB(int[] nums) {
        int n = nums.length;
        if (nums == null || n == 0) {
            return 0;
        }
        int[] s = new int[n];
        s[0] = nums[0];
        int max = nums[0];
        for (int i = 1; i < n; i++) {
            s[i] = s[i - 1] > 0 ? (nums[i] + s[i - 1]) : nums[i];
            max = Math.max(max, s[i]);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        PolitenessSolution politenessSolution = new PolitenessSolution();
        System.out.println(politenessSolution.maxSubArray(nums));
        System.out.println(politenessSolution.maxSubArrayB(nums));
    }
}
