class Solution:
    def kItemsWithMaximumSum(self, numOnes: int, numZeros: int, numNegOnes: int, k: int) -> int:
        if k < numOnes:
            return k
        elif k < numOnes + numZeros:
            return numOnes

        return numOnes - (k - numOnes - numZeros)


def test1():
    solution = Solution()
    numOnes = 3
    numZeros = 2
    numNegOnes = 0
    k = 2
    assert(solution.kItemsWithMaximumSum(
        numOnes, numZeros, numNegOnes, k) == 2)


def test2():
    solution = Solution()
    numOnes = 3
    numZeros = 2
    numNegOnes = 0
    k = 4
    assert(solution.kItemsWithMaximumSum(
        numOnes, numZeros, numNegOnes, k) == 3)


if __name__ == '__main__':
    test1()
    test2()
