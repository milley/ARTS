class Solution:
    def isPowerOfThree(self, n: int) -> bool:
        while n and n % 3 == 0:
            n //= 3

        return n == 1


def test1():
    solution = Solution()
    assert(solution.isPowerOfThree(27))


if __name__ == '__main__':
    test1()
