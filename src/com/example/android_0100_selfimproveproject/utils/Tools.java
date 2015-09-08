package com.example.android_0100_selfimproveproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import com.example.android_0100_selfimproveproject.Constant;

public class Tools
{
    public static Date nextDay(Date theDay)
    {
        return new Date(theDay.getTime() + 1000 * 60 * 60 * 24);
    }

    public static Date prevDay(Date theDay)
    {
        return new Date(theDay.getTime() - 1000 * 60 * 60 * 24);
    }

    public static boolean isEmpty(String str)
    {
        boolean res = false;
        if ((null == str) || (str.trim().length() == 0))
            res = true;
        return res;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean checkTime(String time)
    {
        boolean res = true;
        if (isEmpty(time))
            return true; // can be empty
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FOMMAT_PATTERN);
        try
        {
            sdf.parse(time);
        }
        catch (ParseException e)
        {
            res = false;
        }
        return res;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTime(Date time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FOMMAT_PATTERN);
        return sdf.format(time);
    }
}
