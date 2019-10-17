package com.milley.generateparentheses;

import java.util.ArrayList;
import java.util.List;

/**
 * @author milley
 */
public class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> parenthesisList = new ArrayList<>(2 * n);
        generate("", parenthesisList, n, n);
        return parenthesisList;
    }

    public void generate(String sublist, List<String> list, int left, int right) {
        if (left == 0 && right == 0) {
            list.add(sublist);
            return;
        }
        if (left > 0) {
            generate(sublist + "(", list, left - 1, right);
        }
        if (right > 0 && right > left) {
            generate(sublist + ")", list, left, right - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().generateParenthesis(3));
    }
}
