package com.milley.validsudoku;

/**
 * @author milley
 */
public class Solution {
    public boolean isValidSudoku(char[][] board)
    {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
            {
                if (board[i][j] == '.') {
                    continue;
                }
                if (!isValid(board, board[i][j], i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValid(char[][] board, char ch, int idx, int jdx) {
        for (int t = 0; t < board.length; t++) {
            if (board[t][jdx] != '.' && board[t][jdx] == ch && t != idx) {
                return false;
            }
            if (board[idx][t] != '.' && board[idx][t] == ch && t != jdx) {
                return false;
            }
            if (board[3 * (idx / 3) + t / 3][3 * (jdx / 3) + t % 3] != '.'
                    && board[3 * (idx / 3) + t / 3][3 * (jdx / 3) + t % 3] == ch
                    && (3 * (idx / 3) + t / 3 != idx)
                    && (3 * (jdx / 3) + t % 3 != jdx)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        char[][] board = {
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}
        };


        System.out.println(new Solution().isValidSudoku(board));
    }
}
