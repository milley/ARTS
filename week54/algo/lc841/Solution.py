from typing import List
import collections


class Solution:
    # DFS
    def canVisitAllRooms(self, rooms: List[List[int]]) -> bool:
        def dfs(x: int):
            vis.add(x)
            nonlocal num
            num += 1
            for it in rooms[x]:
                if it not in vis:
                    dfs(it)

        n = len(rooms)
        num = 0
        vis = set()
        dfs(0)
        return num == n

    # BFS
    def canVisitAllRooms1(self, rooms: List[List[int]]) -> bool:
        n = len(rooms)
        num = 0
        vis = {0}
        que = collections.deque([0])

        while que:
            x = que.popleft()
            num += 1
            for it in rooms[x]:
                if it not in vis:
                    vis.add(it)
                    que.append(it)

        return num == n


def test1():
    solution = Solution()
    rooms = [[1], [2], [3], []]
    assert(solution.canVisitAllRooms(rooms))


if __name__ == '__main__':
    test1()
