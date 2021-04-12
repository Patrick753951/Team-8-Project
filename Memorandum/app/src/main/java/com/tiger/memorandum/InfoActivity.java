package com.tiger.memorandum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InfoActivity extends AppCompatActivity {
    private int year;
    private int month;
    private int day;
    private TextView tvinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        tvinfo = (TextView) findViewById(R.id.tvinfo);
        ((Button) findViewById(R.id.btn_add_mood)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, AddMoodActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);
            }
        });
        ((Button) findViewById(R.id.btn_add_schedule)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, AddScheduleActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        update();
    }

    public void update() {
        MyDataBase myDataBase = new MyDataBase(this, MyDataBase.DB_FILENAME, null, 1);
        ArrayList<Record> records = myDataBase.getRecord();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        long currentTimeMillis = c.getTimeInMillis();
        int t = (int) (currentTimeMillis / 1000);
        t = t - t % (3600 * 24);
       StringBuffer sbRec = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        for (Record r : records) {
            if (t <= r.getTime() && (t + 3600 * 24) > r.getTime()) {
                if (r.getType() == 0)
                {
                    sbRec.append("行程 : "+ r.getText());
                    sbRec.append("\n人员:"+r.getPerson());
                }
                else
                {
                    sbRec.append("日记 : "+ r.getText());
                }
                String curDate = format.format(new Date(r.getTime()));
                sbRec.append("\n"+curDate);
                sbRec.append("\n\n");
            }
        }
        tvinfo.setText(sbRec.toString());
    }
}
