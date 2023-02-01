import collections
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def sumOfLeftLeaves(self, root: Optional[TreeNode]) -> int:
        def isLeafNode(node): return not node.left and not node.right

        def dfs(node: TreeNode) -> int:
            ans = 0
            if node.left:
                ans += node.left.val if isLeafNode(
                    node.left) else dfs(node.left)
            if node.right and not isLeafNode(node.right):
                ans += dfs(node.right)
            return ans

        return dfs(root) if root else 0

    def sumOfLeftLeaves1(self, root: Optional[TreeNode]) -> int:
        if not root:
            return 0

        def isLeafNode(node): return not node.left and not node.right

        q = collections.deque([root])
        ans = 0
        while q:
            node = q.popleft()
            if node.left:
                if isLeafNode(node.left):
                    ans += node.left.val
                else:
                    q.append(node.left)
            if node.right:
                if not isLeafNode(node.right):
                    q.append(node.right)

        return ans
