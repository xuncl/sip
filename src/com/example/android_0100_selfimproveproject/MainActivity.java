package com.example.android_0100_selfimproveproject;

import java.util.Date;

import com.example.android_0100_selfimproveproject.activities.ActivityCollector;
import com.example.android_0100_selfimproveproject.activities.BaseActivity;
import com.example.android_0100_selfimproveproject.activities.TargetActivity;
import com.example.android_0100_selfimproveproject.activities.TargetAdapter;
import com.example.android_0100_selfimproveproject.database.DataFetcher;
import com.example.android_0100_selfimproveproject.database.DataUpdater;
import com.example.android_0100_selfimproveproject.database.MyDatabaseHelper;
import com.example.android_0100_selfimproveproject.service.Backlog;
import com.example.android_0100_selfimproveproject.service.Scheme;
import com.example.android_0100_selfimproveproject.service.Target;
import com.example.android_0100_selfimproveproject.utils.Tools;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener
{

    private Scheme scheme;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initTitle();
        initTargets();
    }

    private void initTitle()
    {
        ImageView titleBack = (ImageView) findViewById(R.id.title_back);
        titleBack.setOnClickListener(this);
        ImageView titleAdd = (ImageView) findViewById(R.id.title_add);
        titleAdd.setOnClickListener(this);
        ImageView titleRefresh = (ImageView) findViewById(R.id.title_refresh);
        titleRefresh.setOnClickListener(this);
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setOnClickListener(this);
    }

    private void initTargets()
    {
        dbHelper = new MyDatabaseHelper(this, Constant.DB_NAME, null, 1);
        refreshTargets();
    }

    private void refreshTargets()
    {
        Date today = new Date();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        scheme = DataFetcher.fetchScheme(db, today);
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(scheme.toShortString());
        initList();
    }

    private void initList()
    {
        TargetAdapter adapter = new TargetAdapter(MainActivity.this, R.layout.target_item, scheme.getTargets());
        ListView listView = (ListView) findViewById(R.id.target_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Because of existence of image button, event will not focus on
                // parents.
                Target target = scheme.getTargets().get(position);
                TargetActivity.anctionStart(MainActivity.this, target.getName(), target.getDescription(),
                        Tools.formatTime(target.getTime()), Tools.formatTime(target.getEndTime()), target.getValue(),
                        target.isDone(), Constant.RESULT_TAG);
            }
        });
    }

    private void saveTodayTargets()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataUpdater.updateScheme(db, scheme);
    }

    private void addNewTarget(Target target)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        scheme.getTargets().add(target);
        DataUpdater.insertTarget(db, scheme, target);
    }

    @Override
    protected void onPause()
    {
        saveAll();
        super.onPause();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.title_back:
            // will save data at life cycle
            ActivityCollector.finishAll();
            break;
        case R.id.title_add:
            onAddTarget();
            break;
        case R.id.title_refresh:
            saveAll();
            fetchAll();
            break;
        case R.id.title_text:
            showSchemeDetail();
            break;
        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
        case Constant.RESULT_TAG:
            if (resultCode == RESULT_OK)
            {
                String name = data.getStringExtra(Constant.NAME_PARA);
                String des = data.getStringExtra(Constant.DESCRIPTION_PARA);
                String start = data.getStringExtra(Constant.START_PARA);
                String end = data.getStringExtra(Constant.END_PARA);
                int value = data.getIntExtra(Constant.VALUE_PARA, Constant.BASED_VALUE);
                boolean isdone = data.getBooleanExtra(Constant.ISDONE_PARA, false);
                boolean isAgenda = false;

                if (!isAgenda)
                {
                    Backlog backlog = new Backlog(new Date(),name, start, end, des, value, isdone);
                    addNewTarget(backlog);
                }
                fetchAll();
            }
        }
    }

    private void showSchemeDetail()
    {
        Toast.makeText(MainActivity.this, scheme.toLongString(), Toast.LENGTH_LONG).show();
    }

    private boolean fetchAll()
    {
        Toast.makeText(MainActivity.this, "fectching...", Toast.LENGTH_SHORT).show();
        refreshTargets();
        return false;
    }

    private void onAddTarget()
    {
        Log.d(Constant.SERVICE_TAG, "into onAddTarget()");
        TargetActivity.anctionStart(MainActivity.this, null, null, null, null, 0, false, Constant.RESULT_TAG);

    }

    private boolean saveAll()
    {
        Toast.makeText(MainActivity.this, "saving...", Toast.LENGTH_SHORT).show();
        saveTodayTargets();
        return false;
    }
}
