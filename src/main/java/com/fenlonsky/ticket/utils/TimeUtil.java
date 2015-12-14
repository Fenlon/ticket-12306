package com.fenlonsky.ticket.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by fenlon on 15-12-14.
 */
public class TimeUtil {
    public static String format(Long timestamp) {
        String time;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = sdf.format(timestamp);
        return time;
    }

    public static String addZero(String date) {
        return date + " " + "00:00:00";
    }


    public static Long format(String date) {
        Timestamp ts = Timestamp.valueOf(date);
        return ts.getTime();
    }

    public static String shortStr(String time) {
        time = time.substring(0, time.lastIndexOf(':'));
        return time;
    }

    /* public static void main(String[] args) {

       *//*  System.out.println(appenSuffix(format(1449230160000L)));
        System.out.println(appenSuffix(format(add5MIn(1449230160000L))));*//*

//        Long a = format("2015-12-04 19:56:00");
        String a = removeSuffix("2015-12-04T00:00:00.0Z");
        System.out.println(a.toString());
    }
*/
    public static Long add1day(Long timestamp) {
        timestamp += 24 * 60 * 60 * 1000;
        return timestamp;
    }
}