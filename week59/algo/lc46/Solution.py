from typing import List


class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:

        def backtrack(first=0):
            if first == n:
                res.append(nums[:])
            for i in range(first, n):
                nums[first], nums[i] = nums[i], nums[first]
                backtrack(first + 1)
                nums[first], nums[i] = nums[i], nums[first]

        n = len(nums)
        res = []
        backtrack()

        return res


def test1():
    solution = Solution()
    nums = [1, 2, 3]
    out = solution.permute(nums)
    print(out)


if __name__ == '__main__':
    test1()
