package com.milley.longestincreasingsubsequence;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Solution {

    public int lengthOfLIS(int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }

        int[] dp = new int[len];
        for (int i = 0; i < len; i++) {
            dp[i] = 1;
        }

        int res = 1;
        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < i; ++j) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            res = Math.max(res, dp[i]);
        }

        return res;
    }


    public int lengthOfLIS2(int[] nums) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; ++i) {
            int index = Collections.binarySearch(new ArrayList<>(res), nums[i]);
            if (index < 0) {
                boolean flag = false;
                for (int k = 0; k < res.size(); ++k) {
                    if (res.get(k) > nums[i]) {
                        res.set(k, nums[i]);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    res.add(nums[i]);
                }
            } else {
                res.set(index, nums[i]);
            }
        }

        return res.size();
    }

    public static void main(String[] args) {
//        int[] a = {10,9,2,5,3,7,101,18};
        int[] a = {0,1,0,3,2,3};
//        int[] a = {10,9,2,5,3,7,101,18};
//        int[] a = {7,7,7,7,7,7,7};

//        System.out.println((new Solution()).lengthOfLIS(a));
        System.out.println((new Solution()).lengthOfLIS2(a));
    }
}
