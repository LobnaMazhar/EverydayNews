package com.example.lobna.news.Application;

import android.app.Application;

/**
 * Created by Lobna on 25-Jan-17.
 */

public class NewsApplication extends Application {
    private static NewsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static NewsApplication getInstance(){
        return instance;
    }
}
