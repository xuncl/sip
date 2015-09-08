package com.example.android_0100_selfimproveproject.database;

import java.text.SimpleDateFormat;

import com.example.android_0100_selfimproveproject.Constant;
import com.example.android_0100_selfimproveproject.service.Target;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;

public class DataDeleter
{
    @SuppressLint("SimpleDateFormat")
    public static void deleteTarget(SQLiteDatabase db, Target target)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FOMMAT_PATTERN);
        String[] whereArgs =
        {
                target.getName(), dateFormat.format(target.getTime())
        };
        db.delete(Constant.TABLE_NAME, Constant.COL_NAME + " = ? and " + Constant.COL_MDATE + " = ?", whereArgs);
    }

}
