from typing import List
import collections


class Solution:
    # BFS
    def updateMatrix(self, mat: List[List[int]]) -> List[List[int]]:
        m, n = len(mat), len(mat[0])
        dist = [[0] * n for _ in range(m)]
        zeroes_pos = [(i, j) for i in range(m)
                      for j in range(n) if mat[i][j] == 0]
        q = collections.deque(zeroes_pos)
        seen = set(zeroes_pos)

        while q:
            i, j = q.popleft()
            for ni, nj in [(i - 1, j), (i + 1, j), (i, j - 1), (i, j + 1)]:
                if 0 <= ni < m and 0 <= nj < n and (ni, nj) not in seen:
                    dist[ni][nj] = dist[i][j] + 1
                    q.append((ni, nj))
                    seen.add((ni, nj))

        return dist

    # DP
    def updateMatrix1(self, mat: List[List[int]]) -> List[List[int]]:
        m, n = len(mat), len(mat[0])
        dist = [[10**9] * n for _ in range(m)]

        for i in range(m):
            for j in range(n):
                if mat[i][j] == 0:
                    dist[i][j] = 0

        for i in range(m):
            for j in range(n):
                if i - 1 >= 0:
                    dist[i][j] = min(dist[i][j], dist[i - 1][j] + 1)
                if j - 1 >= 0:
                    dist[i][j] = min(dist[i][j], dist[i][j - 1] + 1)

        for i in range(m - 1, -1, -1):
            for j in range(n - 1, -1, -1):
                if i + 1 < m:
                    dist[i][j] = min(dist[i][j], dist[i + 1][j] + 1)
                if j + 1 < n:
                    dist[i][j] = min(dist[i][j], dist[i][j + 1] + 1)
        return dist


def test1():
    solution = Solution()
    mat = [[0, 0, 0], [0, 1, 0], [0, 0, 0]]
    out = [[0, 0, 0], [0, 1, 0], [0, 0, 0]]
    assert(solution.updateMatrix1(mat) == out)


def test2():
    solution = Solution()
    mat = [[0, 0, 0], [0, 1, 0], [1, 1, 1]]
    out = [[0, 0, 0], [0, 1, 0], [1, 2, 1]]
    assert(solution.updateMatrix1(mat) == out)


def test3():
    solution = Solution()
    mat = [[0], [1]]
    out = [[0], [1]]
    assert(solution.updateMatrix1(mat) == out)


if __name__ == '__main__':
    test1()
    test2()
    test3()
