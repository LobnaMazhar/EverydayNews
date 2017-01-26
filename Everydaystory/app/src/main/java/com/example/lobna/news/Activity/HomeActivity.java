package com.example.lobna.news.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.lobna.news.Menu.Menu;
import com.example.lobna.news.R;
import com.example.lobna.news.Utilities.Notification;

public class HomeActivity extends Menu {

    private static boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        firstTime = settings.getBoolean("FIRST_RUN", true);
        if (firstTime) { // if first time to open the app upon installation ,, turn on notifications system ,, why ?? msh naf3 3'er ama yft7 settingsActivity aw yndh Notifications utility :/
            // do the thing for the first time
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", false);
            editor.commit();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String getNotifications = sharedPreferences.getString(getString(R.string.pref_title_news_notification), "true");
            if (getNotifications.equals("true")) {
                Notification.notification(true);
            } else if (getNotifications.equals("false")) {
                Notification.notification(false);
            }
        }
    }
}
