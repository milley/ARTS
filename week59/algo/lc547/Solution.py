import collections
from typing import List


class Solution:
    def findCircleNum(self, isConnected: List[List[int]]) -> int:
        return self.findCircleNum2(isConnected)

    def findCircleNum1(self, isConnected: List[List[int]]) -> int:
        def dfs(i: int):
            for j in range(cities):
                if isConnected[i][j] == 1 and j not in visited:
                    visited.add(j)
                    dfs(j)

        cities = len(isConnected)
        visited = set()
        provinces = 0

        for i in range(cities):
            if i not in visited:
                dfs(i)
                provinces += 1

        return provinces

    def findCircleNum2(self, isConnected: List[List[int]]) -> int:
        cities = len(isConnected)
        visited = set()
        provinces = 0

        for i in range(cities):
            if i not in visited:
                Q = collections.deque([i])
                while Q:
                    j = Q.popleft()
                    visited.add(j)
                    for k in range(cities):
                        if isConnected[j][k] == 1 and k not in visited:
                            Q.append(k)
                provinces += 1

        return provinces


def test1():
    solution = Solution()
    isConnected = [[1, 1, 0], [1, 1, 0], [0, 0, 1]]
    assert(solution.findCircleNum(isConnected) == 2)


def test2():
    solution = Solution()
    isConnected = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]
    assert(solution.findCircleNum(isConnected) == 3)


if __name__ == '__main__':
    test1()
    test2()
