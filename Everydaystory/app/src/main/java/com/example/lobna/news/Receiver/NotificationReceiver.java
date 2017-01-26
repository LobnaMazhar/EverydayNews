package com.example.lobna.news.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.example.lobna.news.Activity.HomeActivity;
import com.example.lobna.news.Model.News;
import com.example.lobna.news.R;

/**
 * Created by Lobna on 09-Jan-17.
 */

public class NotificationReceiver extends BroadcastReceiver{

    public static int requestCode = 100;
    private News news;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // action resulted from clicking on Notification
        Intent openNewsIntent;
        openNewsIntent = new Intent(context, HomeActivity.class);
        openNewsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Even if the app is opened (same news or any other activity) ,, the news notification opening will replace any opened view.

        // Show intent into the notification
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, openNewsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Let's check the recent news.")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        notificationManager.notify(requestCode, notificationBuilder.build());
    }
}
