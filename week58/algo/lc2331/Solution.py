from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def evaluateTree(self, root: Optional[TreeNode]) -> bool:
        def dfs(node: TreeNode) -> bool:
            if node.val == 0:
                return False
            if node.val == 1:
                return True

            if node.val == 2:
                return dfs(node.left) or dfs(node.right)
            elif node.val == 3:
                return dfs(node.left) and dfs(node.right)

        return dfs(root)


def test1():
    solution = Solution()
    root = TreeNode(2)
    node1 = TreeNode(1)
    root.left = node1
    node3 = TreeNode(3)
    root.right = node3

    node0 = TreeNode(0)
    node3.left = node0
    node4 = TreeNode(1)
    node3.right = node4

    ans = solution.evaluateTree(root)
    print(ans)
    assert(ans)


def test2():
    solution = Solution()
    root = TreeNode(0)
    ans = solution.evaluateTree(root)
    assert(not ans)


if __name__ == "__main__":
    test1()
    test2()
