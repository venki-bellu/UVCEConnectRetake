package com.venkibellu.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Venkatesh Belavadi on 11-Aug-17.
 */

public class NotificationService extends Service {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceNews;
    DatabaseReference referenceCampus_Says;
    DatabaseReference referenceusers;
    private final String PREFERENECE = "UVCE-prefereceFile-AccountID";
    private String id, newpos;
    private SharedPreferences preference;
    private String campusnoti, newsnoti;

    @Override
    public void onCreate() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceNews = firebaseDatabase.getReference().child("News");
        referenceCampus_Says=firebaseDatabase.getReference().child("Campus Says");
        referenceusers = firebaseDatabase.getInstance().getReference().child("Registered Users");
        preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
        id = preference.getString("ID", "null");
        if(!id.equals("null")) {
            Query query = referenceusers.orderByChild("Google_ID").equalTo(id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            campusnoti = snapshot.child("Notification_Viewed_Campus").getValue().toString();
                            newsnoti = snapshot.child("Notification_Viewed_News").getValue().toString();
                            newpos = snapshot.getKey();
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            referenceNews.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (preference.contains("ID")) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (newsnoti.equals("No")) {
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationService.this);
                                    mBuilder.setContentTitle("UVCE Connect");
                                    mBuilder.setContentText("A new post is added in News Feed.");
                                    mBuilder.setSmallIcon(R.drawable.notification_icon);
                                    mBuilder.setAutoCancel(true);

                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    mBuilder.setSound(alarmSound);

                                    // Creates an explicit intent for an Activity in your app
                                    Intent resultIntent = new Intent(NotificationService.this, LogInPage.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                    stackBuilder.addParentStack(LogInPage.class);
// Adds the Intent that starts the Activity to the top of the stack
                                    stackBuilder.addNextIntent(resultIntent);
                                    PendingIntent resultPendingIntent =
                                            stackBuilder.getPendingIntent(
                                                    0,
                                                    PendingIntent.FLAG_UPDATE_CURRENT
                                            );
                                    mBuilder.setContentIntent(resultPendingIntent);
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    // mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                                    mNotificationManager.notify(1, mBuilder.build());
                                    referenceusers.child(newpos).child("Notification_Viewed_News").setValue("Yes");
                                }

                            }
                        },2000);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

            referenceCampus_Says.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (preference.contains("ID")) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (campusnoti.equals("No")) {
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationService.this);
                                    mBuilder.setContentTitle("UVCE Connect");
                                    mBuilder.setContentText("A new post is added in Campus Says.");
                                    mBuilder.setSmallIcon(R.drawable.notification_icon);
                                    mBuilder.setAutoCancel(true);

                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    mBuilder.setSound(alarmSound);

                                    // Creates an explicit intent for an Activity in your app
                                    Intent resultIntent = new Intent(NotificationService.this, LogInPage.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                    stackBuilder.addParentStack(LogInPage.class);
// Adds the Intent that starts the Activity to the top of the stack
                                    stackBuilder.addNextIntent(resultIntent);
                                    PendingIntent resultPendingIntent =
                                            stackBuilder.getPendingIntent(
                                                    0,
                                                    PendingIntent.FLAG_UPDATE_CURRENT
                                            );
                                    mBuilder.setContentIntent(resultPendingIntent);
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    // mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                                    mNotificationManager.notify(2, mBuilder.build());
                                    referenceusers.child(newpos).child("Notification_Viewed_Campus").setValue("Yes");
                                }

                            }
                        }, 2000);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
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
