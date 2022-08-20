from typing import List

class Solution:
    def findTargetSumWays(self, nums: List[int], target: int) -> int:
        n = len(nums)
        total = sum(nums)
        if total < target: return 0
        
        dp = {(0, 0): 1}
        for i in range(1, n + 1):
            for j in range(-total, total + 1):
                dp[(i, j)] = dp.get((i-1, j - nums[i - 1]), 0) + dp.get((i - 1, j + nums[i - 1]), 0)
        return dp.get((n, target), 0)
    
    def findTargetSumWays1(self, nums: List[int], target: int) -> int:
        d = {}
        def dfs(cur, i, d):
            if i < len(nums) and (cur, i) not in d:
                d[(cur, i)] = dfs(cur + nums[i], i + 1, d) + dfs(cur - nums[i], i + 1, d)
            return d.get((cur, i), int(cur == target))
        return dfs(0, 0, d)
    
def test1():
    solution = Solution()
    nums = [1,1,1,1,1]
    target = 3
    assert(solution.findTargetSumWays1(nums, target) == 5)
    
if __name__ == '__main__':
    test1()