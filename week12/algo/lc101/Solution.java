package com.milley.symmetrictree;

public class Solution {
    public boolean isSymmetric(TreeNode root) {
        return compareMirrorTree(root, root);
    }


    private boolean compareMirrorTree(TreeNode sourceTree, TreeNode destTree) {
        if (sourceTree == null && destTree == null) {
            return true;
        }
        if (sourceTree == null || destTree == null) {
            return false;
        }
        return (sourceTree.val == destTree.val) && compareMirrorTree(sourceTree.left, destTree.right)
                && compareMirrorTree(sourceTree.right, destTree.left);
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        TreeNode root21 = new TreeNode(2);
        TreeNode root22 = new TreeNode(2);
        TreeNode root31 = new TreeNode(3);
        TreeNode root32 = new TreeNode(3);
        TreeNode root41 = new TreeNode(4);
        TreeNode root42 = new TreeNode(4);

        root21.left = root31;
        root21.right = root41;

        root22.left = root42;
        root22.right = root32;

        root.left = root21;
        root.right = root22;

        System.out.println(new Solution().isSymmetric(root));
    }
}
