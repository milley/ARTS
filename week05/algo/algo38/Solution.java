package com.milley.CountAndSay;

public class Solution {
    public String countAndSay(int n) {
        if (n == 1) {
            return "1";
        } else {
            return build(countAndSay(n - 1));
        }
    }

    private String build(String numStr) {
        StringBuffer sb = new StringBuffer();
        char previous = numStr.charAt(0);
        int index = 1;

        for (int i = 1; i < numStr.length(); i++) {
            char current = numStr.charAt(i);
            if (current == previous) {
                index++;
            } else {
                sb.append(index);
                sb.append(previous);
                previous = current;
                index = 1;
            }
        }
        sb.append(index);
        sb.append(previous);
        return sb.toString();
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.countAndSay(5));
    }
}
