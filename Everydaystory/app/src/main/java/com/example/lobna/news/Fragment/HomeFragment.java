package com.example.lobna.news.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lobna.news.Activity.DetailsActivity;
import com.example.lobna.news.Adapter.HomeAdapter;
import com.example.lobna.news.Listener.NewsListener;
import com.example.lobna.news.Manager.NewsManager;
import com.example.lobna.news.Model.News;
import com.example.lobna.news.R;
import com.example.lobna.news.Utilities.Network;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Jan-17.
 */

public class HomeFragment extends Fragment implements NewsListener {

    private ListView newsListView;
    private HomeAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        newsListView = (ListView) rootView.findViewById(R.id.homeListView);

        getNews();

        return rootView;
    }

    public void getNews(){
        NewsManager.getInstance().getNews(this);
    }

    @Override
    public void onDownloadFinished(final ArrayList<News> news) {
        newsAdapter = new HomeAdapter(getContext(), news);
        newsListView.setAdapter(newsAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent goToDetailsActivity = new Intent(getActivity(), DetailsActivity.class);
                goToDetailsActivity.putExtra("news", news.get(position));
                startActivity(goToDetailsActivity);
            }
        });
    }

    @Override
    public void onDownloadFailed(Exception e) {
        Network.noInternet(getActivity());
    }
}
