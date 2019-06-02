package com.milley.implementqueuewithstacks;

import java.util.Stack;

public class MyQueue {
    private Stack<Integer> left;
    private Stack<Integer> right;

    public MyQueue() {
        left = new Stack<>();
        right = new Stack<>();
    }

    public void push(int x) {
        if (right.empty()) {
            right.push(x);
        } else {
            while (!right.empty()) {
                int topRight = right.pop();
                left.push(topRight);
            }
            left.push(x);
        }

        while (!left.empty())  {
            int topLeft = left.pop();
            right.push(topLeft);
        }
    }

    public int pop() {
        assert(!right.empty());
        return right.pop();
    }

    public int peek() {
        assert(!right.empty());
        return right.peek();
    }

    public boolean empty() {
        return right.empty();
    }
}
