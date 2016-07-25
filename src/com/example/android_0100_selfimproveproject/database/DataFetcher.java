package com.example.android_0100_selfimproveproject.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.android_0100_selfimproveproject.Constant;
import com.example.android_0100_selfimproveproject.service.Agenda;
import com.example.android_0100_selfimproveproject.service.Backlog;
import com.example.android_0100_selfimproveproject.service.Scheme;
import com.example.android_0100_selfimproveproject.service.Target;
import com.example.android_0100_selfimproveproject.utils.Tools;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataFetcher
{
    @SuppressLint("SimpleDateFormat")
     public static SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FOMMAT_PATTERN);
    /**
     * Get the day's scheme. If there is none, build a new scheme by the day
     * before. If the day before is still none, return null.
     * 
     * @param db
     * @param date
     * @return
     */
    public static Scheme fetchScheme(SQLiteDatabase db, Date date)
    {
        Scheme scheme;
        scheme = fetchOnceByDate(db, sdf.format(date));
//        if (null == scheme)
//        {
//            Scheme yesterdayScheme = fetchOnceByDate(db, sdf.format(Tools.prevDay(date)));
//            if (null != yesterdayScheme)
//            {
//                scheme = new Scheme(yesterdayScheme);
//                DataUpdater.insertScheme(db, scheme);
//            }
//            else
//            {
//                scheme = new Scheme();
//            }
//        }

        if (null == scheme)
        {
            scheme = new Scheme (getPreviousByScheme(db, Tools.prevDay(date)));
            DataUpdater.insertScheme(db, scheme);
        }
        return scheme;
    }

    /**
     * 获取前一日期的scheme，如果没有，会继续向前迭代。
     * @param db
     * @param prevDay
     * @return
     */
    private static Scheme getPreviousByScheme(SQLiteDatabase db,Date prevDay)
    {
        Scheme scheme = fetchOnceByDate(db, sdf.format(prevDay));
        if (scheme==null) {
            scheme = new Scheme(getPreviousByScheme(db, Tools.prevDay(prevDay)));
            DataUpdater.insertScheme(db, scheme);
        }
        return scheme;
        
    }

    private static Scheme fetchOnceByDate(SQLiteDatabase db, String date)
    {
        String[] columns = null;
        String selection = Constant.COL_MDATE + "=?";
        String[] selectionArgs =
        {
                String.valueOf(date)
        };
        String orderby = Constant.COL_STARTTIME + " ASC";

        Cursor cursor = db.query(Constant.TABLE_NAME, columns, selection, selectionArgs, null, null, orderby);

        if (cursor.moveToFirst())
        {
            return buildSchemeByCorsor(cursor);
        }
        else
        {
            return null;
        }
    }

    private static Scheme buildSchemeByCorsor(Cursor cursor)
    {
        int todayValue = 0;
        int yesterdayValue = 0;
        Date date = new Date();
        ArrayList<Target> targets = new ArrayList<Target>();
        do
        {
            String name = cursor.getString(cursor.getColumnIndex(Constant.COL_NAME));
            String mDate = cursor.getString(cursor.getColumnIndex(Constant.COL_MDATE));
            String startTime = cursor.getString(cursor.getColumnIndex(Constant.COL_STARTTIME));
            String endTime = cursor.getString(cursor.getColumnIndex(Constant.COL_ENDTIME));
            String description = cursor.getString(cursor.getColumnIndex(Constant.COL_DESCRIPTION));
            int mValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_MVALUE));
            boolean isAgenda = (cursor.getInt(cursor.getColumnIndex(Constant.COL_ISAGENDA)) > 0);
            int mInterval = cursor.getInt(cursor.getColumnIndex(Constant.COL_MINTERVAL));
            int mMaxValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_MMAXVALUE));
            todayValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_TODAYVALUE));
            yesterdayValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_YESTERDAYVALUE));
            boolean isDone = (cursor.getInt(cursor.getColumnIndex(Constant.COL_ISDONE)) > 0);
            if (isAgenda)
            {
                Agenda agenda = new Agenda(name, mDate, startTime, endTime, description, mValue, mInterval, mMaxValue,
                        isDone);
                targets.add(agenda);
                date = agenda.getTime();
            }
            else
            {
                Backlog backlog = new Backlog(name, mDate, startTime, endTime, description, mValue, isDone);
                targets.add(backlog);
                date = backlog.getTime();
            }
        }
        while (cursor.moveToNext());

        Scheme scheme = new Scheme(date, todayValue, yesterdayValue, targets);

        // May check yesterday's scheme
        scheme.check();

        return scheme;
    }

}
