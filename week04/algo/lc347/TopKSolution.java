package com.milley.TopKFrequentElements;

import java.util.*;

public class TopKSolution {
    public static List<Integer> topKFrequent(int[] nums, int k) {
        List<Integer> elements = new ArrayList<>(k);
        Map<Integer, Integer> map = new HashMap<>(k);
        for (int i = 0; i < nums.length; i++) {
            int key = nums[i];
            if (map.get(key) != null) {
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }

        // Sort map by value
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        int index = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (index < k) {
                elements.add(list.get(i).getKey());
            }
            index++;
        }

        return elements;
    }

    public static void main(String[] args) {
        //int[] nums = {1, 1, 1, 2, 2, 3};
        int[] nums = {4, 1, -1, 2, -1, 2, 3};
        System.out.println(topKFrequent(nums, 2));
    }
}
