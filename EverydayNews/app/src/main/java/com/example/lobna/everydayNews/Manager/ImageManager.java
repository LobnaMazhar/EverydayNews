package com.example.lobna.everydayNews.Manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.lobna.everydayNews.Cache.FileCache;
import com.example.lobna.everydayNews.Cache.MemoryCache;
import com.example.lobna.everydayNews.R;
import com.example.lobna.everydayNews.Utilities.Network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lobna on 26-Jan-17.
 */

public class ImageManager {

    //use ExecutorService instead of threads and asyncTask
    ExecutorService executorService;

    public static final int FROM_INTERNET = 1;
    public static final int FROM_FILES = 2;

    Handler handler;

    Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    MemoryCache memoryCache = new MemoryCache();

    public static ImageManager instance;
    private Context context;

    //get MAx number of threads the device can handle (taken from AsyncTask.java)
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;


    public static ImageManager getInstance() {

        if (instance == null) {
            instance = new ImageManager();
            instance.executorService = Executors.newFixedThreadPool(CORE_POOL_SIZE);
            instance.handler = new Handler();
        }
        return instance;
    }

    public void displayImage(Context context, ImageView imageView, String url, int type) {
        displayImage(context, imageView, url, null , type);
    }


    public void displayImage(Context context, ImageView imageView, String url, ProgressBar progressBar , int type) {

        this.context = context;
        //make progress bar visible
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);

        //assign the url to imageView
        imageViews.put(imageView, url);

        queuePhoto(imageView, url, progressBar , type);
    }

    private void queuePhoto(ImageView imageView, String url, ProgressBar progressBar, int type) {

        PhotoToLoad photoToLoad = new PhotoToLoad(url, imageView, progressBar);
        photoToLoad.type = type;
        //start image viewing process
        executorService.submit(new PhotoLoader(photoToLoad));
    }

    //image viewing runnable
    class PhotoLoader implements Runnable {

        private final PhotoToLoad photoToLoad;

        public PhotoLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {

            try {
                //if image view is reassigned to a new url no need to continue the process
                if (imageViewReused(photoToLoad))
                    return;


                Bitmap bmp = null;

                //first check the memory cache
                bmp = memoryCache.get(String.valueOf(photoToLoad.url.hashCode()));

                //if bmp is found display it and stop the process
                if (bmp != null) {

                    handler.post(new BitmapDisplayer(bmp, photoToLoad));
                    return;
                }
                File file = null;

                if(photoToLoad.type == FROM_INTERNET)
                    //if not found in memory try to find it in file cache
                    file = FileCache.getFile(context, String.valueOf(photoToLoad.url.hashCode()));

                else if(photoToLoad.type == FROM_FILES)
                    file = new File(photoToLoad.url);

                bmp = decodeFile(file);

                //if found display it put it in memory cache and exit the process
                if(bmp != null){

                    memoryCache.put(photoToLoad.url, bmp);
                    handler.post(new BitmapDisplayer(bmp, photoToLoad));
                    return;

                } else {

                    //else download it and cache it in both the file and memory cache
                    Network.downloadBitmap(photoToLoad.url, context);

                    file = FileCache.getFile(context, String.valueOf(photoToLoad.url.hashCode()));

                    bmp = decodeFile(file);

                    memoryCache.put(photoToLoad.url, bmp);
                }

                if (imageViewReused(photoToLoad))
                    return;

                // Get bitmap to display
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);

                // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
                // The runnable will be run on the thread to which this handler is attached.
                // BitmapDisplayer run method will call
                handler.post(bd);

            } catch (Throwable e) {

                BitmapDisplayer bd = new BitmapDisplayer(null, photoToLoad);

                handler.post(bd);

                if(e instanceof OutOfMemoryError)
                    memoryCache.clearCache();
            }
        }
    }

    //decode file to a bitmap and resize the image before viewing it
    private Bitmap decodeFile(File f) {

        try {

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.

            // Set width/height of recreated image
            final int REQUIRED_SIZE = 85;

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with current scale values
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {

        String tag = imageViews.get(photoToLoad.imageView);
        //Check url is already exist in imageViews MAP
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {

        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;

            if (photoToLoad.progressBar != null)
                photoToLoad.progressBar.setVisibility(View.GONE);

            // Show bitmap on UI
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(R.drawable.placeholder);
        }
    }

    private class PhotoToLoad {

        public String url;
        public ImageView imageView;
        public ProgressBar progressBar;
        public int type;

        public PhotoToLoad(String u, ImageView i, ProgressBar p) {
            url = u;
            imageView = i;
            progressBar = p;
        }
    }
}


