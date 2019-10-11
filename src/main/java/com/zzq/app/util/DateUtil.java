package com.zzq.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final Locale LOCALE = Locale.CHINA;

    public static String format(Date date, String s){
        return new SimpleDateFormat(s, LOCALE).format(date);
    }

    public static Date parse(String date, String s){
        try {
            return new SimpleDateFormat(s, LOCALE).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
