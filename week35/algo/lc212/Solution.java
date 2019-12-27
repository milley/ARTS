class Solution {
    Set<String> res = new HashSet<>();
    
    public class Trie {
        public class TrieNode {
            public char data;
            public TrieNode[] children = new TrieNode[26];
            public boolean isEndingChar = false;

            public TrieNode(char data) {
                this.data = data;
            }
        }
        public TrieNode root;
        public Trie() {
            root = new TrieNode('/');
        }

        public void insert(String word) {
            TrieNode p = root;
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (p.children[index] == null) {
                    TrieNode node = new TrieNode(word.charAt(i));
                    p.children[index] = node;
                }
                p = p.children[index];
            }
            p.isEndingChar = true;
        }

        public boolean search(String word) {
            TrieNode p = root;
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (p.children[index] == null) {
                    return false;
                } else {
                    p = p.children[index];
                }
            }
            if (p.isEndingChar) {
                return true;
            }
            return false;
        }

        public boolean startsWith(String prefix) {
            TrieNode p = root;
            for (int i = 0; i < prefix.length(); i++) {
                int index = prefix.charAt(i) - 'a';
                if (p.children[index] == null) {
                    return false;
                } else {
                    p = p.children[index];
                }
            }
            return true;
        }
    }

    public List<String> findWords(char[][] board, String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }

        int m = board.length, n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(board, visited, "", i, j, trie);
            }
        }

        return new ArrayList<>(res);
    }

    private void dfs(char[][] board, boolean[][] visited, String str, int x, int y, Trie trie) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
            return;
        }
        if (visited[x][y]) {
            return;
        }

        str += board[x][y];
        if (!trie.startsWith(str)) {
            return;
        }

        if (trie.search(str)) {
            res.add(str);
        }

        visited[x][y] = true;
        dfs(board, visited, str, x - 1, y, trie);
        dfs(board, visited, str, x + 1, y, trie);
        dfs(board, visited, str, x, y - 1, trie);
        dfs(board, visited, str, x, y + 1, trie);
        visited[x][y] = false;
    }
}