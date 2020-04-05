package com.bitbay.mbart.bitbayapp.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.adapters.RssFeedSourceDialogAdapter;
import com.bitbay.mbart.bitbayapp.helpers.AllDatabaseHandler;
import com.bitbay.mbart.bitbayapp.models.RssSource;

import java.util.List;

public class RssFeedSourceCustomDialog extends DialogFragment
{

    RecyclerView mFragmentRssSourceRecyclerView;
    RssFeedSourceDialogAdapter rssFeedSourceDialogAdapter;
    List<RssSource> rssSourceList;
    List<RssSource> subscribeRssSourceList;
    public RssFeedSourceCustomDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = getActivity().getLayoutInflater().inflate(R.layout.rss_feed_source_dialog, container);

        mFragmentRssSourceRecyclerView = v.findViewById(R.id.rss_feed_source_recycler_view);
        rssFeedSourceDialogAdapter = new RssFeedSourceDialogAdapter(rssSourceList, subscribeRssSourceList, getContext(), new RssFeedSourceDialogAdapter.RssSourceInterface() {

            @Override
            public void checkBoxClick(CheckBox checkBox, int position)
            {
                if (checkBox.isChecked())
                {
                    updateOneRssToDb(rssSourceList.get(position).getRssSource(), rssSourceList.get(position).getRssUrl(), rssSourceList.get(position).getRssLang(), 1);
                    rssSourceList.get(position).setIssubscribe(1);
                }
                else if (!checkBox.isChecked())
                {
                    updateOneRssToDb( rssSourceList.get(position).getRssSource(),rssSourceList.get(position).getRssUrl(), rssSourceList.get(position).getRssLang(), 0);
                    rssSourceList.get(position).setIssubscribe(0);
                }

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mFragmentRssSourceRecyclerView.setLayoutManager(mLayoutManager);
        mFragmentRssSourceRecyclerView.setHasFixedSize(true);
        // specify an adapter (see also next example)
        mFragmentRssSourceRecyclerView.setAdapter(rssFeedSourceDialogAdapter);
        return v;
    }

    public void getRssSourceData(List<RssSource> rssSourceList)
    {
        this.rssSourceList = rssSourceList;
    }

    private void updateOneRssToDb(String rssSource, String rssUrl, String rssLang, int rssIsSubscribe)
    {
        new Runnable() {
            @Override
            public void run() {
                AllDatabaseHandler dbHelper = new AllDatabaseHandler(getContext());
                dbHelper.updateOneRss(rssSource,rssUrl, rssSource,rssLang, rssIsSubscribe);
            }
        }.run();
/*
        AllDatabaseHandler dbHelper = new AllDatabaseHandler(getContext());
        List<RssSource> contacts = dbHelper.getAllRssDB();

        for (RssSource cn : contacts) {
            String log =" update ONE RSS " + cn.getRssUrl() + " source " + cn.getIssubscribe();            // Writing Contacts to log
            Log.e("\nupdate ONE RSS ",log + " RSSISSUBSCRIBE " + rssIsSubscribe + " RSSSOURCE " + rssSource + " RSSURLLINK " + rssUrl);
        }
*/
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onPause()
    {
        dismiss();
        super.onPause();
    }
}
