class Solution:
    def frequencySort(self, s: str) -> str:
        d = {}
        for ch in s:
            if ch in d.keys():
                d[ch] += 1
            else:
                d[ch] = 1

        res = {key: val for key, val in sorted(
            d.items(), key=lambda ele: ele[1], reverse=True)}
        out = ""
        for k, v in res.items():
            out += k * v
        return out


def test1():
    solution = Solution()
    #s = "tree"
    #s = "cccaaa"
    s = "Aabb"
    print(solution.frequencySort(s))


if __name__ == "__main__":
    test1()
