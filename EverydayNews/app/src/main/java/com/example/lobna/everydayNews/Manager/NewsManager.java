package com.example.lobna.everydayNews.Manager;

import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;

import com.example.lobna.everydayNews.Application.EverydayNewsApplication;
import com.example.lobna.everydayNews.BuildConfig;
import com.example.lobna.everydayNews.Listener.NewsListener;
import com.example.lobna.everydayNews.Model.News;
import com.example.lobna.everydayNews.Model.Source;
import com.example.lobna.everydayNews.R;
import com.example.lobna.everydayNews.Utilities.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lobna on 25-Jan-17.
 */

public class NewsManager {
    private static NewsManager instance;
    private static Handler handler;

    private ArrayList<Parcelable> news;

    public static NewsManager getInstance() {
        if(instance == null){
            NewsManager.instance = new NewsManager();
            NewsManager.handler = new Handler();
        }
        return instance;
    }

    public void getNews(final NewsListener newsListener, final String sourceId){
        if(!Network.networkConnectivity()){
            newsListener.onDownloadFailed(new Exception(EverydayNewsApplication.getInstance().getApplicationContext().getString(R.string.noInternetException)));
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection httpURLConnection = null;
                    BufferedReader bufferedReader = null;
                    final String newsJSONStr;
                    try {
                        String baseURL = EverydayNewsApplication.getInstance().getString(R.string.baseArticlesURL);
                        Uri builtURI = Uri.parse(baseURL).buildUpon().appendQueryParameter("apiKey", BuildConfig.API_KEY).appendQueryParameter("source", sourceId).build();

                        URL url = new URL(builtURI.toString());

                        // open connection
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.connect();

                        InputStream inputStream = httpURLConnection.getInputStream();
                        if(inputStream == null)
                            return;
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        StringBuffer stringBuffer = new StringBuffer();
                        String line;
                        while((line = bufferedReader.readLine()) != null)
                            stringBuffer.append(line + "\n");

                        if(stringBuffer.length() == 0)
                            return;

                        newsJSONStr = stringBuffer.toString();

                        getNewsFromJSONStr(newsJSONStr);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                newsListener.onDownloadFinished(news);
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(httpURLConnection != null)
                            httpURLConnection.disconnect();
                    }
                }
            }).start();
        }
    }

    public void getNewsFromJSONStr(String newsJSONStr){
        news = new ArrayList<>();
        try{
            JSONObject data = new JSONObject(newsJSONStr);
            if(data.getString("status").equals("ok")){
                Source source = new Source();
                source.setId(data.getString("source"));
                JSONArray articles = data.getJSONArray("articles");
                for(int i=0; i<articles.length(); ++i){
                    JSONObject current = articles.getJSONObject(i);

                    News news = new News();

                    news.setAuthor(current.getString("author"));
                    news.setTitle(current.getString("title"));
                    news.setDescription(current.getString("description"));
                    news.setImageURL(current.getString("urlToImage"));
                    news.setPublishedAt(current.getString("publishedAt"));
                    news.setSource(source);

                    this.news.add(news);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
