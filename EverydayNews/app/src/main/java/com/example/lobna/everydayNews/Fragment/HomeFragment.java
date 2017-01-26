package com.example.lobna.everydayNews.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lobna.everydayNews.Activity.DetailsActivity;
import com.example.lobna.everydayNews.Adapter.HomeAdapter;
import com.example.lobna.everydayNews.Listener.NewsListener;
import com.example.lobna.everydayNews.Manager.NewsManager;
import com.example.lobna.everydayNews.Manager.SourcesManager;
import com.example.lobna.everydayNews.Model.News;
import com.example.lobna.everydayNews.Model.Source;
import com.example.lobna.everydayNews.R;
import com.example.lobna.everydayNews.Utilities.Network;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Jan-17.
 */

public class HomeFragment extends Fragment implements NewsListener {

    private ArrayList<Parcelable> allData;
    private ListView newsListView;
    private HomeAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        allData = new ArrayList<>();
        newsListView = (ListView) rootView.findViewById(R.id.homeListView);

        getNews();

        return rootView;
    }

    public void getNews() {
        SourcesManager.getInstance().getSources(this);
    }

    @Override
    public void onDownloadFinished(final ArrayList<Parcelable> data) {
        if (data.size() > 0) {
            if (data.get(0) instanceof Source) {
                // Get news from all sources.
                for (int i = 0; i < data.size(); ++i) {
                    NewsManager.getInstance().getNews(this, ((Source) data.get(i)).getId());
                }
            } else if (data.get(0) instanceof News) {
                this.allData.addAll(data);

                if(newsAdapter == null) {
                    newsAdapter = new HomeAdapter(getContext(), getNewsArrayList(data));
                }else{
                    newsAdapter.addAll(getNewsArrayList(data));
                }

                newsListView.setAdapter(newsAdapter);
                newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent goToDetailsActivity = new Intent(getActivity(), DetailsActivity.class);
                        goToDetailsActivity.putExtra("news", allData.get(position));
                        startActivity(goToDetailsActivity);
                    }
                });
            }
        }
    }

    public ArrayList<News> getNewsArrayList(ArrayList<Parcelable> news){
        ArrayList<News> newsArrayList = new ArrayList<>();

        for(int i=0; i<news.size(); ++i) {
            newsArrayList.add((News)news.get(i));
        }

        return newsArrayList;
    }

    @Override
    public void onDownloadFailed(Exception e) {
        Network.noInternet(getActivity());
    }
}
