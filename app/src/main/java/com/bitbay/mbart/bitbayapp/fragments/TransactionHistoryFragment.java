package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.adapters.TransactionHistoryAdapter;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.transactionHistory.Item;
import com.bitbay.mbart.bitbayapp.models.transactionHistory.TransactionHistory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.cryptoCurrency;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.fiatCurrency;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;

public class TransactionHistoryFragment extends Fragment
{
    Spinner fiatSpinner, cryptoSpinner;
    ArrayAdapter<String> cryptoSpinnerAdapter, fiatSpinnerAdapter;
    RecyclerView transactionHistoryRecycler;
    TransactionHistoryAdapter transactionHistoryAdapter;
    TextView noHistoryText;
    String nextPageCursor = "start";
    ArrayList<Item> transactionHistoryList = new ArrayList<>();
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        fiatSpinner = rootView.findViewById(R.id.fiat_spinner);
        cryptoSpinner = rootView.findViewById(R.id.crypto_spinner);
        transactionHistoryRecycler = rootView.findViewById(R.id.transaction_history_recycler);
        noHistoryText = rootView.findViewById(R.id.no_history_text);

        fiatSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fiatCurrency);
        cryptoSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cryptoCurrency);
        fiatSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cryptoSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fiatSpinner.setAdapter(fiatSpinnerAdapter);
        cryptoSpinner.setAdapter(cryptoSpinnerAdapter);

        populateRecycler();

        cryptoSpinner.setSelection(cryptoSpinnerAdapter.getPosition("BTC"));
        fiatSpinner.setSelection(fiatSpinnerAdapter.getPosition("USD"));

        fiatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (transactionHistoryList.size() > 0)
                {
                    if (!transactionHistoryList.get(0).getMarket().equals(cryptoSpinner.getSelectedItem().toString() + "-" + fiatSpinner.getSelectedItem().toString()))
                    {
                        transactionHistoryList.clear();
                    }
                }
                getTransactionHistory(publicKey, privateKey, "{\"markets\":[\""+ cryptoSpinner.getSelectedItem().toString() + "-" + fiatSpinner.getSelectedItem().toString() +"\"],\"nextPageCursor\":\"start\"}");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cryptoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (transactionHistoryList.size() > 0)
                {
                    if (!transactionHistoryList.get(0).getMarket().equals(cryptoSpinner.getSelectedItem().toString() + "-" + fiatSpinner.getSelectedItem().toString()))
                    {
                        transactionHistoryList.clear();
                    }
                }
                getTransactionHistory(publicKey, privateKey, "{\"markets\":[\""+ cryptoSpinner.getSelectedItem().toString() + "-" + fiatSpinner.getSelectedItem().toString() +"\"],\"nextPageCursor\":\"start\"}");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return rootView;
    }

    private void populateRecycler()
    {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(transactionHistoryRecycler.getContext(),
                DividerItemDecoration.VERTICAL);
        transactionHistoryAdapter = new TransactionHistoryAdapter(getContext(), transactionHistoryList);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getContext());
        transactionHistoryRecycler.setLayoutManager(LinearLayoutManager);
        transactionHistoryRecycler.setHasFixedSize(true);
        transactionHistoryRecycler.addItemDecoration(dividerItemDecoration);
        transactionHistoryRecycler.setAdapter(transactionHistoryAdapter);
        transactionHistoryRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                {
                    visibleItemCount = LinearLayoutManager.getChildCount();
                    totalItemCount = LinearLayoutManager.getItemCount();
                    pastVisiblesItems = LinearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = true;
                            getTransactionHistory(publicKey, privateKey, "{\"markets\":[\""+ cryptoSpinner.getSelectedItem().toString() + "-" + fiatSpinner.getSelectedItem().toString() +"\"],\"nextPageCursor\":\""+ nextPageCursor +"\"}");
                        }
                    }
                }
            }
        });
    }
    @SuppressLint("CheckResult")
    private void getTransactionHistory(String publicKey, String privateKey, String requestData)
    {
        BitBayInterface3 restApi3Service = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
        String apiHash = null;
        String uid = null;
        try {
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
            uid = createTransactionID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Observable<TransactionHistory> transactionHistoryObservable = restApi3Service.transactionHistoryApi3(publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid, requestData);
        transactionHistoryObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chart ->
                {
                        if (chart.getStatus().equals("Ok"))
                        {
                            if (chart.getTotalRows() > 0 && !chart.getNextPageCursor().equals(nextPageCursor))
                            {
                                nextPageCursor = chart.getNextPageCursor();
                                transactionHistoryRecycler.setVisibility(View.VISIBLE);
                                noHistoryText.setVisibility(View.GONE);
                                for (int i = 0; i < chart.getItems().length; i++)
                                {
                                    transactionHistoryList.add(chart.getItems()[i]);
                                }
                                transactionHistoryAdapter.notifyDataSetChanged();
                            }
                            if (chart.getTotalRows() == 0)
                            {
                                transactionHistoryList.clear();
                                transactionHistoryRecycler.setVisibility(View.GONE);
                                noHistoryText.setVisibility(View.VISIBLE);
                            }
                            loading = false;
                        }

                }, e ->
                {
                    Log.e("TRANSACTIONHISTORY", "Chart SERVICE " + e);

                });
    }

    @Override
    public void onResume() {
        Log.e("SELECTED", "onResume of TRANSACTIONHISTORYFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.transaction_history_fragment_name));
    }
    @Override
    public void onPause() {
        Log.e("SELECTED", "onPause of TRANSACTIONHISTORYFragment");
        super.onPause();
    }
}
