from collections import deque
from typing import Generator, List


class Solution:
    def openLock(self, deadends: List[str], target: str) -> int:
        if target == "0000":
            return 0

        dead = set(deadends)
        if "0000" in dead:
            return -1

        def num_prev(x: str) -> str:
            return "9" if x == "0" else str(int(x) - 1)

        def num_succ(x: str) -> str:
            return "0" if x == "9" else str(int(x) + 1)

        def get(status: str) -> Generator[str, None, None]:
            s = list(status)
            for i in range(4):
                num = s[i]
                s[i] = num_prev(num)
                yield "".join(s)
                s[i] = num_succ(num)
                yield "".join(s)
                s[i] = num

        q = deque([("0000", 0)])
        seen = {"0000"}
        while q:
            status, step = q.popleft()
            for next_status in get(status):
                if next_status not in seen and next_status not in dead:
                    if next_status == target:
                        return step + 1
                    q.append((next_status, step + 1))
                    seen.add(next_status)

        return -1


def test1():
    solution = Solution()
    deadends = ["0201", "0101", "0102", "1212", "2002"]
    target = "0202"
    assert(solution.openLock(deadends, target) == 6)


def test2():
    solution = Solution()
    deadends = ["8888"]
    target = "0009"
    assert(solution.openLock(deadends, target) == 1)


def test3():
    solution = Solution()
    deadends = ["8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"]
    target = "8888"
    assert(solution.openLock(deadends, target) == -1)


if __name__ == "__main__":
    test1()
    test2()
    test3()
