from typing import Optional

# Definition for singly-linked list.
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next
class Solution:
    def rotateRight(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:
        if k == 0 or not head or not head.next:
            return head
        
        cur = head
        n = 0
        while cur:
            n += 1
            cur = cur.next
        
        if n - k % n == n:
            return head
        
        n = n - k % n
        
        
        
        cur = head
        t = 0
        
        left_head = None
        while cur:
            t += 1
            if t == n:
                left_head = cur.next
                cur.next = None
                break
            
            cur = cur.next
        
        new_head = left_head
        while left_head and left_head.next:
            left_head = left_head.next
        left_head.next = head
        
        return new_head

def test1():
    solution = Solution()
    
    node1 = ListNode(1)
    node2 = ListNode(2)
    node3 = ListNode(3)
    node4 = ListNode(4)
    node5 = ListNode(5)
    
    node1.next = node2
    node2.next = node3
    node3.next = node4
    node4.next = node5
    
    out = solution.rotateRight(node1, 2)
    #print(out.val)
    assert(out.val == 4)
def test2():
    solution = Solution()
    
    node1 = ListNode(1)
    node2 = ListNode(2)
    node1.next = node2
    
    out = solution.rotateRight(node1, 2)
    print(out.val)
    
if __name__ == '__main__':
    #test1()
    test2()