package com.tiger.memorandum;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tiger.memorandum.utils.NotificationUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

/**
 * @name: AlarmReceiver
 * @date: 2021-04-12 15:30
 * @comment:
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        long recordId = intent.getLongExtra("ID", -1);
        Log.d("jcy-TAG", "recordId: " + recordId);
        MyDataBase myDataBase = new MyDataBase(context, MyDataBase.DB_FILENAME, null, 1);
        final Record record = myDataBase.getRecordById(recordId);
        Log.d("jcy-TAG", "record : " + record);
        if (record.getCurNum() >= record.getNum()) {
            //闹钟全部完事了
            Log.d("jcy-TAG", "notif is done : ");
        } else {
            //重复播放下一次闹钟
            final AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent aintent = new Intent();
            aintent.setAction("com.Android.AlarmManager.action.BACK_ACTION");
            aintent.putExtra("ID", record.getId());
            Random r = new Random();
            PendingIntent pi = PendingIntent.getBroadcast(context, r.nextInt(), intent, 0);
            long nextAlarmTime = System.currentTimeMillis() + record.getSpaceTime() * 60 * 1000;
//            long nextAlarmTime = System.currentTimeMillis() +5000;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curDate = format.format(new Date());
            String nextDate = format.format(new Date(nextAlarmTime));
            Log.d("jcy-TAG", " now  : " + curDate);
            Log.d("jcy-TAG", " next time  : " + nextDate);
            //立即提醒
            am.set(AlarmManager.RTC, nextAlarmTime, pi);
            int curNum  =record.getCurNum()+1;
            myDataBase.updateRecord(record.getId(),curNum);
        }
        NotificationUtils.setNotificationBarVisibility(false);
        NotificationUtils.notify(101, new NotificationUtils.Consumer<NotificationCompat.Builder>() {
            @Override
            public void accept(NotificationCompat.Builder param) {
                Intent resultIntent = new Intent(context, MainActivity.class);//点击通知后进入的活动
                //这两句非常重要，使之前的活动不出栈
                resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);//允许更新
                param.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("new alarm")
                        .setContentIntent(resultPendingIntent)
                        .setContentText(record.getText())
                        .setAutoCancel(true);
            }
        });
    }

}