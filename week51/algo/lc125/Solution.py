class Solution:
    def isPalindrome(self, s: str) -> bool:
        n = len(s)
        i = 0
        j = n - 1
        while i < j:
            if not s[i].isalnum():
                i += 1
                continue
            if not s[j].isalnum():
                j -= 1
                continue
            if s[i].lower() == s[j].lower():
                i += 1
                j -= 1
            else:
                #print("i={}, j={}".format(i, j))
                return False
        return True


def main():
    solution = Solution()
    #s = "A man, a plan, a canal: Panama"
    s = "race a car"
    b = solution.isPalindrome(s)
    assert(b == False)


if __name__ == '__main__':
    main()
