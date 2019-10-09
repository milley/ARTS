package com.milley.climbingstairs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author milley
 */
public class Solution {
    private Map<Integer, Integer> stepMap = new HashMap<>();
    public Solution() {
        stepMap.put(1, 1);
        stepMap.put(2, 2);
    }
    public int climbStairs(int n) {
        if (stepMap.get(n) == null) {
            stepMap.put(n, climbStairs(n - 1) + climbStairs(n - 2));
        }
        return stepMap.get(n);
    }
}
