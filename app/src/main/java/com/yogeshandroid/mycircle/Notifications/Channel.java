package com.yogeshandroid.mycircle.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

public class Channel extends Application {
    public static final String FCM_CHANNEL_ID = "channelId";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();


    }

    // method to create fcm channel
    private void createNotificationChannel() {
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(FCM_CHANNEL_ID, "FCM Channel", NotificationManager.IMPORTANCE_DEFAULT);
            // set any additional properties for channel for example . you can set channel description and led color etc
            channel.setDescription("DCM Channel Description");
            channel.setLightColor(Color.GREEN);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }

    public static String getFcmChannelId() {
        return FCM_CHANNEL_ID;
    }
}
