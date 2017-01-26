package com.example.lobna.everydayNews.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.example.lobna.everydayNews.Application.EverydayNewsApplication;
import com.example.lobna.everydayNews.Cache.FileCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lobna on 25-Jan-17.
 */

public class Network {
    public static boolean networkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) EverydayNewsApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void noInternet(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Internet connection");
        builder.setMessage("Connect to a network");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public static void downloadBitmap(String url, Context context) throws IOException {

        HttpURLConnection urlConnection = null;
        URL uri = new URL(url);
        urlConnection = (HttpURLConnection) uri.openConnection();
        int statusCode = urlConnection.getResponseCode();


        InputStream inputStream = urlConnection.getInputStream();
        if (inputStream != null) {
            FileCache.saveFile(context, inputStream, String.valueOf(url.hashCode()));
        }
    }
}
