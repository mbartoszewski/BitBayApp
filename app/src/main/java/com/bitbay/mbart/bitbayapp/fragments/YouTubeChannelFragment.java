package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.adapters.RssFeedAdapter;
import com.bitbay.mbart.bitbayapp.adapters.YouTubeChannelAdapter;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.RSSItem;
import com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo.Item;
import com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo.YouTubeChannelVideoAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class YouTubeChannelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    RecyclerView mFragmentYouTubeChannelRecyclerView;
    YouTubeChannelAdapter YouTubeChannelAdapter;
    TextView newsSourcesNotFound;
    String youTubeChannelID = "UCo1PJlqUMGYfCnZHX0mDONQ",
            youTubeApiKey = "",
            youTubeUrl = "https://www.youtube.com/watch?v=";
    List<Item> youTubeChannelVideoData = new ArrayList<>();
    WebView rssWebView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_rss_feed, container, false);
        mFragmentYouTubeChannelRecyclerView = rootView.findViewById(R.id.fragment_rss_feed_recycler_view);
        rssWebView = rootView.findViewById(R.id.fragment_rss_feed_web_view);
        mSwipeRefreshLayout = rootView.findViewById(R.id.rss_swipe_refresh);
        newsSourcesNotFound = rootView.findViewById(R.id.news_source_not_found_text_view);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        switch (String.valueOf(Locale.getDefault())) {
            case "en_US":
                youTubeChannelID = "UCo1PJlqUMGYfCnZHX0mDONQ";
                break;
            case "pl_PL":
                youTubeChannelID = "UCGziyXWPUouX-BjhQTZYVow";
                break;
            default:
                youTubeChannelID = "UCo1PJlqUMGYfCnZHX0mDONQ";
                break;
        }
        youTubeChannelVideoData.clear();
        populateRssRecyclerView();
        getYouTubeChannelVideo(youTubeApiKey,youTubeChannelID, "20");
        return rootView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    private void populateRssRecyclerView()
    {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mFragmentYouTubeChannelRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        YouTubeChannelAdapter = new YouTubeChannelAdapter(youTubeChannelVideoData, getContext(), new YouTubeChannelAdapter.onClickListenerInterface() {
            @Override
            public void youTubeVideoOnClickListener(String url)
            {
                Log.e("SELECTED", "onResume of YouTubeChannelFragment " + url);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mFragmentYouTubeChannelRecyclerView.setLayoutManager(mLayoutManager);
        mFragmentYouTubeChannelRecyclerView.setHasFixedSize(true);
        mFragmentYouTubeChannelRecyclerView.addItemDecoration(dividerItemDecoration);
        mFragmentYouTubeChannelRecyclerView.setAdapter(YouTubeChannelAdapter);
    }

    @SuppressLint("CheckResult")
    private void getYouTubeChannelVideo(String apiKey, String channelID, String maxResults)
    {
        Map<String, String> data = new HashMap<>();
        data.put("key", apiKey);
        data.put("channelId", channelID);
        data.put("part", "snippet,id");
        data.put("order", "date");
        data.put("maxResults", maxResults);

        BitBayInterface3 youTubeChannelService = BitBayInterface3.retrofitYouTubeApiV3.create(BitBayInterface3.class);
        Observable<YouTubeChannelVideoAPI> rssFeedObservable = youTubeChannelService.youTubeChannelVideoApi3(data);
        rssFeedObservable
                .subscribeOn(Schedulers.io())
                .map(channelVideoAPI ->
                {
                    if (channelVideoAPI.getItems().length > 0)
                    {
                        for (int i =0; i < channelVideoAPI.getItems().length; i++)
                        {
                            youTubeChannelVideoData.add(channelVideoAPI.getItems()[i]);
                        }
                    }

                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status ->
                {
                    YouTubeChannelAdapter.notifyDataSetChanged();
                }, e ->
                {
                });
    }

    @Override
    public void onRefresh()
    {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.nav_menu_rss_sources).setVisible(false);
        menu.findItem(R.id.nav_menu_account).setVisible(false);
        menu.findItem(R.id.nav_menu_bitbay_app).setVisible(false);
        menu.findItem(R.id.nav_menu_market).setVisible(false);
    }

    @Override
    public void onResume()
    {
        Log.e("SELECTED", "onResume of YouTubeChannelFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.menu_youtube_channel));
    }
}
