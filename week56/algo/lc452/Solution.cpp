#include <vector>
#include <algorithm>
#include <cassert>
using namespace std;

class Solution {
public:
    int findMinArrowShots(vector<vector<int>>& points) {
        if (points.size() == 0) {
            return 0;
        }

        sort(points.begin(), points.end(), [](const vector<int>& a, const vector<int>& b) { return a[1] < b[1]; });
        int pos = points[0][1];
        int ans = 1;
        for (auto& balloon : points) {
            if (balloon[0] > pos) {
                pos = balloon[1];
                ans++;
            }
        }

        return ans;
    }
};

void test1() {
    Solution solution;
    vector<vector<int>> points = {{10,16},{2,8},{1,6},{7,12}};
    assert(solution.findMinArrowShots(points) == 2);
}

void test2() {
    Solution solution;
    vector<vector<int>> points = {{1,2},{3,4},{5,6},{7,8}};
    assert(solution.findMinArrowShots(points) == 4);
}

void test3() {
    Solution solution;
    vector<vector<int>> points = {{1,2},{2,3},{3,4},{4,5}};
    assert(solution.findMinArrowShots(points) == 2);
}

int main() {
    test1();
    test2();
    test3();
}