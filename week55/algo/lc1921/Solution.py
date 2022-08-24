from typing import List


class Solution:
    def eliminateMaximum(self, dist: List[int], speed: List[int]) -> int:
        digits = []
        n = len(dist)
        for i in range(n):
            digits.append((dist[i] - 1) // speed[i])
        #digits.sort()
        digits = sorted(digits)

        for i in range(n):
            if digits[i] < i:
                return i

        return n


def test1():
    solution = Solution()
    dist = [1, 3, 4]
    speed = [1, 1, 1]
    assert(solution.eliminateMaximum(dist, speed) == 3)


def test2():
    solution = Solution()
    dist = [1, 1, 2, 3]
    speed = [1, 1, 1, 1]
    assert(solution.eliminateMaximum(dist, speed) == 1)


def test3():
    solution = Solution()
    dist = [3, 2, 4]
    speed = [5, 3, 2]
    assert(solution.eliminateMaximum(dist, speed) == 1)


def test4():
    solution = Solution()
    dist = [3, 5, 7, 4, 5]
    speed = [2, 3, 6, 3, 2]

    assert(solution.eliminateMaximum(dist, speed) == 2)


def test5():
    solution = Solution()
    dist = [4, 3, 4]
    speed = [1, 1, 2]

    assert(solution.eliminateMaximum(dist, speed) == 3)


if __name__ == '__main__':
    test1()
    test2()
    test3()
    test4()
    test5()
