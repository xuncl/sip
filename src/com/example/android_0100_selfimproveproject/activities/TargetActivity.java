package com.example.android_0100_selfimproveproject.activities;

import com.example.android_0100_selfimproveproject.Constant;
import com.example.android_0100_selfimproveproject.R;
import com.example.android_0100_selfimproveproject.utils.LogUtils;
import com.example.android_0100_selfimproveproject.utils.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class TargetActivity extends BaseActivity
{
    private EditText nameEdit;
    private EditText desEdit;
    private EditText startEdit;
    private EditText endEdit;
    private EditText valueEdit;
    private CheckBox isdoneBox;
    private CheckBox isagendaBox;
    private EditText intervalEdit;
    private EditText maxvalueEdit;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LogUtils.d(Constant.SERVICE_TAG, "into onCreate()");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_target);
        save = (Button) findViewById(R.id.button_save);
        nameEdit = (EditText) findViewById(R.id.target_name);
        desEdit = (EditText) findViewById(R.id.target_description);
        startEdit = (EditText) findViewById(R.id.target_start_time);
        endEdit = (EditText) findViewById(R.id.target_end_time);
        valueEdit = (EditText) findViewById(R.id.target_value);
        isdoneBox = (CheckBox) findViewById(R.id.target_isdone);
        isagendaBox = (CheckBox) findViewById(R.id.target_isagenda);
        intervalEdit = (EditText) findViewById(R.id.target_interval);
        maxvalueEdit = (EditText) findViewById(R.id.target_maxvalue);
        Intent intent = getIntent();
        String tname = intent.getStringExtra(Constant.NAME_PARA);
        boolean isagenda = intent.getBooleanExtra(Constant.ISAGENDA_PARA, false);
        if (!Tools.isEmpty(tname)) // modify
        {
            nameEdit.setText(tname);
            desEdit.setText(intent.getStringExtra(Constant.DESCRIPTION_PARA));
            startEdit.setText(intent.getStringExtra(Constant.START_PARA));
            endEdit.setText(intent.getStringExtra(Constant.END_PARA));
            valueEdit.setText("" + intent.getIntExtra(Constant.VALUE_PARA, Constant.BASED_VALUE));
            isdoneBox.setChecked(intent.getBooleanExtra(Constant.ISDONE_PARA, false));
            isagendaBox.setChecked(isagenda);
            intervalEdit.setText("" + intent.getIntExtra(Constant.INTERVAL_PARA, Constant.BASED_INTERVAL));
            maxvalueEdit.setText("" + intent.getIntExtra(Constant.MAXVALUE_PARA, Constant.BASED_VALUE));
            if (isagenda)
            {
                isagendaBox.setEnabled(false);
                intervalEdit.setEnabled(false);
                maxvalueEdit.setEnabled(false);
            }
            else
            {
                isagendaBox.setVisibility(View.GONE);
                intervalEdit.setVisibility(View.GONE);
                maxvalueEdit.setVisibility(View.GONE);
            }
        }
        save.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                boolean valueParsable = true;

                boolean isagenda = isagendaBox.isChecked();
                int value = Constant.BASED_VALUE;
                int interval = Constant.BASED_INTERVAL;
                int maxvalue = Constant.BASED_VALUE;
                try
                {
                    value = Integer.parseInt(valueEdit.getText().toString());
                    if (isagenda)
                    {
                        interval = Integer.parseInt(intervalEdit.getText().toString());
                        maxvalue = Integer.parseInt(maxvalueEdit.getText().toString());
                    }
                }
                catch (Exception e)
                {
                    valueParsable = false;
                }
                String name = nameEdit.getText().toString();
                String description = desEdit.getText().toString();
                String start = startEdit.getText().toString();
                String end = endEdit.getText().toString();
                boolean isdone = isdoneBox.isChecked();

                if (valueParsable && (!Tools.isEmpty(name)) && (Tools.checkTime(start)) && (Tools.checkTime(end)))
                {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.NAME_PARA, name);
                    intent.putExtra(Constant.DESCRIPTION_PARA, description);
                    intent.putExtra(Constant.START_PARA, start);
                    intent.putExtra(Constant.END_PARA, end);
                    intent.putExtra(Constant.VALUE_PARA, value);
                    intent.putExtra(Constant.ISDONE_PARA, isdone);
                    intent.putExtra(Constant.ISAGENDA_PARA, isagenda);
                    intent.putExtra(Constant.INTERVAL_PARA, interval);
                    intent.putExtra(Constant.MAXVALUE_PARA, maxvalue);

                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(TargetActivity.this, "Input wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }

    public static void anctionStart(BaseActivity activity, String name, String description, String start, String end,
            int value, boolean isdone, boolean isagenda, int interval, int maxvalue, int resultTag)
    {
        Intent intent = new Intent(activity, TargetActivity.class);
        intent.putExtra(Constant.NAME_PARA, name);
        intent.putExtra(Constant.DESCRIPTION_PARA, description);
        intent.putExtra(Constant.START_PARA, start);
        intent.putExtra(Constant.END_PARA, end);
        intent.putExtra(Constant.VALUE_PARA, value);
        intent.putExtra(Constant.ISDONE_PARA, isdone);
        intent.putExtra(Constant.ISAGENDA_PARA, isagenda);
        intent.putExtra(Constant.INTERVAL_PARA, interval);
        intent.putExtra(Constant.MAXVALUE_PARA, maxvalue);
        activity.startActivityForResult(intent, resultTag);
    }
}
