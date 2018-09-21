package com.choucheng.dongzhibot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chony on 2016/10/20.
 */

public class TimeUtil {
    private final static int TIME_UNIT_SIXTY = 60;
    public final static long TIME_SEC = 1000;
    public final static long TIME_MIN = 60 * TIME_SEC;
    public final static long TIME_HOUR = 60 * TIME_MIN;
    public final static long TIME_DAY = 24 * TIME_HOUR;
    public final static long TIME_WEEK = 7 * TIME_DAY;
    public final static long TIME_MONTH = 30 * TIME_DAY;

    public static String getFormatDate(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String t=format.format(new Date(time));
        return t;
    }

    public static String getFormatDate(long time, String format){
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        String t=formatDate.format(new Date(time));
        return t;
    }
}
