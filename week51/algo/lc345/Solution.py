class Solution:
    def reverseVowels(self, s: str) -> str:
        i, j = 0, len(s) - 1
        l = list(s)
        vowels = "aeiou"
        while i < j:
            if vowels.find(s[i].lower()) == -1:
                i += 1
                continue
            if vowels.find(s[j].lower()) == -1:
                j -= 1
                continue
            if vowels.find(s[i].lower()) != -1 and vowels.find(s[j].lower()) != -1:
                l[i], l[j] = l[j], l[i]
                i, j = i + 1, j - 1
        return "".join(l)


def main():
    solution = Solution()
    #s = "hello"
    s = "leetcode"
    ret_str = solution.reverseVowels(s)
    print(ret_str)


if __name__ == '__main__':
    main()
