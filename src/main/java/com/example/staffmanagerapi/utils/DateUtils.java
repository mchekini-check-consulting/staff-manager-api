package com.example.staffmanagerapi.utils;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static YearMonth parseMonthYear(String input) {
        if (input == null ){
            return null;
        }
        return YearMonth.parse(input, DateTimeFormatter.ofPattern("MM/yyyy"));
    }

}
