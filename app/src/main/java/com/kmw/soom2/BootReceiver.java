package com.kmw.soom2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.kmw.soom2.Common.DbOpenHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BootReceiver extends BroadcastReceiver {

    DbOpenHelper mDbOpenHelper;
    Cursor iCursor;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    SimpleDateFormat formatHH = new SimpleDateFormat("HH");
    SimpleDateFormat formatMM = new SimpleDateFormat("mm");

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            try {
                mDbOpenHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mDbOpenHelper.create();

            iCursor = mDbOpenHelper.selectColumns();

            while(iCursor.moveToNext()){
                String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
                String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
                long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
                int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));

                diaryNotification(context,iCursor.getInt(iCursor.getColumnIndex("_id")),tempSelectTime);
            }
        }
    }

    void diaryNotification(Context context,int idx,long times)
    {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, idx, intent, 0);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(formatHH.format(new Date(times))));
        calendar.set(Calendar.MINUTE, Integer.parseInt(formatMM.format(new Date(times))));

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
