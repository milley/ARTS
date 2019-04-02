package com.milley.RomanToInteger;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    private Map<String, Integer> romanMap;

    public Solution() {
        romanMap = new HashMap<>(13);
        romanMap.put("I", 1);
        romanMap.put("IV", 4);
        romanMap.put("V", 5);
        romanMap.put("IX", 9);
        romanMap.put("X", 10);
        romanMap.put("XL", 40);
        romanMap.put("L", 50);
        romanMap.put("XC", 90);
        romanMap.put("C", 100);
        romanMap.put("CD", 400);
        romanMap.put("D", 500);
        romanMap.put("CM", 900);
        romanMap.put("M", 1000);
    }
    public int romanToInt(String s) {
        int result = 0;
        if (s.length() == 1) {
            return romanMap.get(s) != null ? romanMap.get(s) : 0;
        }
        for (int i = 0; i < s.length(); i++) {
            String singleCh = String.valueOf(s.charAt(i));
            if (i < s.length() - 1) {
                String doubleChs = s.substring(i, i + 2);
                if (romanMap.containsKey(doubleChs)) {
                    result = result + romanMap.get(doubleChs);
                    i++;
                } else if (romanMap.containsKey(singleCh)) {
                    result = result + romanMap.get(singleCh);
                }
            }
            if (i > 0 && i == s.length() - 1 && !romanMap.containsKey(s.substring(i - 1, i + 1))) {
                result = result + romanMap.get(singleCh);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.romanToInt("LVIII"));
        System.out.println(solution.romanToInt("III"));
        System.out.println(solution.romanToInt("IV"));
        System.out.println(solution.romanToInt("IX"));
        System.out.println(solution.romanToInt("MCMXCIV"));
    }
}
