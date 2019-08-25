package com.milley.plusone;

import java.util.Stack;

/**
 * @author milley
 */
public class Solution {
    public int[] plusOne(int[] digits) {
        int len = digits.length;
        long number = 0;
        while (len > 0) {
            number += digits[len - 1] * Math.pow(10, digits.length - len);
            len--;
        }

        long numberPlusOne = number + 1;
        Stack<Long> stack = new Stack<>();
        while (numberPlusOne > 0) {
            stack.push(numberPlusOne % 10);
            numberPlusOne /= 10;
        }

        int[] result = new int[stack.size()];
        int idx = 0;
        while (!stack.isEmpty()) {
            result[idx++] = stack.pop().intValue();
        }
        return result;
    }

    public int[] plusOnePlanB(int[] digits) {
        int carryBit = 1;
        int len = digits.length;
        Stack<Integer> stack = new Stack<>();
        while (len > 0) {
            if (digits[len - 1] + carryBit > 9) {
                carryBit = 1;
                stack.push(0);
            } else {
                stack.push(digits[len - 1] + carryBit);
                carryBit = 0;
            }
            len--;
        }

        int[] result;

        if (carryBit == 1) {
            result = new int[digits.length + 1];
            int idx = 1;
            result[0] = carryBit;
            while (!stack.isEmpty()) {
                result[idx++] = stack.pop();
            }
        } else {
            result = new int[digits.length];
            int idx = 0;
            while (!stack.isEmpty()) {
                result[idx++] = stack.pop();
            }
        }
        return result;
    }

    public int[] plusOnePlanA(int[] digits) {

        int n = digits.length;
        for(int i=n-1; i>=0; i--) {
            if(digits[i] < 9) {
                digits[i]++;
                return digits;
            }

            digits[i] = 0;
        }

        int[] newNumber = new int [n+1];
        newNumber[0] = 1;

        return newNumber;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3};
        //int[] nums = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        //int[] nums = new int[]{6,1,4,5,3,9,0,1,9,5,1,8,6,7,0,5,5,4,3};
        //int[] plusOne = new Solution().plusOne(nums);
        int[] plusOne = new Solution().plusOnePlanB(nums);
        for (int i : plusOne) {
            System.out.print(i + ",");
        }
    }
}
