from functools import cmp_to_key
import heapq
from typing import List, Tuple


class Frac:
    def __init__(self, idx: int, idy: int, x: int, y: int) -> None:
        self.idx = idx
        self.idy = idy
        self.x = x
        self.y = y

    def __lt__(self, other: "Frac") -> bool:
        return self.x * other.y < self.y * other.x


class Solution:
    def kthSmallestPrimeFraction(self, arr: List[int], k: int) -> List[int]:
        n = len(arr)
        q = [Frac(0, i, arr[0], arr[i]) for i in range(1, n)]
        heapq.heapify(q)

        for _ in range(k - 1):
            frac = heapq.heappop(q)
            i, j = frac.idx, frac.idy
            if i + 1 < j:
                heapq.heappush(q, Frac(i + 1, j, arr[i + 1], arr[j]))

        return [q[0].x, q[0].y]

    def kthSmallestPrimeFraction1(self, arr: List[int], k: int) -> List[int]:
        def cmp(x: Tuple[int, int], y: Tuple[int, int]) -> int:
            return -1 if x[0] * y[1] < x[1] * y[0] else 1

        n = len(arr)
        frac = list()
        for i in range(n):
            for j in range(i + 1, n):
                frac.append((arr[i], arr[j]))

        frac.sort(key=cmp_to_key(cmp))
        return list(frac[k - 1])


def test1():
    solution = Solution()
    arr = [1, 2, 3, 5]
    k = 3
    out = solution.kthSmallestPrimeFraction(arr, k)
    print(out)
    assert(out == [2, 5])


def test2():
    solution = Solution()
    arr = [1, 2, 3, 5]
    k = 3
    out = solution.kthSmallestPrimeFraction1(arr, k)
    print(out)
    assert(out == [2, 5])


if __name__ == '__main__':
    test1()
    test2()
