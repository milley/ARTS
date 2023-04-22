from typing import List


class Solution:
    def permuteUnique(self, nums: List[int]) -> List[List[int]]:
        nums.sort()
        res = []
        ans = []
        n = len(nums)
        visited = [0] * n

        def backtrack():
            if len(ans) == n:
                res.append(ans[::])
            for i in range(n):
                if visited[i] or (i > 0 and nums[i] == nums[i - 1] and not visited[i - 1]):
                    continue
                ans.append(nums[i])
                visited[i] = True
                backtrack()
                ans.pop()
                visited[i] = False

        backtrack()
        return res


def test1():
    solution = Solution()
    nums = [1, 2, 3]
    out = solution.permuteUnique(nums)
    print(out)


def test2():
    solution = Solution()
    nums = [1, 1, 2]
    out = solution.permuteUnique(nums)
    print(out)


if __name__ == '__main__':
    test1()
    test2()
