from typing import List


class Solution:
    def sortColors(self, nums: List[int]) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """
        n = len(nums)
        c0 = 0
        c2 = n
        i = 0

        while i < c2:
            if nums[i] == 0:
                nums[i], nums[c0] = nums[c0], nums[i]
                c0 += 1
                i += 1
            elif nums[i] == 2:
                c2 -= 1
                nums[i], nums[c2] = nums[c2], nums[i]
            else:
                i += 1
