package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbay.mbart.bitbayapp.adapters.ExchangeAdapter;
import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.helpers.AllDatabaseHandler;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.AllObjects;
import com.bitbay.mbart.bitbayapp.models.TickerStats;
import com.bitbay.mbart.bitbayapp.helpers.SimpleItemTouchHelperCallback;
import com.bitbay.mbart.bitbayapp.models.ticker.TickerConverter;
import com.bitbay.mbart.bitbayapp.models.stats.StatsConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExchangeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener
{

    //VIEW
    ExchangeAdapter mAdapter;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CoordinatorLayout mCoordinatorLayout;
    FloatingActionButton fab;

    //LOGIC
    AllObjects tempObj;
    TickerStats tickerStats;
    public List<AllObjects> mData = new ArrayList<>();
    public static List <String> cryptoCurrency = new ArrayList<>();
    public static List <String> fiatCurrency = new ArrayList<>();
    private int refresh = 0;
    private static final String TAG = ExchangeFragment.class.getName();

    SendDetailCardViewData sendDetailCardViewData;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);
        mRecyclerView = rootView.findViewById(R.id.rv_recycler_view);

        mCoordinatorLayout = rootView.findViewById(R.id.currency_fragment);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        fab = rootView.findViewById(R.id.fab);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        if (cryptoCurrency.size() == 0 && fiatCurrency.size() == 0){
            addCurrency();
        }
        populateRecyclerView();
        //OnClickListeners

        fab.setOnClickListener(this);

        //GET DATA FROM SQLITE
        getFromDb(mData);

        return rootView;
    }

    public interface SendDetailCardViewData
    {
        void sendData(String fiatCurrency, String cryptoCurrency, String lastRate);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {

            case R.id.fab:
                refresh = 0;
                getTickerStats("BTC", "PLN", 0);
                mRecyclerView.smoothScrollToPosition(View.FOCUS_DOWN);
                break;
        }
    }

    private void populateRecyclerView()
    {
        mAdapter = new ExchangeAdapter(getContext(),getResources().getString(R.string.updated) + " %s " + getResources().getString(R.string.ago), fab, mData, fiatCurrency, cryptoCurrency, new ExchangeAdapter.CurrencyAdapterListener() {
            @Override
            public void cardOnClick(View v, int position)
            {
                sendDetailCardViewData.sendData(mData.get(position).getFiatCurrency(), mData.get(position).getCryptoCurrency(), mData.get(position).getLast());
            }

            @Override
            public void onCryptoSelected(String currency, int position) {
                try
                {
                    if (mData.size() != 0 && !Objects.equals(mData.get(position).getCryptoCurrency(), currency)) {
                        refresh = 1;
                        getTickerStats(currency, mData.get(position).getFiatCurrency(), position);
                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG, " onCryptoSelected", e);
                }
            }

            @Override
            public void onFiatSelected(String currency, int position) {
                try
                {
                    if (mData.size() != 0 && !Objects.equals(mData.get(position).getFiatCurrency(), currency)) {
                        refresh = 1;
                        getTickerStats(mData.get(position).getCryptoCurrency(), currency, position);
                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG, " onFiatSelected", e);
                }

            }

            @Override
            public void donateOnClick()
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://paypal.me/bartoszewskim"));
                startActivity(browserIntent);

            }

        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    @SuppressLint("CheckResult")
    private void getTickerStats(String cryptoCurrency, String fiatCurrency, int position)
    {
        if (!Objects.equals(cryptoCurrency, fiatCurrency))
        {
            BitBayInterface3 bbApi3Service = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
            // Zip all requests with the Function, which will receive the results.
            Observable.zip(bbApi3Service.tickerApi3(),bbApi3Service.statsApi3(), (t, s) -> {
                tickerStats = new TickerStats(
                        TickerConverter.fromJsonString(new Gson().toJson(t)),
                        StatsConverter.fromJsonString(new Gson().toJson(s)));

                if (tickerStats.getTicker().getStatus().equals("Ok") && tickerStats.getStats().getStatus().equals("Ok"))
                {
                    switch(refresh)
                    {
                        case 0:
                            mData.add(new AllObjects(
                                    cryptoCurrency,
                                    fiatCurrency,
                                    0.0,
                                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                    tickerStats.getStats().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getH(),
                                    tickerStats.getStats().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getL(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getRate(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getPreviousRate(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getHighestBid(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getLowestAsk(),
                                    tickerStats.getStats().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getV()));
                            break;
                        case 1:
                            tempObj =  new AllObjects(
                                    cryptoCurrency,
                                    fiatCurrency,
                                    0.0,
                                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                    tickerStats.getStats().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getH(),
                                    tickerStats.getStats().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getL(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getRate(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getPreviousRate(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getHighestBid(),
                                    tickerStats.getTicker().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getLowestAsk(),
                                    tickerStats.getStats().getItems().get(cryptoCurrency.toUpperCase() + "-" + fiatCurrency.toUpperCase()).getV());
                            mData.set(position,tempObj);
                            break;
                        case 2:
                            if (mData.size() != 0)
                            {
                                for (int i = 0; i < mData.size(); i++)
                                {
                                    tempObj =  new AllObjects(
                                            mData.get(i).getCryptoCurrency(),
                                            mData.get(i).getFiatCurrency(),
                                            0.0,
                                            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                            tickerStats.getStats().getItems().get(mData.get(i).getCryptoCurrency().toUpperCase() + "-" + mData.get(i).getFiatCurrency().toUpperCase()).getH(),
                                            tickerStats.getStats().getItems().get(mData.get(i).getCryptoCurrency().toUpperCase() + "-" + mData.get(i).getFiatCurrency().toUpperCase()).getL(),
                                            tickerStats.getTicker().getItems().get(mData.get(i).getCryptoCurrency().toUpperCase() + "-" + mData.get(i).getFiatCurrency().toUpperCase()).getRate(),
                                            tickerStats.getTicker().getItems().get(mData.get(i).getCryptoCurrency().toUpperCase() + "-" + mData.get(i).getFiatCurrency().toUpperCase()).getPreviousRate(),
                                            tickerStats.getTicker().getItems().get(mData.get(i).getCryptoCurrency().toUpperCase() + "-" + mData.get(i).getFiatCurrency().toUpperCase()).getHighestBid(),
                                            tickerStats.getTicker().getItems().get(mData.get(i).getCryptoCurrency().toUpperCase() + "-" + mData.get(i).getFiatCurrency().toUpperCase()).getLowestAsk(),
                                            tickerStats.getStats().getItems().get(mData.get(i).getCryptoCurrency().toUpperCase() + "-" + mData.get(i).getFiatCurrency().toUpperCase()).getV());
                                    mData.set(i,tempObj);
                                }
                            }
                            break;
                    }
                    return String.valueOf(refresh);
                }
                else
                {
                    return tickerStats.getTicker().getErrors()[0];
                }
                
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(status -> {
                        if (status.matches("0|1|2"))
                        {
                            mAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        else
                        {
                            Snackbar mysqlError = Snackbar.make(mRecyclerView, status, Snackbar.LENGTH_LONG);
                            mysqlError.show();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                            },
                            e -> {
                                mSwipeRefreshLayout.setRefreshing(false);
                                Log.e(TAG, "getTickerStats error " + e);
                            }
                    );
        }
    }

    private void addCurrency()
    {
        new Runnable() {
            @Override
            public void run() {
                cryptoCurrency.add("GAME");
                cryptoCurrency.add("LSK");
                cryptoCurrency.add("LTC");
                cryptoCurrency.add("BTC");
                cryptoCurrency.add("BTG");
                cryptoCurrency.add("BCC");
                cryptoCurrency.add("ETH");
                cryptoCurrency.add("DASH");
                cryptoCurrency.add("KZC");
                cryptoCurrency.add("XRP");
                cryptoCurrency.add("XIN");
                cryptoCurrency.add("XMR");
                cryptoCurrency.add("ZEC");
                cryptoCurrency.add("OMG");
                cryptoCurrency.add("GNT");
                cryptoCurrency.add("FTO");
                cryptoCurrency.add("ZRX");
                cryptoCurrency.add("BAT");
                cryptoCurrency.add("REP");
                cryptoCurrency.add("PAY");
                cryptoCurrency.add("NEU");
                cryptoCurrency.add("TRX");
                cryptoCurrency.add("AMLT");
                cryptoCurrency.add("EXY");
                fiatCurrency.add("PLN");
                fiatCurrency.add("EUR");
                fiatCurrency.add("USD");
                fiatCurrency.add("BTC");
                Collections.sort(cryptoCurrency);
                Collections.sort(fiatCurrency);
            }
        }.run();
    }

    private void saveToDb(List<AllObjects> mData)
    {
        new Runnable() {
            @Override
            public void run() {
                AllDatabaseHandler dbHelper = new AllDatabaseHandler(getContext());
                dbHelper.removeAll();
                for (int i=0; i < mData.size(); i++){
                    dbHelper.addToDB(mData.get(i));
                }
                Log.e(TAG, " NEW RUNNABLE " + " saveToDB");
            }
        }.run();

/**
 List<AllObjects> contacts = dbHelper.getAllDB();

 for (AllObjects cn : contacts) {
 String log =" notification " + cn.getLast() + " alarm " + cn.getCryptoCurrency();            // Writing Contacts to log
 Log.d("SELECTED",log);
 }
 **/

    }

    private void getFromDb(List<AllObjects> mData)
    {
        new Runnable(){
            @Override
            public void run() {
                AllDatabaseHandler dbHelper = new AllDatabaseHandler(getContext());
                if (mData.size() == 0){
                    mData.addAll(dbHelper.getAllDB());
                    refreshALL();
                    Log.e(TAG, " NEW RUNNABLE " + " getfromDB");
                }
            }
        }.run();
    }

    private void refreshALL()
    {
        refresh = 2;
        getTickerStats("BTC", "PLN", 0);
    }

    public static String encode(String privateKey, String publicKey, long timestamp, String requestData) throws Exception
    {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        String data = publicKey + String.valueOf(timestamp) + requestData;
        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_key = new SecretKeySpec(privateKey.getBytes("UTF-8"), "HmacSHA512");
        sha512_HMAC.init(secret_key);
        byte[] bytes = sha512_HMAC.doFinal(data.getBytes("UTF-8"));
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    public static String createTransactionID() throws Exception
    {
        return UUID.randomUUID().toString();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            sendDetailCardViewData = (SendDetailCardViewData) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public void onRefresh()
    {
        if (mData.size() >0 ){
            // Fetching data from server
            refreshALL();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onPause()
    {
        Log.e("SELECTED", "OnPause of CurrencyFragment");
        saveToDb(mData);
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.e("SELECTED", "onResume of CurrencyFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.menu_exchange));
    }

}

