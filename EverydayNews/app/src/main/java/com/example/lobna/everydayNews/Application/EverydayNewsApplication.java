package com.example.lobna.everydayNews.Application;

import android.app.Application;

/**
 * Created by Lobna on 25-Jan-17.
 */

public class EverydayNewsApplication extends Application {
    private static EverydayNewsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static EverydayNewsApplication getInstance(){
        return instance;
    }
}
