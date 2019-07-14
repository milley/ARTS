package com.milley.slidingwindowmaximum;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Solution {
    private PriorityQueue<Integer> queue;

    public int[] maxSlidingWindow(int[] nums, int k) {
        List<Integer> arr = new ArrayList<>();

        if (nums.length == 0) {
            return new int[]{};
        }

        queue = new PriorityQueue<>(k, (lhs, rhs) -> rhs - lhs);

        if (nums.length <= k) {
            for (int i = 0; i < nums.length; i++) {
                queue.add(nums[i]);
            }
            arr.add(queue.peek());
        } else {
            for (int i = 0; i <= nums.length - k; i++) {
                queue.clear();
                for (int j = 0; j < k; j++) {
                    queue.add(nums[i + j]);
                }
                arr.add(queue.peek());
            }
        }


        return arr.stream().mapToInt(Integer::valueOf).toArray();
    }

    public static void main(String[] args) {
        //int[] arr = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
        int[] arr = new int[]{};
        int[] res = new Solution().maxSlidingWindow(arr, 3);
        for (int i : res) {
            System.out.println(i);
        }
    }
}
