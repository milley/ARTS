from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def __init__(self):
        self.base = 0
        self.count = 0
        self.maxCount = 0
        self.answer = []

    def findMode(self, root: Optional[TreeNode]) -> List[int]:
        self.dfs(root)
        return self.answer

    def dfs(self, node: Optional[TreeNode]):
        if node is None:
            return
        self.dfs(node.left)
        self.update(node.val)
        self.dfs(node.right)

    def update(self, val: int):
        if val == self.base:
            self.count += 1
        else:
            self.count = 1
            self.base = val

        if self.count == self.maxCount:
            self.answer.append(self.base)
        if self.count > self.maxCount:
            self.maxCount = self.count
            self.answer.clear()
            self.answer.append(self.base)


def test1():
    solution = Solution()
    node1 = TreeNode(1, None, None)
    node2 = TreeNode(2, None, None)
    node3 = TreeNode(2, None, None)
    node1.right = node2
    node2.left = node3

    print(solution.findMode(node1))


if __name__ == '__main__':
    test1()
