package com.example.lobna.news.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lobna.news.Model.News;
import com.example.lobna.news.R;
import com.squareup.picasso.Picasso;

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

    @NonNull
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
        Picasso.with(context).load(newsObject.getImageURL()).into(viewHolder.imageView);

        return convertView;
    }

    public class ViewHolder{
        ImageView imageView;
        TextView titleTextView;

        ViewHolder(View imagesList){
            imageView = (ImageView) imagesList.findViewById(R.id.newsImageView);
            titleTextView = (TextView) imagesList.findViewById(R.id.newsTitleTextView);
        }
    }
}
