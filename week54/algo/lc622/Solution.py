class MyCircularQueue:

    def __init__(self, k: int):
        self.head = 0
        self.tail = 0
        self.items = [0] * (k + 1)
        self.n = k + 1

    def enQueue(self, value: int) -> bool:
        if self.isFull():
            return False
        self.items[self.tail] = value
        self.tail = (self.tail + 1) % self.n
        return True

    def deQueue(self) -> bool:
        if self.isEmpty():
            return False
        self.head = (self.head + 1) % self.n
        return True

    def Front(self) -> int:
        return -1 if self.isEmpty() else self.items[self.head]

    def Rear(self) -> int:
        return -1 if self.isEmpty() else self.items[(self.tail - 1) % self.n]

    def isEmpty(self) -> bool:
        return self.head == self.tail

    def isFull(self) -> bool:
        return (self.tail + 1) % self.n == self.head


def test1():
    obj = MyCircularQueue(3)
    assert(obj.enQueue(1) == True)
    assert(obj.enQueue(2) == True)
    assert(obj.enQueue(3) == True)
    print(obj.items)
    assert(obj.enQueue(4) == False)
    assert(obj.isFull() == True)
    assert(obj.deQueue() == True)
    assert(obj.enQueue(4) == True)
    print(obj.items)
    assert(obj.Rear() == 4)
    assert(obj.Front() == 2)


# Your MyCircularQueue object will be instantiated and called as such:
if __name__ == '__main__':
    test1()
