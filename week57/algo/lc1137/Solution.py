class Solution:
    def tribonacci(self, n: int) -> int:
        if n == 0:
            return 0
        elif n == 1 or n == 2:
            return 1
        else:
            return self.tribonacci(n - 3) + self.tribonacci(n - 2) + self.tribonacci(n - 1)

    def tribonacci1(self, n: int) -> int:
        if n == 0:
            return 0
        elif n == 1 or n == 2:
            return 1

        p = 0
        q = r = 1
        for i in range(3, n + 1):
            s = p + q + r
            p, q, r = q, r, s
        return s
