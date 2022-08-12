class Solution:
    def decodeString(self, s: str) -> str:
        stack = []

        i = 0
        while i < len(s):
            ch = s[i]
            if ch != ']':
                if '0' <= ch and ch <= '9':
                    num_str = ''
                    while '0' <= s[i] and s[i] <= '9':
                        num_str = num_str + s[i]
                        i += 1
                    print(num_str)
                    stack.append(num_str)
                else:
                    i += 1
                    stack.append(ch)
            else:
                i += 1

                sub = []
                while stack[-1] != '[':
                    sub.append(stack.pop())
                sub.reverse()

                #print(sub)

                # pop [
                stack.pop()
                # pop number
                times = int(stack.pop())
                sub_str = "".join(sub) * times
                stack.append(sub_str)

        return "".join(stack)


def test1():
    solution = Solution()
    str = "3[a]2[bc]"
    print(solution.decodeString(str))
    assert(solution.decodeString(str) == "aaabcbc")


def test2():
    solution = Solution()
    str = "3[a2[c]]"
    print(solution.decodeString(str))
    assert(solution.decodeString(str) == "accaccacc")


def test3():
    solution = Solution()
    str = "2[abc]3[cd]ef"
    print(solution.decodeString(str))
    assert(solution.decodeString(str) == "abcabccdcdcdef")


def test4():
    solution = Solution()
    str = "abc3[cd]xyz"
    print(solution.decodeString(str))
    assert(solution.decodeString(str) == "abccdcdcdxyz")


def test5():
    solution = Solution()
    str = "10[leetcode]"
    print(solution.decodeString(str))


if __name__ == "__main__":
    test1()
    test2()
    test3()
    test4()
    test5()
