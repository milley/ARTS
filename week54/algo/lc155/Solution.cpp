#include <stack>
#include <vector>
#include <string>
#include <cassert>
using namespace std;

class Solution {
public:
    int evalRPN(vector<string>& tokens) {
        stack<int> s;
        for (const string& token : tokens) {
            if (isdigit(token.back())) {
                s.push(stoi(token));
            } else {
                int n2 = s.top(); s.pop();
                int n1 = s.top(); s.pop();
                int n = 0;
                switch (token[0]) {
                    case '+': n = n1 + n2; break;
                    case '-': n = n1 - n2; break;
                    case '*': n = n1 * n2; break;
                    case '/': n = n1 / n2; break;
                }
                s.push(n);
            }
        }
        return s.top();
    }
};

void test1() {
    Solution solution;
    vector<string> vec{"2","1","+","3","*"};
    assert(solution.evalRPN(vec) == 9);
}

void test2() {
    Solution solution;
    vector<string> vec{"4","13","5","/","+"};
    assert(solution.evalRPN(vec) == 6);
}

void test3() {
    Solution solution;
    vector<string> vec{"10","6","9","3","+","-11","*","/","*","17","+","5","+"};
    assert(solution.evalRPN(vec) == 22);
}

int main() {
    test1();
    test2();
    test3();

    return 0;
}