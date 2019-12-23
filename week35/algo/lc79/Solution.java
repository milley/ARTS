package com.milley.wordsearch;


/**
 * @author milley
 */
public class Solution {

    public boolean exist(char[][] board, String word) {
        for (char ch : word.toCharArray()) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (ch == board[i][j] && chInBoard(board, i, j, 0, word)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean chInBoard(char[][] board, int idx, int jdx, int len, String word) {
        if (len == word.length()) {
            return true;
        }
        if (idx < 0 || idx >= board.length || jdx < 0 || jdx >= board[idx].length) {
            return false;
        }
        if (board[idx][jdx] != word.charAt(len)) {
            return false;
        }

        char t = board[idx][jdx];
        board[idx][jdx] = '#';

        boolean found = chInBoard(board, idx + 1, jdx, len + 1, word) ||
                chInBoard(board, idx - 1, jdx, len + 1, word) ||
                chInBoard(board, idx, jdx + 1, len + 1, word) ||
                chInBoard(board, idx, jdx - 1, len + 1, word);
        board[idx][jdx] = t;

        return found;
    }

    public static void main(String[] args) {
        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };

        System.out.println(new Solution().exist(board, "ABCCED"));
        System.out.println(new Solution().exist(board, "SEE"));
        System.out.println(new Solution().exist(board, "ABCB"));
    }
}
