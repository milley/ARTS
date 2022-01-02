package week45.algo.lc222;

public class Solution {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    private int height(TreeNode root) {
        if (root == null)
            return 0;
        return height(root.left) + 1;
    }

    public int countNodes(TreeNode root) {
        if (root == null)
            return 0;
        int l = height(root.left);
        int r = height(root.right);
        if (l == r) {
            return (1 << l) + countNodes(root.right);
        } else {
            return (1 << (l - 1)) + countNodes(root.left);
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);

        TreeNode n2 = new TreeNode(2);
        TreeNode n4 = new TreeNode(4);
        TreeNode n5 = new TreeNode(5);
        n2.left = n4;
        n2.right = n5;

        TreeNode n3 = new TreeNode(3);
        TreeNode n6 = new TreeNode(6);
        n3.left = n6;

        root.left = n2;
        root.right = n3;

        System.out.println(solution.countNodes(root));
    }
}
