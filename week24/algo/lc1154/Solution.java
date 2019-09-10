package com.milley.dayoftheyear;

public class Solution {
    public static int dayOfYear(String date) {
        char[] dateArray = date.toCharArray();
        int totalDays = 0;
        int year, month, day;

        int[] DAYS_OF_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        year = 1000 * (dateArray[0] - '0') + 100 * (dateArray[1] - '0') +
                10 * (dateArray[2] - '0') + (dateArray[3] - '0');
        month = 10 * (dateArray[5] - '0') + (dateArray[6] - '0');
        day = 10 * (dateArray[8] - '0') + (dateArray[9] - '0');

        boolean isLeafYear = (year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0);
        if (month > 2 && isLeafYear) {
            totalDays += 1;
        }
        while (month > 1) {
            totalDays += DAYS_OF_MONTH[month - 2];
            month--;
        }
        totalDays += day;

        return totalDays;
    }

    public static void main(String[] args) {
        System.out.println(dayOfYear("2019-01-09"));
        System.out.println(dayOfYear("2019-02-10"));
        System.out.println(dayOfYear("2003-03-01"));
        System.out.println(dayOfYear("2004-03-01"));
    }
}
