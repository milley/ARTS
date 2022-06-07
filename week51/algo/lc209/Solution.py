from typing import List
import sys


class Solution:
    def minSubArrayLen(self, target: int, nums: List[int]) -> int:
        n = len(nums)
        left, right, total = 0, 0, 0
        res = sys.maxsize
        while right < n:
            total += nums[right]
            while total >= target:
                res = min(res, right - left + 1)
                total -= nums[left]
                left += 1
            right += 1
        return 0 if res == sys.maxsize else res


def main():
    solution = Solution()
    nums = [2, 3, 1, 2, 4, 3]
    target = 7
    m = solution.minSubArrayLen(target, nums)
    assert(m == 2)


if __name__ == '__main__':
    main()
