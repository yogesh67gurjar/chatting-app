package com.yogeshandroid.mycircle.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Application {
    //  saari activities k shuru hone k phle ye code run hoga

    public static final String FCM_CHANNEL_ID = "channelId";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    // method to create fcm channel
    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(FCM_CHANNEL_ID, "FCM Channel", NotificationManager.IMPORTANCE_DEFAULT);
            // set any additional properties for channel for example . you can set channel description and led color etc
            channel.setDescription("DCM Channel Description");
            channel.setLightColor(Color.GREEN);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // jab multiple notification channels ho tb apn ese krenge
//            List<NotificationChannel> channelList=new ArrayList<>();
//            channelList.add(channel);
//            channelList.add(channel);
//            notificationManager.createNotificationChannels(channelList);

        }
    }

    public static String getFcmChannelId() {
        return FCM_CHANNEL_ID;
    }
}
