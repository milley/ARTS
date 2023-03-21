from typing import List


class Solution:
    def restoreMatrix(self, rowSum: List[int], colSum: List[int]) -> List[List[int]]:
        n, m = len(rowSum), len(colSum)
        matrix = [[0] * m for _ in range(n)]
        #print(matrix)

        i = j = 0
        while i < n and j < m:
            v = min(rowSum[i], colSum[j])
            matrix[i][j] = v
            rowSum[i] -= v
            colSum[j] -= v
            if rowSum[i] == 0:
                i += 1
            if colSum[j] == 0:
                j += 1

        return matrix


def test1():
    solution = Solution()
    rowSum = [3, 8]
    colSum = [4, 7]
    l = solution.restoreMatrix(rowSum, colSum)
    print(l)


if __name__ == '__main__':
    test1()
