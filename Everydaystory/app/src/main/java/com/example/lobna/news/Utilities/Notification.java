package com.example.lobna.news.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.lobna.news.Application.NewsApplication;
import com.example.lobna.news.Receiver.NotificationReceiver;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Lobna on 26-Jan-17.
 */

public class Notification {

    private static Calendar calendar;
    private static Intent intent;
    private static PendingIntent pendingIntent;
    private static AlarmManager alarmManager;
    private static Context appContext;

    public static void notification(boolean notify) {
        appContext = NewsApplication.getInstance().getApplicationContext();

        calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 00 + (int)(Math.random()*24));
        calendar.set(Calendar.MINUTE, 00 + (int)(Math.random()*60));

        intent = new Intent(appContext, NotificationReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(appContext, NotificationReceiver.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) appContext.getSystemService(ALARM_SERVICE);

        if (notify)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        else
            alarmManager.cancel(pendingIntent);
    }
}
