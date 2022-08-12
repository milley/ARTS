from collections import deque


class MyStack:

    def __init__(self):
        self.left = deque()
        self.right = deque()

    def push(self, x: int) -> None:
        self.right.append(x)
        while self.left:
            self.right.append(self.left.popleft())
        self.left, self.right = self.right, self.left

    def pop(self) -> int:
        return self.left.popleft()

    def top(self) -> int:
        return self.left[0]

    def empty(self) -> bool:
        return not self.left


def test1():
    myStack = MyStack()
    myStack.push(1)
    myStack.push(2)
    assert(myStack.top() == 2)  # 返回 2
    assert(myStack.pop() == 2)  # 返回 2
    assert(myStack.empty() == False)  # 返回 False
    assert(myStack.pop() == 1)
    assert(myStack.empty() == True)


if __name__ == "__main__":
    test1()
