package com.venkibellu.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Venkatesh Belavadi on 11-Aug-17.
 */

public class NotificationService extends Service {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    public void onCreate() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("NotiTest");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(NotificationService.this);
                mBuilder.setContentTitle("A new post is added");
                mBuilder.setContentText("Please open the app to view post");
                mBuilder.setSmallIcon(R.drawable.cast_ic_notification_1);
                mBuilder.setVibrate(new long[] {1000,1000,1000,1000,1000});

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                mNotificationManager.notify(1, mBuilder.build());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service started...",Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(),"Service stopped...",Toast.LENGTH_SHORT).show();
    }
}
