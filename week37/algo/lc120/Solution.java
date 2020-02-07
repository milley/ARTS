package com.milley.triangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author milley
 */
public class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        List<Integer> mini = triangle.get(triangle.size() - 1);
        for (int i = triangle.size() - 2; i >= 0; i--) {
            for (int j = 0; j < triangle.get(i).size(); j++) {
                mini.set(j, triangle.get(i).get(j) + Math.min(mini.get(j), mini.get(j + 1)));
            }
        }

        return mini.get(0);
    }

    public static void main(String[] args) {
        List<List<Integer>> triangle = new ArrayList<>();
        List<Integer> row0 = new ArrayList<>();
        row0.add(2);
        triangle.add(row0);

        List<Integer> row1 = new ArrayList<>();
        row1.add(3);
        row1.add(4);
        triangle.add(row1);

        List<Integer> row2 = new ArrayList<>();
        row2.add(6);
        row2.add(5);
        row2.add(7);
        triangle.add(row2);
        List<Integer> row3 = new ArrayList<>();
        row3.add(4);
        row3.add(1);
        row3.add(8);
        row3.add(3);
        triangle.add(row3);

        System.out.println(new Solution().minimumTotal(triangle));
    }
}
