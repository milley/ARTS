package com.milley.nqueens;

import java.util.*;

/**
 * @author milley
 */
public class Solution {
    Set<Integer> cols = new HashSet<>();
    Set<Integer> pie = new HashSet<>();
    Set<Integer> na = new HashSet<>();
    Set<Integer> currSet = new LinkedHashSet<>();
    List<Set<Integer>> currList = new ArrayList<>();

    public List<List<String>> solveNQueens(int n) {
        if (n < 1) {
            return new ArrayList<>();
        }
        dfs(n, 0, currSet);
        return generateList(currList, n);
    }

    private String generateStarString(int i, int n) {
        StringBuffer sb = new StringBuffer();
        for (int ix = 0; ix < n; ix++) {
            if (ix == i) {
                sb.append("Q");
            } else {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    private List<List<String>> generateList(List<Set<Integer>> result, int n) {
        List<List<String>> resultStringList = new ArrayList<>();
        for (Set<Integer> set : result) {
            List<String> resultString = new ArrayList<>();
            for (Integer i : set) {
                resultString.add(generateStarString(i, n));
            }
            resultStringList.add(resultString);
        }
        return resultStringList;
    }

    private void dfs(int n, int row, Set<Integer> currSet) {
        if (row == n) {
            Set<Integer> set = new LinkedHashSet<>(currSet);
            currList.add(set);
            return;
        }

        for (int col = 0; col < n; col++) {
            if (cols.contains(col) || pie.contains(col + row) || na.contains(row - col)) {
                continue;
            }
            cols.add(col);
            pie.add(row + col);
            na.add(row - col);

            currSet.add(col);
            dfs(n, row + 1, currSet);

            currSet.remove(col);
            cols.remove(col);
            pie.remove(row + col);
            na.remove(row - col);
        }
    }

    public static void main(String[] args) {
        List<List<String>> resultList = new Solution().solveNQueens(4);
        for (List<String> list : resultList) {
            System.out.println(list);
        }
    }
}
