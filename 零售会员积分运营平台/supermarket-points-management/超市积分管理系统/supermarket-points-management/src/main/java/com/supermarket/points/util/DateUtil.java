package com.supermarket.points.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatDefault(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }
}