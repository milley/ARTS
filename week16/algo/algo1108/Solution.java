package com.milley.defanginganipaddress;


/**
 * @author milley
 * Defanging an IP Address
 */
public class Solution {
    public String defangIPAddress(String address) {
        char[] IPArray = address.toCharArray();
        char[] defangIPArray = new char[address.length() + 6];
        int index = 0;
        for (int i = 0; i < IPArray.length; i++) {
            if (IPArray[i] != '.') {
                defangIPArray[index] = IPArray[i];
                index++;
            } else {
                defangIPArray[index] = '[';
                index++;
                defangIPArray[index] = '.';
                index++;
                defangIPArray[index] = ']';
                index++;
            }
        }
        return String.valueOf(defangIPArray);
    }

    public static void main(String[] args) {
        System.out.println(new Solution().defangIPAddress("255.100.50.0"));
    }
}
