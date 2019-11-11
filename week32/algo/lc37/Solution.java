package com.milley.sudokusolver;

/**
 * @author milley
 */
public class Solution {
    public void solveSudoku(char[][] board) {
        isValidSudoku(board);
    }

    private boolean isValidSudoku(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] >= '1' && board[i][j] <= '9') {
                    continue;
                } else if (board[i][j] == '.') {
                    for (char ch = '1'; ch <= '9'; ch++) {
                        if (isValid(board, ch, i, j)) {
                            board[i][j] = ch;
                            if (isValidSudoku(board)) {
                                return true;
                            } else {
                                board[i][j] = '.';
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(char[][] board, char ch, int idx, int jdx) {
        for (int t = 0; t < board.length; t++) {
            if (board[t][jdx] != '.' && board[t][jdx] == ch) {
                return false;
            }
            if (board[idx][t] != '.' && board[idx][t] == ch) {
                return false;
            }
            if (board[3 * (idx / 3) + t / 3][3 * (jdx / 3) + t % 3] != '.'
                    && board[3 * (idx / 3) + t / 3][3 * (jdx / 3) + t % 3] == ch) {
                return false;
            }
        }
        return true;
    }

    public static void print(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print("\n");
        }
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
        new Solution().solveSudoku(board);
        print(board);
    }
}
