package com.milley.nqueensii;

/**
 * @author milley
 */
public class Solution {
    public int count = 0;
    public int totalNQueens(int n) {
        boolean[] cols = new boolean[n];
        boolean[] d1 = new boolean[2 * n];
        boolean[] d2 = new boolean[2 * n];
        helper(0, cols, d1, d2, n);
        return count;
    }

    private void helper(int row, boolean[] cols, boolean[] d1, boolean[] d2, int n) {
        if (row == n) {
            count++;
        } else {
            for (int col = 0; col < n; col++) {
                int id1 = col - row + n;
                int id2 = col + row;
                if (cols[col] || d1[id1] || d2[id2]) {
                    continue;
                }

                cols[col] = true; d1[id1] = true; d2[id2] = true;
                helper(row + 1, cols, d1, d2, n);
                cols[col] = false; d1[id1] = false; d2[id2] = false;
            }
        }
    }

    public int totalNQueensWithBit(int n) {
        dfs(n, 0, 0, 0, 0);
        return count;
    }

    private void dfs(int n, int row, int cols, int pie, int na) {
        if (row >= n) {
            count++;
            return;
        }

        // 得到所有的空位
        int bits = (~(cols | pie | na)) & ((1 << n) - 1);
        while (bits > 0) {
            // 取最低位的1
            int p = bits & -bits;
            dfs(n, row + 1, cols | p, (pie | p) << 1,
                    (na | p) >> 1);
            // 去掉最低位的1
            bits = bits & (bits - 1);
        }
    }

    public static void main(String[] args) {
        // new Solution().totalNQueens(4);
        new Solution().totalNQueensWithBit(4);
        System.out.println(count);
    }
}
