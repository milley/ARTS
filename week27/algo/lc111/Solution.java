package com.milley.minimumdepthofbinarytree;

import com.milley.common.TreeNode;

class Solution {
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null) {
            return 1 + minDepth(root.right);
        }
        if (root.right == null) {
            return 1 + minDepth(root.left);
        }

        TreeNode left = root.left;
        TreeNode right = root.right;
        return 1 + Math.min(minDepth(left), minDepth(right));
    }
}
