package com.venkibellu.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Allan Akshay on 19-07-2017.
 */

public class NotificationHandle extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String content = remoteMessage.getNotification().getBody();
        String feed = remoteMessage.getData().get("Feed");

        final int NOTIFICATION_ID = (feed.equals("News") ? 1 : 0);

        Intent intent = new Intent(this, LogInPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setColor(getResources().getColor(R.color.colorNotification))
                .setSmallIcon(R.drawable.ic_notificaion)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_ID, builder.build());
    }
}