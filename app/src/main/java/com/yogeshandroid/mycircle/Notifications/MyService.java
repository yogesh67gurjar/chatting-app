package com.yogeshandroid.mycircle.Notifications;

import static com.yogeshandroid.mycircle.Notifications.Channel.FCM_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yogeshandroid.mycircle.R;

public class MyService extends FirebaseMessagingService {

    public static final String TAG = "TAG_MyService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d(TAG, "onMessageReceived: ");

        Log.d(TAG, "Message Recieved From " + message.getFrom());

        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();

//            Intent pi=new Intent(this, MainActivity.class);
//            PendingIntent conte=PendingIntent.getActivity(this, 0, pi, PendingIntent.FLAG_IMMUTABLE);

            Notification notitication = new NotificationCompat.Builder(this, FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.smiling)
                    .setContentTitle(title)
                    .setContentText(body)
//                    .setContentIntent()
                    .setColor(Color.BLUE)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(10024, notitication);
        }
        if (message.getData().size() > 0) {
            Log.d(TAG + "DATA MESSAGE", message.getData().toString());
            for (String key : message.getData().keySet()) {
                Log.d(TAG + " Data Message ", message.getData().get(key));
            }
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages: ");
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: "+token);
    }
}