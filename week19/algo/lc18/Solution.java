package com.milley.foursum;

import java.util.List;

/**
 * @author milley
 */
public class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        int len = nums.length;

        Arrays.sort(nums);
        for (int a = 0; a < len - 3; a++) {
            if (nums[a] << 2 > target) {
                break;
            }
            for (int d = len - 1; d > a + 2; d--) {
                if (nums[d] << 2 < target) {
                    return result;
                }
                int rem = target - nums[a] - nums[d];
                int b = a + 1, c = d - 1;
                while (b < c) {
                    int sum = nums[b] + nums[c];
                    if (sum > rem) {
                        --c;
                    } else if (sum < rem) {
                        ++b;
                    } else {
                        result.add(Arrays.asList(nums[a], nums[b], nums[c], nums[d]));
                        while (++b <= c && nums[b] == nums[b - 1]) {
                            continue;
                        }
                        while (--c >= b && nums[c] == nums[c + 1]) {
                            continue;
                        }
                    }

                }
                while (d >= 1 && nums[d] == nums[d - 1]) {
                    --d;
                }
            }
            while (a < len - 1 && nums[a] == nums[a + 1]) {
                ++a;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        //int[] nums = new int[]{1, 0, -1, 0, -2, 2};
        //int[] nums = new int[]{1, 1, 2, 0, -1, 0, -2, 2};
        //int[] nums = new int[]{0, 0, 0, 0};
        //int[] nums = new int[]{-1, 0, 1, 2, -1, -4};        int target = -1;
//        int[] nums = new int[]{-4, -3, -2, -1, 0, 0, 1, 2, 3, 4};
//        int target = 0;
        int[] nums = new int[]{-1, -5, -5, -3, 2, 5, 0, 4};        int target = -7;
        System.out.println(new Solution().fourSum(nums, target));
    }
}
