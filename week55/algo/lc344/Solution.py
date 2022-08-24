from typing import List


class Solution:
    def reverseString(self, s: List[str]) -> None:
        n = len(s)
        i = 0
        while i < n // 2:
            s[i], s[n - i - 1] = s[n - i - 1], s[i]
            i += 1

        #print(s)


def test1():
    solution = Solution()
    s = ["h", "e", "l", "l", "o"]
    #s = ["H","a","n","n","a","h"]
    solution.reverseString(s)


if __name__ == '__main__':
    test1()
