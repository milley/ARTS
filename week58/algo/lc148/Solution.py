from typing import Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


class Solution:
    def sortList(self, head: Optional[ListNode]) -> Optional[ListNode]:
        return self.merge_sorted(head, None)

    def merge_sorted(self, head: Optional[ListNode], tail: Optional[ListNode]) -> Optional[ListNode]:
        if head == None:
            return head
        if head.next == tail:
            head.next = None
            return head
        slow, fast = head, head
        while fast != tail:
            slow = slow.next
            fast = fast.next
            if fast != tail:
                fast = fast.next

        mid = slow
        list1 = self.merge_sorted(head, mid)
        list2 = self.merge_sorted(mid, tail)

        return self.merge(list1, list2)

    def merge(self, head1: Optional[ListNode], head2: Optional[ListNode]) -> Optional[ListNode]:
        dummyHead = ListNode(0)
        temp, temp1, temp2 = dummyHead, head1, head2
        while temp1 and temp2:
            if temp1.val <= temp2.val:
                temp.next = temp1
                temp1 = temp1.next
            else:
                temp.next = temp2
                temp2 = temp2.next
            temp = temp.next
        if temp1:
            temp.next = temp1
        if temp2:
            temp.next = temp2
        return dummyHead.next


def print_all(head: ListNode) -> str:
    nums = []
    while head:
        nums.append(head.val)
        head = head.next
    out = "->".join(str(num) for num in nums)
    print(out)
    return out


def test1():
    solution = Solution()
    node4 = ListNode(4)
    node2 = ListNode(2)
    node4.next = node2
    node1 = ListNode(1)
    node2.next = node1
    node3 = ListNode(3)
    node1.next = node3

    input = node4
    out = solution.sortList(input)
    print_all(out)


if __name__ == "__main__":
    test1()
