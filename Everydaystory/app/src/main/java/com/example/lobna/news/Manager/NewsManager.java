package com.example.lobna.news.Manager;

import android.net.Uri;
import android.os.Handler;

import com.example.lobna.news.Application.NewsApplication;
import com.example.lobna.news.BuildConfig;
import com.example.lobna.news.Listener.NewsListener;
import com.example.lobna.news.Model.News;
import com.example.lobna.news.R;
import com.example.lobna.news.Utilities.Network;

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

    private ArrayList<News> news;

    public static NewsManager getInstance() {
        if(instance == null){
            NewsManager.instance = new NewsManager();
            NewsManager.handler = new Handler();
        }
        return instance;
    }

    public void getNews(final NewsListener newsListener){
        if(!Network.networkConnectivity()){
            newsListener.onDownloadFailed(new Exception("No internet connection"));
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection httpURLConnection = null;
                    BufferedReader bufferedReader = null;
                    final String newsJSONStr;
                    try {
                        String baseURL = NewsApplication.getInstance().getString(R.string.baseNewsURL);
                        Uri builtURI = Uri.parse(baseURL).buildUpon().appendQueryParameter("apiKey", BuildConfig.API_KEY).build();

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
                JSONArray articles = data.getJSONArray("articles");
                for(int i=0; i<articles.length(); ++i){
                    JSONObject current = articles.getJSONObject(i);

                    News news = new News();

                    news.setAuthor(current.getString("author"));
                    news.setTitle(current.getString("title"));
                    news.setDescription(current.getString("description"));
                    news.setImageURL(current.getString("urlToImage"));
                    news.setPublishedAt(current.getString("publishedAt"));

                    this.news.add(news);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
