from collections import Counter
from typing import List


class Solution:
    def countTriplets(self, arr: List[int]) -> int:
        return self.countTriplets4(arr)

    def countTriplets1(self, arr: List[int]) -> int:
        n = len(arr)
        s = [0]
        for val in arr:
            s.append(s[-1] ^ val)

        ans = 0
        for i in range(n):
            for j in range(i + 1, n):
                for k in range(j, n):
                    if s[i] == s[k + 1]:
                        #print(i, j, k)
                        ans += 1
        return ans

    def countTriplets2(self, arr: List[int]) -> int:
        n = len(arr)
        s = [0]
        for val in arr:
            s.append(s[-1] ^ val)
        ans = 0
        for i in range(n):
            for k in range(i + 1, n):
                if s[i] == s[k + 1]:
                    ans += k - i

        return ans

    def countTriplets3(self, arr: List[int]) -> int:
        n = len(arr)
        s = [0]
        for val in arr:
            s.append(s[-1] ^ val)

        cnt, total = Counter(), Counter()
        ans = 0
        for k in range(n):
            if s[k + 1] in cnt:
                ans += cnt[s[k + 1]] * k - total[s[k + 1]]
            cnt[s[k]] += 1
            total[s[k]] += k

        return ans

    # Assignment expressions start at python3.8
    # def countTriplets4(self, arr: List[int]) -> int:
    #     print("------------------------")
    #     cnt, total = Counter(), Counter()
    #     ans = s = 0

    #     for k, val in enumerate(arr):
    #         if (t := s ^ val) in cnt:
    #             ans += cnt[t] * k - total[t]
    #         cnt[s] += 1
    #         total[s] += k
    #         s = t

    #     return ans


def test1():
    solution = Solution()

    arr = [2, 3, 1, 6, 7]
    assert(solution.countTriplets(arr) == 4)


def test2():
    solution = Solution()

    arr = [1, 1, 1, 1, 1]
    assert(solution.countTriplets(arr) == 10)


def test3():
    solution = Solution()

    arr = [2, 3]
    assert(solution.countTriplets(arr) == 0)


def test4():
    solution = Solution()

    arr = [1, 3, 5, 7, 9]
    assert(solution.countTriplets(arr) == 3)


def test5():
    solution = Solution()

    arr = [7, 11, 12, 9, 5, 2, 7, 17, 22]
    assert(solution.countTriplets(arr) == 8)


if __name__ == '__main__':
    test1()
    test2()
    test3()
    test4()
    test5()
