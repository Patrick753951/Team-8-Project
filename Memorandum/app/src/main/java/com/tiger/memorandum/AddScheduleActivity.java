package com.tiger.memorandum;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class AddScheduleActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, View.OnClickListener {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private RelativeLayout rl_space_num;
    private RelativeLayout rl_space_time;
    private EditText ed_space_time;
    private EditText ed_space_num;
    private EditText et_person;
    private CheckBox cbAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        hour = 0;
        minute = 0;
        TimePicker timePicker = (TimePicker) findViewById(R.id.scheduletime);
        timePicker.setEnabled(true);
        timePicker.setOnTimeChangedListener(this);
        ((Button) findViewById(R.id.btnschedule)).setOnClickListener(this);
        rl_space_num= findViewById(R.id.rl_space_num);
        ed_space_num= findViewById(R.id.ed_space_num);
        ed_space_time= findViewById(R.id.ed_space_time);
        rl_space_time= findViewById(R.id.rl_space_time);
        et_person= findViewById(R.id.et_person);
        cbAlarm = (CheckBox) findViewById(R.id.checkbox);
        cbAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rl_space_num.setVisibility(View.VISIBLE);
                    rl_space_time.setVisibility(View.VISIBLE);
                }else {
                    rl_space_num.setVisibility(View.GONE);
                    rl_space_time.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int h, int m) {
        hour = h;
        minute = m;
    }

    public boolean setSystemAlarmClock(String message, int hour, int minute) {
        if (Build.VERSION.SDK_INT < 9) {
            return false;
        }
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        if (Build.VERSION.SDK_INT >= 11) {
            intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
        }
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setAlarmClock(Record record,long time) {
        Log.d("jcy-TAG", "set:AlarmManager  time "+time);
        final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction("com.Android.AlarmManager.action.BACK_ACTION");
        intent.putExtra("ID", record.getId());
        Random r=new Random();
        PendingIntent pi = PendingIntent.getBroadcast(this, r.nextInt(), intent, 0);
        //立即提醒
        am.set(AlarmManager.RTC, time, pi);
        return true;
    }

    @Override
    public void onClick(View view) {

        EditText et = (EditText) findViewById(R.id.et_schedule);
        String content = et.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入行程内容", Toast.LENGTH_SHORT).show();
            return;
        }
        String person = et_person.getText().toString();
        if (TextUtils.isEmpty(person)) {
            Toast.makeText(this, "请输入人员", Toast.LENGTH_SHORT).show();
            return;
        }
        String spaceTime = ed_space_time.getText().toString();
        String spaceNum = ed_space_num.getText().toString();
        if(cbAlarm.isChecked()){

            if (TextUtils.isEmpty(spaceTime)) {
                Toast.makeText(this, "请输入闹钟间隔时间", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(spaceNum)) {
                Toast.makeText(this, "请输入闹钟间隔次数", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        MyDataBase myDataBase = new MyDataBase(this, MyDataBase.DB_FILENAME, null, 1);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, 0);
        long currentTimeMillis = c.getTimeInMillis();
        int t = (int) (currentTimeMillis / 1000);
        Record record = new Record();
        record.setTime(t);
        record.setType(0);
        record.setPerson(person);
        if(cbAlarm.isChecked()){
            record.setNum(Integer.valueOf(spaceNum));
            record.setSpaceTime(Integer.valueOf(spaceTime));
        }

        record.setText(content);
        long id = myDataBase.addRecord(record);
        record.setId((int) id);
        Toast.makeText(this, R.string.addschedulesuccess, Toast.LENGTH_SHORT).show();
        if (cbAlarm.isChecked()) {
            if (setAlarmClock(record,currentTimeMillis)) {
//            if (setAlarmClock(record,System.currentTimeMillis()+6*1000)) {
                Toast.makeText(this, R.string.addalarmsuccess, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
