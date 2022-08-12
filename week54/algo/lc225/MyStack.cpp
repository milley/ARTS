#include <queue>
#include <cassert>
using namespace std;

class MyStack {
public:
    queue<int> q;

    MyStack() {

    }
    
    void push(int x) {
        int n = q.size();
        q.push(x);
        for (int i = 0; i < n; ++i) {
            q.push(q.front());
            q.pop();
        }
    }
    
    int pop() {
        int r = q.front();
        q.pop();
        return r;
    }
    
    int top() {
        return q.front();
    }
    
    bool empty() {
        return q.empty();
    }
};

int main() {
    MyStack* obj = new MyStack();
    obj->push(1);
    obj->push(2);
    assert(obj->pop() == 2);
    assert(obj->top() == 1);
    assert(obj->pop() == 1);
    assert(obj->empty());
}