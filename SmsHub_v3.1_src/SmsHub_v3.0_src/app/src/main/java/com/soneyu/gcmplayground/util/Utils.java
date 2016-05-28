package com.soneyu.gcmplayground.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.soneyu.core.MainActivity;
import com.soneyu.smshub.R;

/**
 * Created by Silico Studio on 5/29/2016.
 */
public class Utils {

    public static void createNotification(Context context, String title, String message, String alert) {

        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent notificationIntent = PendingIntent.getActivity(context,(int) System.currentTimeMillis() ,intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)

                .setSmallIcon(R.mipmap.ic_launcher).
                        setContentTitle(title).
                        setTicker(alert).
                        setContentText(message);

        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());


    }
}
