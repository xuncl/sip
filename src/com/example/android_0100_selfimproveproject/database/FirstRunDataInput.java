package com.example.android_0100_selfimproveproject.database;

import com.example.android_0100_selfimproveproject.Constant;
import com.example.android_0100_selfimproveproject.utils.LogUtils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class FirstRunDataInput
{
    SQLiteDatabase db;
    ContentValues values;
    String mdate = "2016/07/18";
    int yesterdayvalue = 1263;
    int todayvalue = 1263;
    int isagenda = 1;

    public FirstRunDataInput(SQLiteDatabase db)
    {
        this.db = db;
        this.values = new ContentValues();
    }

    public void insertYesterday()
    {
        insertOneTarget("����", "06:30", "06:30", "�����ǰ���������", 2, 1, 10, 0);
        insertOneTarget("�ƻ�", "07:30", "07:30", "�ߵ��ǰ������ɽ��ռƻ���������Ϊʧ��", 2, 1, 10, 0);
        insertOneTarget("����", "07:30", "08:30", "����һ��Сʱ�������˶�", 5, 1, 10, 1);
        insertOneTarget("����", "08:30", "09:00", "�ٴ�ն�����дʻ㣬����30��", 4, 1, 5, 1);
        insertOneTarget("����", "09:00", "09:30", "�е������ʦ��һ�Σ��ӿ�ʼ����ϰ", 4, 1, 5, 1);
        insertOneTarget("Ӣ���Ķ�", "09:30", "10:00", "Quoraһƪ", 4, 1, 5, 1);
        insertOneTarget("�����Ķ�", "10:00", "12:00", "��cocos��������", 2, 1, 10, 0);
        insertOneTarget("����ʵ��", "14:00", "16:00", "��cocos��������ʵ��", 2, 1, 10, 0);
        insertOneTarget("�����ճ�", "16:00", "17:30", "java���� 1-5 �ʼ�", 2, 1, 10, 0);
        insertOneTarget("�����Ķ�", "20:00", "21:00", "������Ķ�һ���顷���и���", 2, 1, 5, 0);
        insertOneTarget("�ռ�", "21:00", "21:30", "д��������", 5, 1, 5, 1);
        insertOneTarget("ժ��", "21:30", "22:00", "ժ��һ�����Ļ�һ������", 4, 1, 5, 1);
        insertOneTarget("ˢ��", "23:30", "23:30", "ÿ�����Σ�ÿ������5����", 5, 1, 5, 1);
        insertOneTarget("��ϵ����", "23:30", "23:30", "�绰�����", 5, 1, 5, 1);
        insertOneTarget("����", "23:30", "23:30", "ÿ����һ����Ƭ����¼��һ����Ƶ��ÿ���һƪ�����ĵã���100��", 5, 1, 5, 1);
        insertOneTarget("��ʳ", "23:30", "23:30", "�������壬�ٳ���ʳ�����ˮ��", 2, 1, 10, 0);
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
