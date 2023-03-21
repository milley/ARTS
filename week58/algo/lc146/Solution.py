class ListNode:
    def __init__(self, k, v):
        self.key = k
        self.val = v
        self.next = None
        self.prev = None


class LRUCache:

    def __init__(self, capacity: int):
        self.capacity = capacity
        self.hkeys = {}
        self.top = ListNode(None, -1)
        self.tail = ListNode(None, -1)
        self.top.next = self.tail
        self.tail.prev = self.top

    def get(self, key: int) -> int:
        if key in self.hkeys.keys():
            cur = self.hkeys[key]
            cur.next.prev = cur.prev
            cur.prev.next = cur.next

            top_node = self.top.next
            self.top.next = cur
            cur.prev = self.top
            cur.next = top_node
            top_node.prev = cur

            return self.hkeys[key].val
        return -1

    def put(self, key: int, value: int) -> None:
        if key in self.hkeys.keys():
            cur = self.hkeys[key]
            cur.val = value

            cur.prev.next = cur.next
            cur.next.prev = cur.prev

            top_node = self.top.next
            self.top.next = cur
            cur.prev = self.top
            cur.next = top_node
            top_node.prev = cur
        else:
            cur = ListNode(key, value)
            self.hkeys[key] = cur

            top_node = self.top.next
            self.top.next = cur
            cur.prev = self.top
            cur.next = top_node
            top_node.prev = cur

            if len(self.hkeys.keys()) > self.capacity:
                self.hkeys.pop(self.tail.prev.key)

                self.tail.prev.prev.next = self.tail
                self.tail.prev = self.tail.prev.prev

    def __repr__(self):
        vals = []
        p = self.top.next
        while p.next:
            vals.append(str(p.value))
            p = p.next
        return "->".join(vals)


def test1():
    lRUCache = LRUCache(2)
    lRUCache.put(1, 1)
    lRUCache.put(2, 2)
    assert(lRUCache.get(1) == 1)
    lRUCache.put(3, 3)
    assert(lRUCache.get(2) == -1)
    lRUCache.put(4, 4)
    assert(lRUCache.get(1) == -1)
    assert(lRUCache.get(3) == 3)
    assert(lRUCache.get(4) == 4)


if __name__ == "__main__":
    test1()
