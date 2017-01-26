package com.example.lobna.everydayNews.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.lobna.everydayNews.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Lobna on 26-Jan-17.
 */

public class Image {
    public static void saveImage(Activity activity, Bitmap imageBitmap, String imageName) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // CREATE FILE
            File root = Environment.getExternalStorageDirectory();
            // CREATE DIRECTORY
            File dir = new File(root.getAbsolutePath() + "/" + activity.getString(R.string.app_name));
            boolean mkdir = true;
            if (!dir.exists()) {
                mkdir = dir.mkdir();
            }

            File file = new File(dir, imageName + ".jpg");

            if (mkdir) {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.close();

                    Toast.makeText(activity, "Image saved.", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(activity, "Can't save the image, please retry and accept to save. :)", Toast.LENGTH_LONG).show();
        }
    }
}
