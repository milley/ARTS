from typing import List


class Solution:
    def findMinArrowShots(self, points: List[List[int]]) -> int:
        if not points:
            return 0

        points.sort(key=lambda balloon: balloon[1])
        pos = points[0][1]
        ans = 1
        for balloon in points:
            if balloon[0] > pos:
                pos = balloon[1]
                ans += 1

        return ans


def test1():
    solution = Solution()
    points = [[10, 16], [2, 8], [1, 6], [7, 12]]
    assert(solution.findMinArrowShots(points) == 2)


def test2():
    solution = Solution()
    points = [[1, 2], [3, 4], [5, 6], [7, 8]]
    assert(solution.findMinArrowShots(points) == 4)


def test3():
    solution = Solution()
    points = [[1, 2], [2, 3], [3, 4], [4, 5]]
    assert(solution.findMinArrowShots(points) == 2)


if __name__ == "__main__":
    test1()
    test2()
    test3()
