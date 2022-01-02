package com.milley.integertoroman;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    private static Map<Integer, String> map1 = Stream.of(new Object[][] {
            {1, "I"},
            {4, "IV"},
            {5, "V"},
            {9, "IX"},
            {10, "X"},
            {40, "XL"},
            {50, "L"},
            {90, "XC"},
            {100, "C"},
            {400, "CD"},
            {500, "D"},
            {900, "CM"},
            {1000, "M"}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

    private static String thousand(int num) {
        if (num == 1000) {
            return map1.get(num);
        }

        return gen_str_by_chars(num / 1000, 'M');
    }

    private static String hundred(int num) {
        if (num == 100 || num == 400 || num == 500 || num == 900) {
            return map1.get(num);
        }
        if (num % 100 == 0) {
            return num > 500 ? "D" + gen_str_by_chars((num - 500) / 100, 'C') : gen_str_by_chars(num / 100, 'C');
        }

        return num > 500 ? "D" + gen_str_by_chars(num - 500, 'C') : gen_str_by_chars(num, 'C');
    }

    private static String ten(int num) {
        if (num == 10 || num == 40 || num == 50 || num == 90) {
            return map1.get(num);
        }
        if (num % 10 == 0) {
            return num > 50 ? "L" + gen_str_by_chars((num - 50) / 10, 'X') : gen_str_by_chars(num / 10 , 'X');
        }

        return num > 50 ? "L" + gen_str_by_chars((num - 50), 'X') : gen_str_by_chars(num , 'X');
    }

    private static String one(int num) {
        if (num == 9 || num == 5 || num == 4 || num == 1) {
            return map1.get(num);
        }

        return num > 5 ? "V" + gen_str_by_chars(num - 5, 'I') : gen_str_by_chars(num, 'I');
    }

    private static String gen_str_by_chars(int num, char c) {
        StringBuffer sb = new StringBuffer();
        while (num > 0) {
            sb.append(c);
            num--;
        }

        return sb.toString();
    }

    public static String intToRoman(int num) {

        return thousand(num / 1000 * 1000)
                + hundred(num % 1000 / 100 * 100)
                + ten(num % 1000 % 100 / 10 * 10)
                + one(num % 10);
    }

    /// Solution2
    private static String[] thousands = {"", "M", "MM", "MMM"};
    private static String[] hundreds  = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
    private static String[] tens      = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    private static String[] ones      = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

    public static String intToRoman2(int num) {
        StringBuffer roman = new StringBuffer();
        roman.append(thousands[num / 1000]);
        roman.append(hundreds[num % 1000 / 100]);
        roman.append(tens[num % 100 / 10]);
        roman.append(ones[num % 10]);
        return roman.toString();
    }

    /// Solution3
    private static int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public static String intToRoman3(int num) {
        StringBuffer roman = new StringBuffer();
        for (int i = 0; i < values.length; ++i) {
            int value = values[i];
            String symbol = symbols[i];
            while (num >= value) {
                num -= value;
                roman.append(symbol);
            }
            if (num == 0) {
                break;
            }
        }
        return roman.toString();
    }

    public static void main(String[] args) {
        System.out.println(intToRoman(20));
        System.out.println(intToRoman2(20));
        System.out.println(intToRoman3(20));
    }
}
