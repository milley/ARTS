from typing import List


class Solution:
    def maxArea(self, height: List[int]) -> int:
        n = len(height)
        i, j = 0, n - 1
        max_area = 0
        while i < j:
            tmp = 0
            if height[i] < height[j]:
                tmp = (j - i) * height[i]
                i += 1
            else:
                tmp = (j - i) * height[j]
                j -= 1
            if tmp > max_area:
                max_area = tmp
        return max_area


def main():
    solution = Solution()
    #height = [1,8,6,2,5,4,8,3,7]
    height = [1, 1]
    m = solution.maxArea(height)
    #assert(m == 49)
    assert(m == 1)


if __name__ == '__main__':
    main()
