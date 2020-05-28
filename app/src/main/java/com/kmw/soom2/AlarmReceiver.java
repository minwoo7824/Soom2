package com.kmw.soom2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.InsertActivity.InsertActivity.SplashActivity;

import java.sql.SQLException;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    DbOpenHelper mDbOpenHelper;
    Cursor iCursor;

    @Override
    public void onReceive(Context context, Intent intent) {

        mDbOpenHelper = new DbOpenHelper(context);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mDbOpenHelper.create();

        iCursor = mDbOpenHelper.selectColumns();

        while (iCursor.moveToNext()){
            Log.i(TAG,"cnt : " + intent.getIntExtra("cnt",0));
            Log.i(TAG,"time : " + iCursor.getInt(iCursor.getColumnIndex("_id")) + " time : " + iCursor.getLong(iCursor.getColumnIndex("selectTime")) + " current : " + System.currentTimeMillis());
            if (intent.getIntExtra("cnt",0) == iCursor.getInt(iCursor.getColumnIndex("_id"))){
                if (iCursor.getString(iCursor.getColumnIndex("selectDay")).contains(doDayOfWeek()) && iCursor.getInt(iCursor.getColumnIndex("pushCheck")) == 1){
//                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);//추가

                    Intent notificationIntent = new Intent(context, SplashActivity.class);
                    notificationIntent.putExtra("medicineAlarm",true);

                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent pendingI = PendingIntent.getActivity(context, 0,
                            notificationIntent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "drugAlarm");

                    //OREO API 26 이상에서는 채널 필요
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        builder.setSmallIcon(R.drawable.soom_logo_512); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                    }else builder.setSmallIcon(R.mipmap.soom_logo_512); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                    builder.setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setTicker("{Time to watch some cool stuff!}")
                            .setContentTitle("약 먹을 시간입니다.")
                            .setContentText("클릭, 간편하게 복용여부 기록하기.")
                            .setContentInfo("INFO")
                            .setPriority(NotificationCompat.PRIORITY_MAX) //알람 중요도
                            .setContentIntent(pendingI);

                    notificationManager.notify(1234, builder.build());
                }
            }
        }
    }

    private String doDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        String strWeek = null;

        int week = calendar.get(Calendar.DAY_OF_WEEK);

        if (week == 1){
            strWeek = "1";
        }else if (week == 2){
            strWeek = "2";
        }else if (week == 3){
            strWeek = "3";
        }else if (week == 4){
            strWeek = "4";
        }else if (week == 5){
            strWeek = "5";
        }else if (week == 6){
            strWeek = "6";
        }else if (week == 7){
            strWeek = "7";
        }
        return strWeek;
    }
}
