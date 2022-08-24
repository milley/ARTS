class Solution:
    def fib(self, n: int) -> int:
        if n == 0:
            return 0
        if n == 1:
            return 1
        return self.fib(n - 1) + self.fib(n - 2)

    # x * x = x + 1
    def fib1(self, n: int) -> int:
        sqrt5 = 5 ** 0.5
        fibN = ((1 + sqrt5) / 2) ** n - ((1 - sqrt5) / 2) ** n
        return round(fibN / sqrt5)


if __name__ == '__main__':
    solution = Solution()
    assert(solution.fib1(2) == 1)
    assert(solution.fib1(3) == 2)
    assert(solution.fib1(4) == 3)
