package com.example.android_0100_selfimproveproject.activities;

import com.example.android_0100_selfimproveproject.Constant;
import com.example.android_0100_selfimproveproject.utils.LogUtils;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LogUtils.d(Constant.BASE_ACTIVITY_TAG, getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    
}
