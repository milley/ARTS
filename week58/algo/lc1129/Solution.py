from typing import List


class Solution:
    def shortestAlternatingPaths(self, n: int, redEdges: List[List[int]], blueEdges: List[List[int]]) -> List[int]:
        g = [[] for _ in range(n)]
        for x, y in redEdges:
            g[x].append((y, 0))
        for x, y in blueEdges:
            g[x].append((y, 1))

        dis = [-1] * n
        vis = {(0, 0), (0, 1)}
        q = [(0, 0), (0, 1)]
        level = 0
        while q:
            tmp = q
            q = []
            for x, color in tmp:
                if dis[x] == -1:
                    dis[x] = level
                for p in g[x]:
                    if p[1] != color and p not in vis:
                        vis.add(p)
                        q.append(p)

            level += 1
        return dis


def test1():
    solution = Solution()
    n = 3
    red_edges = [[0, 1], [1, 2]]
    blue_edges = []
    #print(solution.shortestAlternatingPaths(n, red_edges, blue_edges))
    assert(solution.shortestAlternatingPaths(
        n, red_edges, blue_edges) == [0, 1, -1])


if __name__ == '__main__':
    test1()
