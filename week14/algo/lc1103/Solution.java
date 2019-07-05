package com.milley.distributecandiestopeople;


public class Solution {
    public int[] distributeCandies(int candies, int num_people) {
        int[] peopleContains = new int[num_people];
        int maxFullIndex = calMaxFullElement(candies);
        int surplus = candies - sum(maxFullIndex);
        for (int i = 0; i < maxFullIndex; i++) {
            peopleContains[i % num_people] += (i + 1);
        }

        int remainder = maxFullIndex % num_people;
        if (surplus > 0) {
            peopleContains[remainder] += surplus;
        } else if (surplus < 0) {
            if (remainder == 0) {
                peopleContains[num_people - 1] += surplus;
            } else {
                peopleContains[remainder - 1] += surplus;
            }
        }
        return peopleContains;
    }

    private int calMaxFullElement(int candies) {
        return (int) Math.floor(Math.sqrt(candies * 2));
    }

    private int sum(int n) {
        return n * (n + 1) / 2;
    }

    public static void main(String[] args) {
        int[] arr = new Solution().distributeCandies(800, 40);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
