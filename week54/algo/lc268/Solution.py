from typing import List


class Solution:
    def missingNumber1(self, nums: List[int]) -> int:
        nums.sort()
        t = 0
        for i in nums:
            if i != t:
                return t
            t += 1
        return t

    def missingNumber(self, nums: List[int]) -> int:
        n = len(nums)
        total = n * (n + 1) // 2
        arrSum = sum(nums)
        return total - arrSum


if __name__ == '__main__':
    solution = Solution()
    nums = [3, 0, 1]
    print(solution.missingNumber(nums))
