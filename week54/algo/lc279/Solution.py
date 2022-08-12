import sys
import math


class Solution:
    def numSquares(self, n: int) -> int:
        f = [0] * (n + 1)
        for i in range(1, n + 1):
            minn = sys.maxsize
            j = 1
            while j * j <= i:
                minn = min(minn, f[i - j * j])
                j += 1
            f[i] = minn + 1

        return f[n]

    def isPerfectSquare(self, x: int) -> bool:
        y = math.floor(math.sqrt(x))
        return x == y * y

    def checkAnswer4(self, x: int) -> bool:
        while x % 4 == 0:
            x //= 4
        return x % 8 == 7

    def numSquares1(self, n: int) -> int:
        if self.isPerfectSquare(n):
            return 1
        if self.checkAnswer4(n):
            return 4
        i = 1
        while i * i <= n:
            j = n - i * i
            if self.isPerfectSquare(j):
                return 2
            i += 1
        return 3


def test1():
    solution = Solution()
    assert(solution.numSquares(12) == 3)
    assert(solution.numSquares(13) == 2)


def test2():
    solution = Solution()
    assert(solution.numSquares1(12) == 3)
    assert(solution.numSquares1(13) == 2)


if __name__ == '__main__':
    test1()
    test2()
