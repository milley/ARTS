from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def zigzagLevelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        ans = []
        if not root:
            return ans

        queue = [(root, 1)]
        while queue:
            node, level = queue.pop(0)
            if level > len(ans):
                ans.append([node.val])
            else:
                ans[-1].append(node.val)

            if node.left:
                queue.append((node.left, level + 1))
            if node.right:
                queue.append((node.right, level + 1))

        for i in range(len(ans)):
            if i % 2 == 1:
                ans[i] = ans[i][::-1]

        return ans


def test1():
    solution = Solution()
    root = TreeNode(3, None, None)
    node9 = TreeNode(9, None, None)
    node20 = TreeNode(20, None, None)
    root.left = node9
    root.right = node20

    node15 = TreeNode(15, None, None)
    node7 = TreeNode(7, None, None)
    node20.left = node15
    node20.right = node7

    print(solution.zigzagLevelOrder(root))


if __name__ == "__main__":
    test1()
