from typing import List


class Solution:
    def arithmeticTriplets(self, nums: List[int], diff: int) -> int:
        s = set(nums)
        res = 0
        for num in nums:
            if (num + diff) in s and (num + 2 * diff) in s:
                res += 1

        return res


def test1():
    solution = Solution()
    nums = [0, 1, 4, 6, 7, 10]
    diff = 3
    assert(solution.arithmeticTriplets(nums, diff) == 2)


def test2():
    solution = Solution()
    nums = [4, 5, 6, 7, 8, 9]
    diff = 2
    assert(solution.arithmeticTriplets(nums, diff) == 2)


if __name__ == '__main__':
    test1()
    test2()
