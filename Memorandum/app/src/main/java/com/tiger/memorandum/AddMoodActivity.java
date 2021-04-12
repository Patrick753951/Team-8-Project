package com.tiger.memorandum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddMoodActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, View.OnClickListener {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        hour = 0;
        minute = 0;
        TimePicker timePicker = (TimePicker) findViewById(R.id.moodtime);
        timePicker.setEnabled(true);
        timePicker.setOnTimeChangedListener(this);

        ((Button) findViewById(R.id.btnmood)).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onTimeChanged(TimePicker timePicker, int h, int m) {
        hour = h;
        minute = m;
    }

    @Override
    public void onClick(View view) {
        EditText et = (EditText) findViewById(R.id.et_mood);
        String content = et.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(this, "please enter the mood", Toast.LENGTH_SHORT).show();
            return;
        }
        MyDataBase myDataBase = new MyDataBase(this, MyDataBase.DB_FILENAME, null, 1);

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, 0);
        long currentTimeMillis = c.getTimeInMillis();
        int t = (int) (currentTimeMillis / 1000);
        Record record = new Record();
        record.setTime(t);
        record.setType(1);
        record.setText(content);
        myDataBase.addRecord(record);
        Toast.makeText(this, R.string.addmoodsuccess, Toast.LENGTH_SHORT).show();
        finish();
    }
}
