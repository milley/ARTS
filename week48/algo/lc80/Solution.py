from typing import List

class Solution:
    def removeDuplicates(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        
        flag = False
        i = 1
        j = 1
        while j < n:
            if nums[j] != nums[j - 1]:
                flag = False
                nums[i] = nums[j]
                i += 1
            else:
                if not flag:
                    nums[i] = nums[j]
                    i += 1
                flag = True
            j += 1
        
        #print(nums)
        return i