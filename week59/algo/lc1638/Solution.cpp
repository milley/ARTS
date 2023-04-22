#include <vector>
#include <string>
#include <iostream>

#include <cassert>

using namespace std;

class Solution {
public:
    int countSubstrings(string s, string t) {
        return countSubstrings2(s, t);
    }

    int countSubstrings1(string s, string t) {
        int m = s.size(), n = t.size();
        int ans = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diff = 0;
                for (int k = 0; i + k < m && j + k < n; k++) {
                    diff += s[i + k] == t[j + k] ? 0 : 1;
                    if (diff > 1) {
                        break;
                    } else if (diff == 1) {
                        ans++;
                    }
                }
            }
        }

        return ans;
    }

    int countSubstrings2(string s, string t) {
        int m = s.size(), n = t.size();
        vector<vector<int>> dpl(m + 1, vector<int>(n + 1));
        vector<vector<int>> dpr(m + 1, vector<int>(n + 1));

        //print(dpl); print(dpr);
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                dpl[i + 1][j + 1] = s[i] == t[j] ? (dpl[i][j] + 1) : 0;
            }
        }
        for (int i = m - 1; i >= 0; --i) {
            for (int j = n - 1; j >= 0; --j) {
                dpr[i][j] = s[i] == t[j] ? (dpr[i + 1][j + 1] + 1) : 0;
            }
        }

        int ans = 0;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (s[i] != t[j]) {
                    ans += (dpl[i][j] + 1) * (dpr[i + 1][j + 1] + 1);
                }
            }
        }

        return ans;
    }

private:
    void print(vector<vector<int>>& v) {
        cout << "[" << endl;
        for (vector<int>& vv : v) {
            cout << "[";
            for (int& x : vv) {
                cout << x << " ";
            }
            cout << "]" << endl;
        }

        cout << "]" << endl;
    }
};

int test(Solution& solution, string s, string t) {
    return solution.countSubstrings(s, t);
}

int main() {
    Solution solution;

    string s = "aba";
    string t = "baba";
    assert(test(solution, s, t) == 6);

    s = "ab";
    t = "bb";
    assert(test(solution, s, t) == 3);

    s = "a";
    t = "a";
    assert(test(solution, s, t) == 0);

    s = "abe";
    t = "bbc";
    assert(test(solution, s, t) == 10);

    return 0;
}