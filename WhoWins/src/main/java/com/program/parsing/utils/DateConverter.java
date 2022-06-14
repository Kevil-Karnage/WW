package com.program.parsing.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class DateConverter {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm");


    public static Date getCurrentDate() {
        Date date = new Date(new java.util.Date().getTime());
        return date;
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date stringToDate(String s) throws ParseException {
        return new Date(dateFormat.parse(s).getTime());
    }

    public static Date stringToDateTime(String s) throws ParseException {
        Date date = new Date(dateTimeFormat.parse(s).getTime());
        return date;
    }

    public static String parseDateStringToFormat(String date) {
        String[] parts = date.split(" ");

        String day = parseDay(parts[0]);
        Month month = Month.get(parts[2]);
        int year = Integer.parseInt(parts[3]);
        return year + "-" + month.number + "-" + day;
    }

    public static Date convertToDate(String date) throws ParseException {
        String stringDate = parseDateStringToFormat(date);
        return DateConverter.stringToDate(stringDate);
    }

    public static Date convertToDate(String date, String time) throws ParseException {
        String stringDate = parseDateStringToFormat(date);
        return DateConverter.stringToDateTime(stringDate + " - " + time);
    }

    public static String parseDay(String str) {
        char[] numberInCharArray = Arrays.copyOf(str.toCharArray(), str.length() - 2);
        String day = new String(numberInCharArray);
        if (day.length() == 1) return "0" + day;
        return day;
    }
}

enum Month {
    NONE("NONE", ""),
    JANUARY("January", "01"),
    FEBRUARY("February","02"),
    MARCH("March","03"),
    APRIL("April","04"),
    MAY("May","05"),
    JUNE("June","06"),
    JULY("July","07"),
    AUGUST("August","08"),
    SEPTEMBER("September","09"),
    OCTOBER("October","10"),
    NOVEMBER("November","11"),
    DECEMBER("December","12");


    final String name;
    final String number;

    Month(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public static Month get(String str) {
        for (Month month :
                Month.values()) {
            if (month.name.equals(str)) {
                return month;
            }
        }
        return NONE;
    }
}
