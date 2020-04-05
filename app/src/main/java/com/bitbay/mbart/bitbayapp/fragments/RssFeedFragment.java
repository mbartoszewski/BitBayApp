package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.adapters.RssFeedAdapter;
import com.bitbay.mbart.bitbayapp.activities.WebViewActivity;
import com.bitbay.mbart.bitbayapp.helpers.AllDatabaseHandler;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.RSSFeed;
import com.bitbay.mbart.bitbayapp.models.RSSItem;
import com.bitbay.mbart.bitbayapp.models.RssSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RssFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    RecyclerView mFragmentRssFeedRecyclerView;
    RssFeedAdapter rssFeedAdapter;
    TextView newsSourcesNotFound;
    List<RSSItem> RSSData = new ArrayList<>();
    List<RssSource> rssSourceList = new ArrayList<>();
    sendDataToRssDialog sendDataToRssDialog;
    WebView rssWebView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean isExist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_rss_feed, container, false);
        mFragmentRssFeedRecyclerView = rootView.findViewById(R.id.fragment_rss_feed_recycler_view);
        rssWebView = rootView.findViewById(R.id.fragment_rss_feed_web_view);
        mSwipeRefreshLayout = rootView.findViewById(R.id.rss_swipe_refresh);
        newsSourcesNotFound = rootView.findViewById(R.id.news_source_not_found_text_view);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        getRssFromDb(rssSourceList);
        getAllRssData(rssSourceList);

        populateRssRecyclerView();
        setHasOptionsMenu(true);

        return rootView;
    }

    public interface sendDataToRssDialog
    {
        void sendRssSourceData(List<RssSource> rssSourceList);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            sendDataToRssDialog = (RssFeedFragment.sendDataToRssDialog) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    public static String convertStringToTimestamp(String date, String source)
    {
        DateFormat formatter;
        try {
            switch (source)
            {
                case "bitcoinmagazine":
                    formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss  Z", Locale.US);
                    break;
                case "youtube":
                    formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
                    break;
                    default:
                        formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.US);
                        break;
            }
            // you can change format of date
            Date timestampDate = formatter.parse(date);
            return String.valueOf(timestampDate.getTime());
        } catch (ParseException e)
        {
            return "Error";
        }

    }

    private void populateRssRecyclerView()
    {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mFragmentRssFeedRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        rssFeedAdapter = new RssFeedAdapter(RSSData, getContext(), new RssFeedAdapter.onClickListenerInterface() {
            @Override
            public void rssOnClickListener(String url)
            {
                //mFragmentRssFeedRecyclerView.setVisibility(View.GONE);
                //rssWebView.setVisibility(View.VISIBLE);
                Intent webView = new Intent(getContext(), WebViewActivity.class);
                webView.putExtra("url", url);
                startActivity(webView);
                //rssWebView.loadUrl(url);
                //rssWebView.setWebViewClient(new WebViewClient());
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mFragmentRssFeedRecyclerView.setLayoutManager(mLayoutManager);
        mFragmentRssFeedRecyclerView.setHasFixedSize(true);
        mFragmentRssFeedRecyclerView.addItemDecoration(dividerItemDecoration);
        mFragmentRssFeedRecyclerView.setAdapter(rssFeedAdapter);
    }

    private void getRssFromDb(List<RssSource> rssSourceList)
    {
        new Runnable(){
            @Override
            public void run() {
                AllDatabaseHandler dbHelper = new AllDatabaseHandler(getContext());
                rssSourceList.addAll(dbHelper.getAllRssDB());
            }
        }.run();
    }

    private void getAllRssData(List<RssSource> subscribeRssSourceList)
    {
        if (subscribeRssSourceList.size() > 0 )
        {
            for (int i =0; i < subscribeRssSourceList.size(); i++)
            {
                if (subscribeRssSourceList.get(i).getIssubscribe() != 0)
                {
                    getRSSFeed(subscribeRssSourceList.get(i).getRssUrl(), subscribeRssSourceList.get(i).getRssSource());
                }
            }
        }
        else
        {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressLint("CheckResult")
    private void getRSSFeed(String rssUrl, String rssSource)
    {
        BitBayInterface3 rssFeedService = BitBayInterface3.retrofitXML.create(BitBayInterface3.class);
        Observable<RSSFeed> rssFeedObservable = rssFeedService.getProducts(rssUrl);
        rssFeedObservable
                .subscribeOn(Schedulers.io())
                .map(rssFeed ->
                {
                    if (rssFeed !=null && rssFeed.getItem() != null)
                    {
                        for (int i =0; i < rssFeed.getItem().size(); i++)
                        {
                            isExist = RSSData.contains(rssFeed.getItem().get(i));
                            if (!isExist)
                            {
                                RSSData.add(i, rssFeed.getItem().get(i));
                                RSSData.get(i).setPubDate(convertStringToTimestamp(String.valueOf(rssFeed.getItem().get(i).getPubDate()), rssSource));
                                RSSData.get(i).setSource(rssSource);
                                RSSData.get(i).setRssSourceUrl(rssUrl.substring(0, rssUrl.lastIndexOf("/")));
                                Collections.sort(RSSData);
                            }
                        }
                        return "true";
                    }
                    else
                    {
                        return "Error";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status ->
                {
                    if (status.equals("true"))
                    {
                        rssFeedAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    else
                    {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Snackbar rssFeedError = Snackbar.make(mFragmentRssFeedRecyclerView, status, Snackbar.LENGTH_SHORT);
                        rssFeedError.show();
                    }
                }, e ->
                {
                });
    }

    @Override
    public void onRefresh()
    {
        getAllRssData(rssSourceList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.nav_menu_rss_sources:
               sendDataToRssDialog.sendRssSourceData(rssSourceList);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.nav_menu_rss_sources).setVisible(true);
        menu.findItem(R.id.nav_menu_account).setVisible(false);
        menu.findItem(R.id.nav_menu_bitbay_app).setVisible(false);
        menu.findItem(R.id.nav_menu_market).setVisible(false);
    }

    @Override
    public void onResume()
    {
        Log.e("SELECTED", "onResume of DetailCardViewFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.menu_rss_feed));
    }
}
