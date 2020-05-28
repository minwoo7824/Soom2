package com.kmw.soom2;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.kmw.soom2.Home.HomeAdapter.KakaoSDKAdapter;
import com.kakao.auth.KakaoSDK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LoggerConfiguration;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    private static final Logger LOG = LoggerFactory.getLogger(GlobalApplication.class.getSimpleName());

    public static GlobalApplication getGlobalApplicationContext() {

        if (instance == null) {

            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");

        }
        return instance;
    }

    @Override

    public void onCreate() {

        super.onCreate();

        FileLogHandlerConfiguration fileHandler = LoggerConfiguration.fileLogHandler(this);
        fileHandler.setRotateFilesCountLimit(9);
        LoggerConfiguration.configuration()
                .addHandlerToRootLogger(fileHandler);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//오레오 버전 이상은 채널로 만들어 줘야 한다.
            makeNotificationChannel("drugAlarm","drugAlarm","",NotificationManager.IMPORTANCE_HIGH, null);
            makeNotificationChannel("notice","notice","",NotificationManager.IMPORTANCE_HIGH, null);
            makeNotificationChannel("community","community","",NotificationManager.IMPORTANCE_HIGH, null);
        }
        instance = this;
        // Kakao Sdk 초기화

        KakaoSDK.init(new KakaoSDKAdapter());

    }

    @Override

    public void onTerminate() {

        super.onTerminate();

        instance = null;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, String description,int importance, Uri soundUri)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setShowBadge(true);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build();

        long[] pattern = {0,500,0,500};

        channel.setVibrationPattern(pattern);
        channel.enableVibration(true);
        channel.setDescription(description);
        channel.setSound(soundUri,audioAttributes);

        notificationManager.createNotificationChannel(channel);
    }
}