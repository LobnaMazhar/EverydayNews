package com.example.lobna.news.Cache;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmedb on 10/28/16.
 */

public class FileCache {

    public static void saveFile(Context context , InputStream input , String fileName){

        try {

            OutputStream output =  context.openFileOutput(fileName, Context.MODE_PRIVATE);
            try {
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = input.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace(); // handle exception, define IOException and others
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {

                input.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }

    public static File getFile(Context context , String url){

        File f = new File(context.getFilesDir(), url);
        return f;

    }

    public static void clearCache(Context context){

        File[] files= context.getFilesDir().listFiles();
        if(files==null)
            return;
        //delete all cache directory files
        for(File f:files){

            Calendar time = Calendar.getInstance();
            time.add(Calendar.DAY_OF_YEAR,-1);
            //I store the required attributes here and delete them
            Date lastModified = new Date(f.lastModified());
            if(lastModified.before(time.getTime()))
                f.delete();
        }
    }
}
