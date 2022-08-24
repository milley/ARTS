class Solution:
    def isPowerOfFour(self, n: int) -> bool:
        while n and n % 4 == 0:
            n //= 4
        return n == 1


def test1():
    solution = Solution()
    assert(solution.isPowerOfFour(16))


if __name__ == '__main__':
    test1()
