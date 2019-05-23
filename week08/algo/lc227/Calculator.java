package com.milley.structure.stack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Calculator {
    private StackBased<Integer> numStack;
    private StackBased<Character> operateStack;
    private Map<Character, Integer> levelMap;

    public Calculator() {
        numStack = new StackBased<>();
        operateStack = new StackBased<>();

        levelMap = new HashMap<>();
        levelMap.put('+',1);
        levelMap.put('-',1);
        levelMap.put('*',2);
        levelMap.put('/',2);
    }

    public int calculate(String str) {
        int result = 0;
        if (str == null || str.length() == 0) {
            return 0;
        }
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch >= '0' && ch <= '9') {
                num = num * 10 + str.charAt(i) - '0';
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == ' ') {
                if (ch == ' ') {
                    continue;
                }
                numStack.push(num);
                num = 0;

                if (operateStack.peek() == null) {
                    operateStack.push(ch);
                    continue;
                }
                char topOperCh = operateStack.peek();
                if (levelMap.get(ch) > levelMap.get(topOperCh)) {
                    operateStack.push(ch);
                } else {
                    while (operateStack.peek() != null && levelMap.get(ch)  <= levelMap.get(operateStack.peek())) {
                        int num1 = numStack.pop();
                        int num2 = numStack.pop();
                        char topCh = operateStack.pop();

                        int tmpResult = getResultByOperator(num1, num2, topCh);
                        numStack.push(tmpResult);
                    }
                    operateStack.push(ch);
                }
            } else {
                throw new IllegalArgumentException("Arguments error");
            }
        }

        numStack.push(num);

        while (operateStack.peek() != null) {
            char ch = operateStack.pop();
            int num1 = numStack.pop();
            int num2 = numStack.pop();
            numStack.push(getResultByOperator(num1, num2, ch));
        }

        while (numStack.peek() != null) {
            result += numStack.pop();
        }
        return result;
    }

    private int getResultByOperator(int num1, int num2, char ch) {
        int tmpResult = 0;
        switch (ch) {
            case '+':
                 tmpResult = num1 + num2;
                 break;
             case '-':
                 tmpResult = num2 - num1;
                 break;
             case '*':
                 tmpResult = num1 * num2;
                 break;
             case '/':
                 if (num1 == 0) {
                     throw new IllegalArgumentException("operator error");
                 }
                 tmpResult = num2 / num1;
                 break;
             default:
                 break;
        }
        return tmpResult;
    }

    public class StackBased<T> {
        private Node head = null;
            public void push(T ch) {
            Node newNode = new Node(ch, null);
            if (head == null) {
                head = newNode;
            } else {
                newNode.next = head;
                head = newNode;
            }
        }

        public T pop() {
            if (head == null) {
                return null;
            } else {
                T ch = head.data;
                head = head.next;
                return ch;
            }
        }

        public T peek() {
            if (head == null) {
                return null;
            } else {
                T ch = head.data;
                return ch;
            }
        }

        private class Node {
            private T data;
            private Node next;

            public T getData() {
                return data;
            }

            public Node getNext() {
                return next;
            }

            public Node(T data, Node next) {
                this.data = data;
                this.next = next;
            }
        }
    }


    public static void main(String[] args) throws IOException {
        Calculator calculator = new Calculator();
        System.out.println(calculator.calculate("1+2*5/3+6/4*2"));
        System.out.println(calculator.calculate("3+5/2"));
        System.out.println(calculator.calculate("2+3-4"));
        System.out.println(calculator.calculate("3+2*2"));
        System.out.println(calculator.calculate("3/2 "));
        System.out.println(calculator.calculate("100000000/1/2/3/4/5/6/7/8/9/10"));


        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\test1.txt"))));

        StringBuilder builder = new StringBuilder();
        int charsRead = -1;
        char[] chars = new char[100];
        do{
            charsRead = in.read(chars,0,chars.length);
            //if we have valid chars, append them to end of string.
            if(charsRead>0) {
                builder.append(chars,0,charsRead);
            }
        }while(charsRead>0);
        String stringReadFromReader = builder.toString();
        System.out.println(calculator.calculate(stringReadFromReader));
    }
}
