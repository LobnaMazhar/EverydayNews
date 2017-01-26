package com.example.lobna.everydayNews.Manager;

import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;

import com.example.lobna.everydayNews.Application.EverydayNewsApplication;
import com.example.lobna.everydayNews.BuildConfig;
import com.example.lobna.everydayNews.Listener.NewsListener;
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
 * Created by Lobna on 26-Jan-17.
 */

public class SourcesManager {
    private static SourcesManager instance;
    private static Handler handler;

    private ArrayList<Parcelable> sources;

    public static SourcesManager getInstance() {
        if (instance == null) {
            SourcesManager.instance = new SourcesManager();
            SourcesManager.handler = new Handler();
        }
        return instance;
    }

    public void getSources(final NewsListener newsListener) {
        if (!Network.networkConnectivity()) {
            newsListener.onDownloadFailed(new Exception(EverydayNewsApplication.getInstance().getApplicationContext().getString(R.string.noInternetException)));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection httpURLConnection = null;
                    BufferedReader bufferedReader = null;
                    final String newsJSONStr;
                    try {
                        String baseURL = EverydayNewsApplication.getInstance().getString(R.string.baseSourcesURL);
                        Uri builtURI = Uri.parse(baseURL).buildUpon().appendQueryParameter("apiKey", BuildConfig.API_KEY).build();

                        URL url = new URL(builtURI.toString());

                        // open connection
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.connect();

                        InputStream inputStream = httpURLConnection.getInputStream();
                        if (inputStream == null)
                            return;
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        StringBuffer stringBuffer = new StringBuffer();
                        String line;
                        while ((line = bufferedReader.readLine()) != null)
                            stringBuffer.append(line + "\n");

                        if (stringBuffer.length() == 0)
                            return;

                        newsJSONStr = stringBuffer.toString();

                        getNewsFromJSONStr(newsListener, newsJSONStr);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                newsListener.onDownloadFinished(sources);
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpURLConnection != null)
                            httpURLConnection.disconnect();
                    }
                }
            }).start();
        }
    }

    public void getNewsFromJSONStr(NewsListener newsListener, String newsJSONStr) {
        sources = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(newsJSONStr);
            if (data.getString("status").equals("ok")) {
                JSONArray sources = data.getJSONArray("sources");
                for (int i = 0; i < sources.length(); ++i) {
                    if(this.sources.size() == 5)
                        break;
                    JSONObject current = sources.getJSONObject(i);

                    Source source = new Source();

                    source.setId(current.getString("id"));

                    this.sources.add(source);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
