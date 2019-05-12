package com.milley.KthLargestElementInAStream;

import java.util.PriorityQueue;

public class KthLargest {
    private PriorityQueue<Integer> queue;
    private int kth;

    public KthLargest(int k, int[] nums) {
        this.kth = k;
        queue = new PriorityQueue<>(k);
        for (int i = 0; i < nums.length; i++) {
            add(nums[i]);
        }
    }
    public int add(int val) {
        if (queue.size() < kth) {
            queue.offer(val);
        } else if (val > queue.peek()) {
            queue.poll();
            queue.offer(val);
        }
        return queue.peek();
    }

    public static void main(String[] args) {
        /*int k = 3;
        int[] arr = new int[]{4, 5, 8, 2};
        KthLargest kthLargest = new KthLargest(k, arr);
        System.out.println(kthLargest.add(3));
        System.out.println(kthLargest.add(5));
        System.out.println(kthLargest.add(10));
        System.out.println(kthLargest.add(9));
        System.out.println(kthLargest.add(4));*/

        int k = 7;
        int[] arr = new int[]{-10,1,3,1,4,10,3,9,4,5,1};
        KthLargest kthLargest = new KthLargest(k, arr);
        System.out.println(kthLargest.add(3));
        System.out.println(kthLargest.add(2));
        System.out.println(kthLargest.add(3));
        System.out.println(kthLargest.add(1));
        System.out.println(kthLargest.add(2));
        System.out.println(kthLargest.add(4));
        System.out.println(kthLargest.add(5));
        System.out.println(kthLargest.add(5));
    }
}
