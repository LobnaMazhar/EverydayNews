package com.example.lobna.everydayNews.Activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lobna.everydayNews.Cache.FileCache;
import com.example.lobna.everydayNews.Manager.ImageManager;
import com.example.lobna.everydayNews.Menu.Menu;
import com.example.lobna.everydayNews.Model.News;
import com.example.lobna.everydayNews.R;
import com.example.lobna.everydayNews.Utilities.Image;

import java.util.List;

public class DetailsActivity extends Menu {

    private News news;

    private ImageView newsImageView;
    private TextView descriptionTextView;
    private TextView authorTextView;
    private TextView publishedTextView;
    private TextView sourceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news = getIntent().getExtras().getParcelable("news");

        Bitmap bitmap = BitmapFactory.decodeFile(FileCache.getFile(this, String.valueOf(news.getImageURL().hashCode())).getAbsolutePath());
        Palette.Swatch swatch = null;
        if (bitmap != null) {
            Palette palette = Palette.from(bitmap).generate();
            List<Palette.Swatch> swatches = palette.getSwatches();
            if (swatches != null && !swatches.isEmpty()) {
                swatch = swatches.get(0);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setStatusBarColor(swatch.getRgb());
                }
            }
        }

        setContentView(R.layout.activity_details);

        if (bitmap != null) {
            CollapsingToolbarLayout toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            toolbar_layout.setContentScrimColor(swatch.getRgb());
        }

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
        if (news.getAuthor() != null)
            authorTextView.setText("Author: " + news.getAuthor());

        publishedTextView = (TextView) findViewById(R.id.publishedTextView);
        publishedTextView.setText("Published at: " + news.getPublishedAt());

        sourceTextView = (TextView) findViewById(R.id.sourceTextView);
        sourceTextView.setText("Source: " + news.getSource().getId());
    }

    public void saveImage(View view) {
        if (newsImageView.getDrawable() != null) {
            //Put up the Yes/No message box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Save image")
                    .setMessage("Do you want to save this image?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            Bitmap imageBitmap = ((BitmapDrawable) newsImageView.getDrawable()).getBitmap();
                            Image.saveImage(DetailsActivity.this, imageBitmap, news.getTitle());
                        }
                    })
                    .setNegativeButton("No", null) //Do nothing on no
                    .show();
        } else {
            Toast.makeText(DetailsActivity.this, "No image to be saved", Toast.LENGTH_LONG).show();
        }
    }
}
