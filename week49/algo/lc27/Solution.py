from typing import List

class Solution:
    def removeElement(self, nums: List[int], val: int) -> int:
        n = len(nums)
        i = -1
        j = 0
        cnt = 0
        while j <= n - 1:
            if nums[j] != val:
                i += 1
                cnt += 1
                nums[i] = nums[j]
            j += 1

        #print(nums)
        return cnt
