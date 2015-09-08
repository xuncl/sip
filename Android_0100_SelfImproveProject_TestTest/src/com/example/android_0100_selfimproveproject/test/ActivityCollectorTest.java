package com.example.android_0100_selfimproveproject.test;

import com.example.android_0100_selfimproveproject.activities.ActivityCollector;
import com.example.android_0100_selfimproveproject.activities.TargetActivity;

import android.test.AndroidTestCase;

public class ActivityCollectorTest extends AndroidTestCase
{

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public void testAddActivity()
    {
        assertEquals(0, ActivityCollector.activities.size());
        TargetActivity targetActivity = new TargetActivity();
        ActivityCollector.addActivity(targetActivity);
        assertEquals(1, ActivityCollector.activities.size());
        ActivityCollector.addActivity(targetActivity);
        assertEquals(1, ActivityCollector.activities.size());
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
