package com.yogeshandroid.mycircle.Notifications;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class Channel extends Application {

    private boolean isAppInForeground = false;
    private Handler handler;
    private Runnable runnable;
    int hour, minute, second;

    public static final String FCM_CHANNEL_ID = "channelId";

    @Override
    public void onCreate() {
        super.onCreate();

//        samayKoBharosoKoni();

        createNotificationChannel();

//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//
//            }
//
//            @Override
//            public void onActivityStarted(@NonNull Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityResumed(@NonNull Activity activity) {
//                isAppInForeground = true;
//            }
//
//            @Override
//            public void onActivityPaused(@NonNull Activity activity) {
//                isAppInForeground = false;
//
//            }
//
//            @Override
//            public void onActivityStopped(@NonNull Activity activity) {
//
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(@NonNull Activity activity) {
//
//            }
//
//        });
    }

    private void samayKoBharosoKoni() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isAppInForeground) {
                    Toast.makeText(getApplicationContext(), "aage", Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("onlineOffline", "Online");
                            Log.e("lastSeenKaMaamla", "online");
                            Log.e("appIsIn", "foreground");
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(map);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "peeche", Toast.LENGTH_SHORT).show();

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> map = new HashMap<>();

                                LocalDateTime now = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    now = LocalDateTime.now();
                                    hour = now.getHour();
                                    minute = now.getMinute();
                                    second = now.getSecond();
                                    String timeHoRa = "Last seen at " + String.format("%02d", hour) + " : " + String.format("%02d", minute);

                                    map.put("onlineOffline", timeHoRa);
                                    Log.e("lastSeenKaMaamla", timeHoRa);
                                    Log.e("appIsIn", "khatam kr di");
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                    handler.removeCallbacks(this);

                }

                handler.postDelayed(this, 5000); // 5 seconds
            }
        };

        handler.postDelayed(runnable, 5000); // 5 seconds
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(FCM_CHANNEL_ID, "FCM Channel", NotificationManager.IMPORTANCE_DEFAULT);
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
