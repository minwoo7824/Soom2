package com.kmw.soom2.Common;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunityDetailActivity;
import com.kmw.soom2.InsertActivity.InsertActivity.SplashActivity;
import com.kmw.soom2.MyPage.Activity.NoticeActivity;
import com.kmw.soom2.MyPage.Activity.NoticeDetailActivity;
import com.kmw.soom2.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.app.Notification.CATEGORY_MESSAGE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyNotificationListener";

    private static PowerManager.WakeLock sCpuWakeLock;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    MediaPlayer player;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;
    NotificationManager notificationManager;
    private static int notificationId = 0;
    MediaPlayer mediaPlayer;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }

    @Override
    public void onCreate() {

    }

    // [START receive_message]
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG,"remoteMessage = "+remoteMessage.getData());
        Log.i(TAG,"remoteMessage.getData().get(title) = "+remoteMessage.getData().get("title"));
        Log.i(TAG,"remoteMessage.getData().get(body) = "+remoteMessage.getData().get("body"));
        Log.i(TAG,"notification = " + remoteMessage.getNotification());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = null;

        if (remoteMessage.getData().get("android_channel_id").equals("community")){
            notificationIntent = new Intent(this, CommunityDetailActivity.class);
            Log.i(TAG,"communityNo : " + remoteMessage.getData().get("no"));
            notificationIntent.putExtra("communityNo",remoteMessage.getData().get("no"));
            notificationIntent.putExtra("push",true);
        }else if (remoteMessage.getData().get("android_channel_id").equals("notice")){
            notificationIntent = new Intent(this, NoticeActivity.class);
            notificationIntent.putExtra("noticeNo",remoteMessage.getData().get("no"));
            notificationIntent.putExtra("body",remoteMessage.getData().get("body"));
            notificationIntent.putExtra("push",true);
        }else{
            notificationIntent = new Intent(this, SplashActivity.class);
        }

        PendingIntent pendingI = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, remoteMessage.getData().get("android_channel_id"));

        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.soom_logo_512); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
        }else builder.setSmallIcon(R.mipmap.soom_logo_512); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setContentInfo("INFO")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingI);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        notificationManager.notify(1234, builder.build());
    }
}
