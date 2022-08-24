class Solution:
    def smallestNumber(self, num: int) -> int:
        arr = []
        abs_num = abs(num)
        while abs_num != 0:
            last_num = abs_num % 10
            arr.append(last_num)
            abs_num //= 10

        if num >= 0:
            arr.sort()

            zero_start = False
            if len(arr) > 0 and arr[0] == 0:
                zero_start = True
            for i in range(len(arr)):
                if zero_start and arr[i] != 0:
                    arr[0], arr[i] = arr[i], arr[0]
                    break

        else:
            arr.sort(reverse=True)

        n = len(arr)
        result = 0
        i = 0
        while i < n:
            left_num = arr[i]
            result += left_num * (10 ** (n - i - 1))
            i += 1

        return result if num > 0 else result * -1

    def smallestNumber1(self, num: int) -> int:
        if num == 0:
            return 0

        negative = (num) < 0
        num = abs(num)
        digits = sorted(int(digit) for digit in str(num))

        if negative:
            digits = digits[::-1]
        else:
            if digits[0] == 0:
                i = 1
                while digits[i] == 0:
                    i += 1
                digits[0], digits[i] = digits[i], digits[0]

        ans = int("".join(str(digit) for digit in digits))
        return -ans if negative else ans


def test1():
    solution = Solution()
    assert(solution.smallestNumber1(301) == 103)
    assert(solution.smallestNumber1(-7605) == -7650)


if __name__ == "__main__":
    test1()
