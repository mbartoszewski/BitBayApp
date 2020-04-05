package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.adapters.MyOffersAdapter;
import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.openItems.Item;
import com.bitbay.mbart.bitbayapp.models.openItems.OpenItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;

public class MyOffersFragment extends Fragment {

    MyOffersAdapter mOffersAdapter;
    TextView offersNotFound;
    RecyclerView mFragmentOffersRecyclerView;
    List<Item> offersList = new ArrayList<>();


    private static final String TAG = WalletFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_offers, container, false);
        mFragmentOffersRecyclerView = rootView.findViewById(R.id.fragment_my_offers_recycler_view);
        offersNotFound = rootView.findViewById(R.id.offers_not_found_text_view);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mFragmentOffersRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mOffersAdapter = new MyOffersAdapter(getContext(), offersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mFragmentOffersRecyclerView.setLayoutManager(mLayoutManager);
        mFragmentOffersRecyclerView.setHasFixedSize(true);
        mFragmentOffersRecyclerView.addItemDecoration(dividerItemDecoration);
        // specify an adapter (see also next example)
        mFragmentOffersRecyclerView.setAdapter(mOffersAdapter);
        getMyOffers();

        return rootView;
    }

    @SuppressLint("CheckResult")
    private void getMyOffers ()
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Fetching Data","Please wait...",false,false);

        try {
            String apiHash = null;
            String uid = null;
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
            uid = createTransactionID();
            BitBayInterface3 myOffersService = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
            io.reactivex.Observable<OpenItems> orderbookObservable = myOffersService.openItemsApi3(publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid);

            orderbookObservable
                    .subscribeOn(Schedulers.io())
                    .map(objectList -> {
                        offersList.clear();
                        if (objectList.getStatus().equals("Ok") && objectList.getItems().size() != 0)
                        {
                            offersList.addAll(objectList.getItems());
                           return "success";
                        }
                        else if (objectList.getStatus().equals("Ok") && objectList.getItems().size() == 0)
                        {
                            return "empty";
                        }
                        else
                        {
                            return objectList.getErrors()[0];
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(myOffers ->
                    {
                        switch (myOffers)
                        {
                            case "success":
                                mFragmentOffersRecyclerView.setVisibility(View.VISIBLE);
                                mOffersAdapter.notifyDataSetChanged();
                                break;
                            case "empty":
                                offersNotFound.setVisibility(View.VISIBLE);
                                break;
                            default:
                                Snackbar connectionError = Snackbar.make(mFragmentOffersRecyclerView, myOffers, Snackbar.LENGTH_LONG);
                                connectionError.show();
                                break;
                        }
                        loading.dismiss();
                    }, e ->
                    {
                        Log.e(TAG, "ORDERBOOK SERVICE " + e);

                    });
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume()
    {
        Log.e("SELECTED", "onResume of CurrencyFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.menu_offers));
    }
}
