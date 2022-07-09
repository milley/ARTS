# Definition for singly-linked list.
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        pre = None
        node = head

        while node is not None:
            next_node = node.next
            node.next = pre
            pre = node
            node = next_node

        return pre

    def isPalindrome(self, head: ListNode) -> bool:
        if head is None:
            return True

        # 找到前半部分链表的尾节点并反转后半部分链表
        start = head
        slow, fast = head, head

        while fast.next is not None and fast.next.next is not None:
            slow = slow.next
            fast = fast.next.next

        first_half_end = slow
        second_half_start = self.reverseList(slow.next)

        # 判断是否回文
        result = True
        first_position = head
        second_position = second_half_start
        while result and second_position is not None:
            if first_position.val != second_position.val:
                result = False
            first_position = first_position.next
            second_position = second_position.next

        # 还原链表并返回结果
        first_half_end.next = self.reverseList(second_half_start)
        return result


if __name__ == '__main__':
    solution = Solution()

    node = ListNode(1)
    node2 = ListNode(2)
    node.next = node2
    node2_1 = ListNode(2)
    node2.next = node2_1
    node1_1 = ListNode(1)
    node2_1.next = node1_1
    print(solution.isPalindrome(node))
