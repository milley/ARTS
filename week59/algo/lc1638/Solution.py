class Solution:
    def countSubstrings(self, s: str, t: str) -> int:
        return self.countSubstrings2(s, t)

    def countSubstrings1(self, s: str, t: str) -> int:
        ans = 0
        for i in range(len(s)):
            for j in range(len(t)):
                diff = 0
                k = 0
                while i + k < len(s) and j + k < len(t):
                    if s[i + k] != t[j + k]:
                        diff += 1
                    if diff == 1:
                        print("i:{} j:{} k:{}".format(i, j, k))
                        ans += 1
                    elif diff > 1:
                        break
                    k += 1
        return ans

    def countSubstrings2(self, s: str, t: str) -> int:
        m, n = len(s), len(t)
        dpl = [[0] * (n + 1) for _ in range(m + 1)]
        dpr = [[0] * (n + 1) for _ in range(m + 1)]

        #print(dpl)
        #print(dpr)

        for i in range(m):
            for j in range(n):
                dpl[i + 1][j + 1] = (dpl[i][j] + 1) if s[i] == t[j] else 0

        for i in reversed(range(m)):
            for j in reversed(range(n)):
                dpr[i][j] = (dpr[i + 1][j + 1] + 1) if s[i] == t[j] else 0

        ans = 0
        for i in range(m):
            for j in range(n):
                if s[i] != t[j]:
                    ans += (dpl[i][j] + 1) * (dpr[i + 1][j + 1] + 1)
        return ans


def test1():
    solution = Solution()

    s1 = "aba"
    s2 = "baba"
    assert(solution.countSubstrings(s1, s2) == 6)


def test2():
    solution = Solution()

    s1 = "ab"
    s2 = "bb"
    assert(solution.countSubstrings(s1, s2) == 3)


def test3():
    solution = Solution()

    s1 = "a"
    s2 = "a"
    assert(solution.countSubstrings(s1, s2) == 0)


def test4():
    solution = Solution()

    s1 = "abe"
    s2 = "bbc"
    assert(solution.countSubstrings(s1, s2) == 10)


if __name__ == '__main__':
    test1()
    test2()
    test3()
    test4()
