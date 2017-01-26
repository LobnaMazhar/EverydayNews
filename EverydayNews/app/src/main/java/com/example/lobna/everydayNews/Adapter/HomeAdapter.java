package com.example.lobna.everydayNews.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lobna.everydayNews.Manager.ImageManager;
import com.example.lobna.everydayNews.Model.News;
import com.example.lobna.everydayNews.R;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Jan-17.
 */

public class HomeAdapter extends ArrayAdapter<News>{

    private Context context;
    private ArrayList<News> news;

    public HomeAdapter(Context context, ArrayList<News> news) {
        super(context, R.layout.row_home, news);
        this.context = context;
        this.news = news;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_home, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        News newsObject = news.get(position);

        viewHolder.titleTextView.setText(newsObject.getTitle());
        ImageManager.getInstance().displayImage(context, viewHolder.imageView, newsObject.getImageURL(), viewHolder.newsImageProgessBar, ImageManager.FROM_INTERNET);

        return convertView;
    }

    public class ViewHolder{
        ProgressBar newsImageProgessBar;
        ImageView imageView;
        TextView titleTextView;

        ViewHolder(View imagesList){
            newsImageProgessBar = (ProgressBar) imagesList.findViewById(R.id.newsImageProgessBar);
            imageView = (ImageView) imagesList.findViewById(R.id.newsImageView);
            titleTextView = (TextView) imagesList.findViewById(R.id.newsTitleTextView);
        }
    }
}
