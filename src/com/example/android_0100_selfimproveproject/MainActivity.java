package com.example.android_0100_selfimproveproject;

import java.util.Date;

import com.example.android_0100_selfimproveproject.activities.ActivityCollector;
import com.example.android_0100_selfimproveproject.activities.BaseActivity;
import com.example.android_0100_selfimproveproject.activities.TargetActivity;
import com.example.android_0100_selfimproveproject.activities.TargetAdapter;
import com.example.android_0100_selfimproveproject.database.DataDeleter;
import com.example.android_0100_selfimproveproject.database.DataFetcher;
import com.example.android_0100_selfimproveproject.database.DataUpdater;
import com.example.android_0100_selfimproveproject.database.MyDatabaseHelper;
import com.example.android_0100_selfimproveproject.service.Backlog;
import com.example.android_0100_selfimproveproject.service.Scheme;
import com.example.android_0100_selfimproveproject.service.Target;
import com.example.android_0100_selfimproveproject.utils.Tools;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
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
        db.close();
    }

    private void initList()
    {
        TargetAdapter adapter = new TargetAdapter(MainActivity.this, R.layout.target_item, scheme.getTargets());
        ListView listView = (ListView) findViewById(R.id.target_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @SuppressWarnings("deprecation")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Because of existence of image button, event will not focus on
                // parents.
                Target target = scheme.getTargets().get(position);
//                deleteDialog(target);
                MainActivity.this.showDialog(scheme.getTargets().indexOf(target));
            }
        });
    }

    private void saveTodayTargets()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataUpdater.updateScheme(db, scheme);
        db.close();
    }

    private void addNewTarget(Target target)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        scheme.getTargets().add(target);
        DataUpdater.insertTarget(db, scheme, target);
        db.close();
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
        String name = data.getStringExtra(Constant.NAME_PARA);
        String des = data.getStringExtra(Constant.DESCRIPTION_PARA);
        String start = data.getStringExtra(Constant.START_PARA);
        String end = data.getStringExtra(Constant.END_PARA);
        int value = data.getIntExtra(Constant.VALUE_PARA, Constant.BASED_VALUE);
        boolean isdone = data.getBooleanExtra(Constant.ISDONE_PARA, false);
        switch (requestCode)
        {
        case Constant.RESULT_ADD_TAG:
            if (resultCode == RESULT_OK)
            {
                boolean isAgenda = false;

                if (!isAgenda)
                {
                    Backlog backlog = new Backlog(new Date(), name, start, end, des, value, isdone);
                    addNewTarget(backlog);
                }
                fetchAll();
            }
            break;
        case Constant.RESULT_MOD_TAG:
            if (resultCode == RESULT_OK)
            {
                Target target = Tools.getTargetByScheme(scheme, name);
                target.setDescription(des);
                target.setValue(value);
                target.setTime(Tools.parseTimeByDate(new Date(), start));
                target.setEndTime(Tools.parseTimeByDate(new Date(), end));
                saveAll();
                fetchAll();
            }
            break;
        
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
        TargetActivity.anctionStart(MainActivity.this, null, null, null, null, 0, false, Constant.RESULT_ADD_TAG);

    }

    private void deleteTarget(Target target)
    {
        Toast.makeText(MainActivity.this, "deleting...", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataDeleter.deleteTarget(db, target);
        scheme.getTargets().remove(target);
        scheme.check();
        db.close();
    }

    private boolean saveAll()
    {
        Toast.makeText(MainActivity.this, "saving...", Toast.LENGTH_SHORT).show();
        saveTodayTargets();
        return false;
    }

    private void deleteDialog(final Target target)
    {
        AlertDialog.Builder builder = new Builder(MainActivity.this);
        builder.setMessage("确认删除" + target.getName() + "吗？");
        builder.setTitle("删除");
        builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                deleteTarget(target);
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        final int index  = id;
        Dialog dialog = null;
        Builder builder = new android.app.AlertDialog.Builder(this);
        // 设置对话框的标题
        builder.setTitle("选择您的操作");
        // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setItems(R.array.dialog_actions, new android.content.DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                String action = getResources().getStringArray(R.array.dialog_actions)[which];
                if (action.equals("删除"))
                {
                    dialog.dismiss();
                    deleteDialog(scheme.getTargets().get(index));
                }
                else if (action.equals("修改"))
                {
                    dialog.dismiss();
                    Target target = scheme.getTargets().get(index);
                    TargetActivity.anctionStart(MainActivity.this, target.getName(), target.getDescription(),
                            Tools.formatTime(target.getTime()), Tools.formatTime(target.getEndTime()), target.getValue(),
                            target.isDone(), Constant.RESULT_MOD_TAG);
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });
        // 创建一个列表对话框
        dialog = builder.create();
        return dialog;
    }
}
