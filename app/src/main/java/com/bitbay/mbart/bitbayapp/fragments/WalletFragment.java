package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.adapters.WalletAdapter;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.Dashboard;
import com.bitbay.mbart.bitbayapp.models.ticker.TickerConverter;
import com.bitbay.mbart.bitbayapp.models.wallet.Balance;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;

public class WalletFragment extends Fragment
{
    WalletAdapter mWalletAdapter;
    RecyclerView mFragmentWalletRecyclerView;
    List<Balance> walletList = new ArrayList<>();
    String currencySign = "-EUR";
    Dashboard dashboardObject;
    sendDataToDepositDialog sendDataToDepositDialog;
    private static final String TAG = WalletFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        mFragmentWalletRecyclerView = rootView.findViewById(R.id.fragment_wallet_recycler_view);

        mWalletAdapter = new WalletAdapter(getContext(), walletList, mFragmentWalletRecyclerView, new WalletAdapter.walletAdapterListener() {
            @Override
            public void onDepositButtonClicked(String walletID, int position)
            {
                sendDataToDepositDialog.sendDepositData(walletID, walletList.get(position).getCurrency(), mFragmentWalletRecyclerView);
            }

            @Override
            public void onWithdrawButtonClicked(String walletID, int position) {
                sendDataToDepositDialog.sendWithdrawData(walletID, walletList.get(position).getCurrency(), mFragmentWalletRecyclerView);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mFragmentWalletRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mFragmentWalletRecyclerView.setLayoutManager(mLayoutManager);
        mFragmentWalletRecyclerView.setHasFixedSize(true);
        mFragmentWalletRecyclerView.addItemDecoration(dividerItemDecoration);
        // specify an adapter (see also next example)
        mFragmentWalletRecyclerView.setAdapter(mWalletAdapter);
        getWallet(publicKey, privateKey, "");

        return rootView;
    }

    public interface sendDataToDepositDialog
    {
        void sendDepositData(String walletID, String currencyCode, RecyclerView mFragmentWalletRecyclerView);
        void sendWithdrawData(String walletID, String currencyCode,RecyclerView mFragmentWalletRecyclerView);
    }

    @SuppressLint("CheckResult")
    private void getWallet(String publicKey, String privateKey, String requestData)
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Fetching Data","Please wait...",false,false);

        try
        {
                String apiHash = null;
                String uid = null;
                apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
                uid = createTransactionID();
                BitBayInterface3 dashboardService = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
                // Zip all requests with the Function, which will receive the results.

                Observable.zip(dashboardService.balanceListsApi3(publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid), dashboardService.tickerApi3(), (b, t) -> {

                    dashboardObject = new Dashboard(b, TickerConverter.fromJsonString(new Gson().toJson(t)));
                    if (dashboardObject.getBalances().getStatus().equals("Ok") && dashboardObject.getBalances().getBalances().size() > 0)
                    {
                        walletList.clear();
                        loading.dismiss();
                        walletList.addAll(dashboardObject.getBalances().getBalances());

                        for (int i =0; i < walletList.size(); i++)
                        {
                            if (!walletList.get(i).getCurrency().matches("PLN|USD|EUR"))
                            {
                                switch (String.valueOf(Locale.getDefault()))
                                {
                                    case "en_US":
                                        currencySign = "-USD";
                                        break;
                                    case "pl_PL":
                                        currencySign = "-PLN";
                                        break;
                                    default:
                                        currencySign = "-EUR";
                                        break;
                                }
                                try
                                {
                                    walletList.get(i).setRate(Double.parseDouble(dashboardObject.getTicker().getItems().get(walletList.get(i).getCurrency().toUpperCase() + currencySign).getRate()));
                                }
                                catch (Exception e)
                                {
                                    walletList.get(i).setRate(1);
                                }
                            }
                            else
                            {
                                walletList.get(i).setRate(1);
                            }
                        }
                        return "true";
                    }
                    else
                    {
                        return dashboardObject.getBalances().getErrors()[0];
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( status ->
                                {
                                   if (status.equals("true"))
                                   {
                                       mWalletAdapter.notifyDataSetChanged();
                                   }
                                   else
                                   {
                                       Snackbar connectionError = Snackbar.make(mFragmentWalletRecyclerView, status, Snackbar.LENGTH_LONG);
                                       connectionError.show();
                                       loading.dismiss();
                                   }
                                    //mFragmentWalletRecyclerView.getRecycledViewPool().clear();
                                },
                                e -> {
                            loading.dismiss();
                                    Log.e(TAG, "dashboard ERROR Z RXJAVA" + e);
                                }
                        );
            }
            catch (Exception e)
            {
                Log.e(TAG, "dashboard ERROR  Z CATCH" + e);
            }
        }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            sendDataToDepositDialog = (WalletFragment.sendDataToDepositDialog) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public void onPause()
    {
        Log.e("SELECTED", "OnPause of WalletFragment");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.e("SELECTED", "onResume of WalletFragment");
        super.onResume();
        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.menu_wallet));
    }
}

