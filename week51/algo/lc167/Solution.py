from typing import List


class Solution:
    def twoSum(self, numbers: List[int], target: int) -> List[int]:
        n = len(numbers)
        for i in range(n):
            low, high = i + 1, n - 1
            while low <= high:
                mid = (low + high) // 2
                if numbers[mid] == target - numbers[i]:
                    return [i + 1, mid + 1]
                elif numbers[mid] > target - numbers[i]:
                    high = mid - 1
                else:
                    low = mid + 1
        return [-1, -1]

    def twoSum1(self, numbers: List[int], target: int) -> List[int]:
        n = len(numbers)
        i = 0
        j = n - 1
        while i < j:
            if numbers[i] + numbers[j] == target:
                return [i + 1, j + 1]
            else:
                if numbers[i] + numbers[j] < target:
                    i += 1
                else:
                    j -= 1
        return []


def main():
    solution = Solution()
    list = [2, 7, 11, 15]
    target = 9
    l = solution.twoSum1(list, target)
    print(l)


if __name__ == '__main__':
    main()
