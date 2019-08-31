package com.milley.lowestcommonancestorofabinarytree;

/**
 * @author milley
 */
public class Solution {
    public static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) {
            this.val = val;
        }
    }

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        return left == null ? right : right == null ? left : root;
    }

    public static void main(String[] args) {
        // root = [6, 2, 8, 0, 4, 7, 9, null, null, 3, 5]
        TreeNode root = new TreeNode(6);
        root.left = new TreeNode(2);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(7);
        root.right.right = new TreeNode(9);
        root.left.right.left = new TreeNode(3);
        root.left.right.right = new TreeNode(5);

//        TreeNode node = lowestCommonAncestor(root, root.left, root.right);
        TreeNode node = lowestCommonAncestor(root, root.left, root.left.right);
        System.out.println(node.val);
    }

}
