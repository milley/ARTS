package com.milley.invertbinarytree;

public class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode leftChild = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(leftChild);

        return root;
    }


    public class TreeNode {
        int val;
        TreeNode left, right;

        public TreeNode(int val) {
            this.val = val;
        }
    }
}
