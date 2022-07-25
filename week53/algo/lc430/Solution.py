class Node:
    def __init__(self, val, prev, next, child):
        self.val = val
        self.prev = prev
        self.next = next
        self.child = child


class Solution:
    def flatten(self, head: 'Node') -> 'Node':
        save_root = []
        root = head
        while head and (head.next or head.child):
            if head.child is None:
                head = head.next
            elif head.next:
                head.next.prev = None
                save_root.append(head.next)
                head.child.prev = head
                head.next = head.child
                head.child = None
                head = head.next
            else:
                while head.child:
                    if head.next:
                        save_root.append(head.next)
                        head.next.prev = None
                    head.child.prev = head
                    head.next = head.child
                    head.child = None
                    head = head.next

        while len(save_root) > 0:
            tmp = save_root.pop()
            tmp.prev = head
            head.next = tmp
            head.child = None
            while head and head.next:
                head = head.next

        return root


def test1():
    solution = Solution()
    node1 = Node(1, None, None, None)
    node2 = Node(2, None, None, None)
    node3 = Node(3, None, None, None)

    node1.next = node2
    node2.prev = node1
    node1.child = node3

    root = solution.flatten(node1)
    assert(root.val == 1)
    root = root.next
    assert(root.val == 3)
    root = root.next
    assert(root.val == 2)


def test2():
    solution = Solution()
    node1 = Node(1, None, None, None)
    node2 = Node(2, None, None, None)
    node3 = Node(3, None, None, None)
    node4 = Node(4, None, None, None)
    node5 = Node(5, None, None, None)
    node6 = Node(6, None, None, None)
    node1.next = node2
    node2.prev = node1
    node2.next = node3
    node3.prev = node2
    node3.next = node4
    node4.prev = node3
    node4.next = node5
    node5.prev = node4
    node5.next = node6
    node6.prev = node5

    node7 = Node(7, None, None, None)
    node8 = Node(8, None, None, None)
    node9 = Node(9, None, None, None)
    node10 = Node(10, None, None, None)
    node7.next = node8
    node8.prev = node7
    node8.next = node9
    node9.prev = node8
    node9.next = node10
    node10.prev = node9

    node3.child = node7

    node11 = Node(11, None, None, None)
    node12 = Node(12, None, None, None)
    node11.next = node12
    node12.prev = node11

    node8.child = node11

    root = solution.flatten(node1)
    for _ in range(12):
        print(root.val)
        root = root.next


def test3():
    solution = Solution()
    node1 = Node(1, None, None, None)
    node2 = Node(2, None, None, None)
    node3 = Node(3, None, None, None)
    node1.child = node2
    node2.child = node3

    root = solution.flatten(node1)
    assert(root.val == 1)
    root = root.next
    assert(root.val == 2)
    root = root.next
    assert(root.val == 3)


def test4():
    solution = Solution()
    node1 = Node(1, None, None, None)
    node2 = Node(2, None, None, None)
    node3 = Node(3, None, None, None)
    node4 = Node(4, None, None, None)
    node5 = Node(5, None, None, None)
    node6 = Node(6, None, None, None)
    node1.next = node2
    node2.prev = node1
    node2.next = node3
    node3.prev = node2
    node3.next = node4
    node4.prev = node3
    node4.next = node5
    node5.prev = node4
    node5.next = node6
    node6.prev = node5

    node7 = Node(7, None, None, None)
    node8 = Node(8, None, None, None)
    node7.next = node8
    node8.prev = node7

    node3.child = node7

    node11 = Node(11, None, None, None)
    node12 = Node(12, None, None, None)
    node11.next = node12
    node12.prev = node11

    node8.child = node11

    root = solution.flatten(node1)
    for _ in range(9):
        print(root.val)
        root = root.next


def test5():
    solution = Solution()
    node1 = Node(1, None, None, None)
    node2 = Node(2, None, None, None)
    node4 = Node(4, None, None, None)
    node1.next = node2
    node2.prev = node1
    node2.next = node4
    node4.prev = node2

    node3 = Node(3, None, None, None)
    node2.child = node3

    node5 = Node(5, None, None, None)
    node3.child = node5

    root = solution.flatten(node1)
    assert(root.val == 1)
    root = root.next
    assert(root.val == 2)
    root = root.next
    assert(root.val == 3)
    root = root.next
    assert(root.val == 5)
    root = root.next
    assert(root.val == 4)


def test6():
    solution = Solution()
    node1 = Node(1, None, None, None)
    node2 = Node(2, None, None, None)
    node4 = Node(4, None, None, None)

    node2.next = node4
    node4.prev = node2

    node1.child = node2

    node3 = Node(3, None, None, None)
    node2.child = node3

    node5 = Node(5, None, None, None)
    node3.child = node5

    root = solution.flatten(node1)
    assert(root.val == 1)
    root = root.next
    assert(root.val == 2)
    root = root.next
    assert(root.val == 3)
    root = root.next
    assert(root.val == 5)
    root = root.next
    assert(root.val == 4)


if __name__ == '__main__':
    test1()
    test2()
    test3()
    test4()
    test5()
    test6()
