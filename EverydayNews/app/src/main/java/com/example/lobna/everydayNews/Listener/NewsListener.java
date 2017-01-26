package com.example.lobna.everydayNews.Listener;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Jan-17.
 */

public interface NewsListener {
    public void onDownloadFinished(ArrayList<Parcelable> data);

    public void onDownloadFailed(Exception e);
}
