package com.example.android_0100_selfimproveproject.database;

import com.example.android_0100_selfimproveproject.Constant;
import com.example.android_0100_selfimproveproject.utils.LogUtils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class FirstRunDataInput
{
    SQLiteDatabase db;
    ContentValues values;
    String mdate = "2015/09/15";
    int yesterdayvalue = 555;
    int todayvalue = 597;
    int isagenda = 1;

    public FirstRunDataInput(SQLiteDatabase db)
    {
        this.db = db;
        this.values = new ContentValues();
    }

    public void insertYesterday()
    {
        insertOneTarget("早起", "06:30", "06:30", "六点半前起才算早起", 2, 1, 10, 1);
        insertOneTarget("计划", "07:30", "07:30", "七点半前必须完成今日计划，否则视为失败", 2, 1, 10, 1);
        insertOneTarget("锻炼", "07:30", "08:30", "至少一个小时的有氧运动", 9, 1, 10, 1);
        insertOneTarget("单词", "08:30", "09:00", "百词斩，考研词汇，至少30个", 5, 1, 5, 1);
        insertOneTarget("口语", "09:00", "09:30", "有道口语大师，一课，从开始至练习", 4, 1, 5, 1);
        insertOneTarget("英语阅读", "09:30", "10:00", "Quora一篇", 3, 1, 5, 1);
        insertOneTarget("技术阅读", "10:00", "12:00", "SIP的数据持久化", 2, 1, 10, 1);
        insertOneTarget("技术实践", "14:00", "16:00", "SIP的手机调试", 2, 1, 10, 0);
        insertOneTarget("技术日常", "16:00", "17:30", "git命令行学习实践", 2, 1, 10, 0);
        insertOneTarget("其他阅读", "20:00", "21:00", "《如何阅读一本书》第一章", 2, 1, 5, 0);
        insertOneTarget("日记", "21:00", "21:30", "写今天所得", 5, 1, 5, 1);
        insertOneTarget("摘抄", "21:30", "22:00", "摘抄一段美文或一句名言", 5, 1, 5, 1);
        insertOneTarget("刷牙", "23:30", "23:30", "每天两次，每次至少5分钟", 5, 1, 5, 1);
        insertOneTarget("联系家人", "23:30", "23:30", "电话或短信", 5, 1, 5, 1);
        insertOneTarget("拍照", "23:30", "23:30", "每天拍一张相片，或录制一秒视频", 2, 1, 5, 0);
        insertOneTarget("节食", "23:30", "23:30", "不吃油腻，少吃主食，多吃水果", 3, 1, 10, 1);
    }

    public void insertOneTarget(String name, String starttime, String endtime, String description, int mvalue,
            int minterval, int mmaxvalue, int isdone)
    {
        values.put(Constant.COL_NAME, name);
        values.put(Constant.COL_MDATE, mdate);
        values.put(Constant.COL_STARTTIME, starttime);
        values.put(Constant.COL_ENDTIME, endtime);
        values.put(Constant.COL_DESCRIPTION, description);
        values.put(Constant.COL_MVALUE, mvalue);
        values.put(Constant.COL_ISAGENDA, isagenda);
        values.put(Constant.COL_MINTERVAL, minterval);
        values.put(Constant.COL_MMAXVALUE, mmaxvalue);
        values.put(Constant.COL_TODAYVALUE, todayvalue);
        values.put(Constant.COL_YESTERDAYVALUE, yesterdayvalue);
        values.put(Constant.COL_ISDONE, isdone);
        db.insert(Constant.TABLE_NAME, null, values);
        values.clear();
        LogUtils.d(Constant.DB_TAG, "INSERT INITIAL VALUE:" + name);
    }
}
