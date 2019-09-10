package com.milley.majorityelement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> majorityMap = new HashMap<>(nums.length);
        for (int num : nums) {
            Integer val = majorityMap.get(num);
            majorityMap.put(num, val == null ? 1 : val.intValue() + 1);
        }

        for (Map.Entry<Integer, Integer> entry : majorityMap.entrySet()) {
            if (entry.getValue() > nums.length / 2) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public int majorityElementBySort(int[] nums) {
        Arrays.sort(nums);
        int eqLen = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                eqLen++;
            } else {
                if (eqLen > nums.length / 2 - 1) {
                    return nums[i];
                } else {
                    eqLen = 0;
                }
            }
        }
        return nums[eqLen];
    }

    public int majorityElementByMagic(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.majorityElement(new int[]{3, 2, 3}));
        System.out.println(solution.majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}));
        System.out.println(solution.majorityElementBySort(new int[]{3, 2, 3}));
        System.out.println(solution.majorityElementBySort(new int[]{-1, 1, 1, 1, 2, 1}));
        System.out.println(solution.majorityElementByMagic(new int[]{3, 2, 3}));
        System.out.println(solution.majorityElementByMagic(new int[]{-1, 1, 1, 1, 2, 1}));
    }
}
