from typing import List
from operator import add, sub, mul


class Solution:
    def evalRPN(self, tokens: List[str]) -> int:
        stack = []
        if len(tokens) == 1:
            return int(tokens[0])

        for expr in tokens:
            if expr == '+':
                right = stack[-1]
                stack.pop()
                left = stack[-1]
                stack.pop()
                stack.append(int(left) + int(right))
            elif expr == '-':
                right = stack[-1]
                stack.pop()
                left = stack[-1]
                stack.pop()
                stack.append(int(left) - int(right))
            elif expr == '*':
                right = stack[-1]
                stack.pop()
                left = stack[-1]
                stack.pop()
                stack.append(int(left) * int(right))
            elif expr == '/':
                right = stack[-1]
                stack.pop()
                left = stack[-1]
                stack.pop()
                stack.append(int(left) // abs(int(right)))
            else:
                stack.append(expr)

        return stack[-1]

    def evalRPN1(self, tokens: List[str]) -> int:
        op_to_binary_fn = {
            "+": add,
            "-": sub,
            "*": mul,
            "/": lambda x, y: int(x / y),
        }

        stack = list()
        for token in tokens:
            try:
                num = int(token)
            except ValueError:
                num2 = stack.pop()
                num1 = stack.pop()
                num = op_to_binary_fn[token](num1, num2)
            finally:
                stack.append(num)
        return stack[0]

    def evalRPN2(self, tokens: List[str]) -> int:
        s = []
        for token in tokens:
            if token not in '+-*/':
                s.append(int(token))
            else:
                n2 = s.pop()
                n1 = s.pop()
                if token == '+':
                    n = n1 + n2
                elif token == '-':
                    n = n1 - n2
                elif token == '*':
                    n = n1 * n2
                else:
                    n = int(n1 / n2)
                s.append(n)
        return s[-1]


def test1():
    solution = Solution()
    tokens = ["2", "1", "+", "3", "*"]
    assert(solution.evalRPN2(tokens) == 9)


def test2():
    solution = Solution()
    tokens = ["4", "13", "5", "/", "+"]
    assert(solution.evalRPN2(tokens) == 6)


def test3():
    solution = Solution()
    tokens = ["10", "6", "9", "3", "+", "-11",
              "*", "/", "*", "17", "+", "5", "+"]
    assert(solution.evalRPN2(tokens) == 22)


if __name__ == '__main__':
    test1()
    test2()
    test3()
