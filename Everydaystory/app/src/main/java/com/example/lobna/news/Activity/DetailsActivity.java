package com.example.lobna.news.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lobna.news.Cache.FileCache;
import com.example.lobna.news.Manager.ImageManager;
import com.example.lobna.news.Menu.Menu;
import com.example.lobna.news.Model.News;
import com.example.lobna.news.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DetailsActivity extends Menu {

    private News news;

    private ImageView newsImageView;
    private TextView descriptionTextView;
    private TextView authorTextView;
    private TextView publishedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news = getIntent().getExtras().getParcelable("news");

        Bitmap bitmap = BitmapFactory.decodeFile(FileCache.getFile(this, String.valueOf(news.getImageURL().hashCode())).getAbsolutePath());
        Palette palette = Palette.from(bitmap).generate();
        List<Palette.Swatch> swatches = palette.getSwatches();
        Palette.Swatch swatch = null;
        if (swatches != null && !swatches.isEmpty()) {
            swatch = swatches.get(0);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(swatch.getRgb());
            }
        }

        setContentView(R.layout.activity_details);

        CollapsingToolbarLayout toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar_layout.setContentScrimColor(swatch.getRgb());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(news.getTitle());
        setSupportActionBar(toolbar);

        fillData();
    }

    public void fillData() {
        newsImageView = (ImageView) findViewById(R.id.newsImageView);
        ImageManager.getInstance().displayImage(this, newsImageView, news.getImageURL(), ImageManager.FROM_INTERNET);

        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(news.getDescription());

        authorTextView = (TextView) findViewById(R.id.authorTextView);
        authorTextView.setText(news.getAuthor());

        publishedTextView = (TextView) findViewById(R.id.publishedTextView);
        publishedTextView.setText(news.getPublishedAt());
    }

    public void saveImage(View view){
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Save image")
                .setMessage("Do you want to save this image?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked, do something
                        Bitmap imageBitmap =((BitmapDrawable) newsImageView.getDrawable()).getBitmap();
                        saveImage(imageBitmap, news.getTitle());
                    }
                })
                .setNegativeButton("No", null) //Do nothing on no
                .show();
    }

    public void saveImage(Bitmap imageBitmap, String imageName) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // CREATE FILE
            File root = Environment.getExternalStorageDirectory();
            // CREATE DIRECTORY
            File dir = new File(root.getAbsolutePath() + "/" + getString(R.string.app_name));
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

                    Toast.makeText(this, "Image saved.", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(this, "Can't save the image, please retry and accept to save. :)", Toast.LENGTH_LONG).show();
        }
    }
}
