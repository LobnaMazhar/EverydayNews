package com.example.lobna.news.Listener;

import com.example.lobna.news.Model.News;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Jan-17.
 */

public interface NewsListener {
    public void onDownloadFinished(ArrayList<News> news);

    public void onDownloadFailed(Exception e);
}
