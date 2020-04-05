package com.bitbay.mbart.bitbayapp.dialogs;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.CryptoAddressGenerated;
import com.bitbay.mbart.bitbayapp.models.CryptoDeposit;
import com.bitbay.mbart.bitbayapp.models.fiatDeposit.FiatDeposit;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;


public class DepositCustomDialog extends DialogFragment implements View.OnClickListener
{

    TextView cryptoDepositAddressTextView, cryptoDepositPaymentIDTextView;
    ConstraintLayout fiatDepositConstraint, cryptoDepositConstraint, cryptoDepositPaymentIDConstraint;
    ImageButton cryptoDepositAddressCopy, cryptoDepositAddressShare, cryptoPaymentIDCopy, cryptoPaymentIDShare;
    RecyclerView mFragmentWalletRecyclerView;
    ClipboardManager clipboardManager;
    ClipData clip;
    Snackbar copiedSnackbar;
    String walletID, depositAddress, paymentID, currencyCode;
    private static final String TAG = DepositCustomDialog.class.getName();
    Intent shareIntent;

    public DepositCustomDialog()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = getActivity().getLayoutInflater().inflate(R.layout.deposit_dialog, container);

        fiatDepositConstraint = v.findViewById(R.id.fiat_deposit_constraint);

        cryptoDepositConstraint = v.findViewById(R.id.crypto_deposit_constraint);
        cryptoDepositPaymentIDConstraint = v.findViewById(R.id.crypto_deposit_paymentID_constraint);
        cryptoDepositAddressTextView = v.findViewById(R.id.crypto_deposit_address_text);
        cryptoDepositPaymentIDTextView = v.findViewById(R.id.crypto_deposit_paymentID_text);
        cryptoDepositAddressCopy = v.findViewById(R.id.crypto_deposit_address_copy);
        cryptoDepositAddressShare = v.findViewById(R.id.crypto_deposit_address_share);
        cryptoPaymentIDCopy = v.findViewById(R.id.crypto_deposit_paymentId_copy);
        cryptoPaymentIDShare = v.findViewById(R.id.crypto_deposit_paymentId_share);

        if (!currencyCode.matches("PLN|USD|EUR"))
        {
            String requestData = "{\"currency\":\""+ currencyCode + "\"}";
            getCryptoDepositAddress(publicKey, privateKey, walletID, requestData);
            cryptoDepositConstraint.setVisibility(View.VISIBLE);
        }
        else if (currencyCode.matches("PLN|USD|EUR"))
        {
            getFiatDepositAdress(publicKey, privateKey, currencyCode);
            fiatDepositConstraint.setVisibility(View.VISIBLE);
        }
        // Do all the stuff to initialize your custom view
        shareIntent = new Intent(Intent.ACTION_SEND);
        clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);

        cryptoDepositAddressCopy.setOnClickListener(this);
        cryptoDepositAddressShare.setOnClickListener(this);
        cryptoPaymentIDCopy.setOnClickListener(this);
        cryptoPaymentIDShare.setOnClickListener(this);

        return v;
    }

    public void getDepositData(String walletID, String currencyCode, RecyclerView mFragmentWalletRecyclerView)
    {
        this.walletID = walletID;
        this.currencyCode = currencyCode;
        this.mFragmentWalletRecyclerView = mFragmentWalletRecyclerView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.crypto_deposit_address_copy:
                clip = ClipData.newPlainText(currencyCode, depositAddress);
                clipboardManager.setPrimaryClip(clip);
                copiedSnackbar = Snackbar.make(mFragmentWalletRecyclerView, getActivity().getResources().getIdentifier("copy_wallet_addres", "string", getActivity().getPackageName()), Snackbar.LENGTH_SHORT);
                copiedSnackbar.show();
                break;
            case R.id.crypto_deposit_address_share:
                shareIntent.setType("text/*");// You Can set source type here like video, image text, etc.
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, currencyCode + " deposit adress "+ depositAddress);
                shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(Intent.createChooser(shareIntent, "Share Deposit Address Using:"));
                break;
            case R.id.crypto_deposit_paymentId_copy:
                clip = ClipData.newPlainText(currencyCode, paymentID.substring(4));
                clipboardManager.setPrimaryClip(clip);
                copiedSnackbar = Snackbar.make(mFragmentWalletRecyclerView, getActivity().getResources().getIdentifier("copy_wallet_addres", "string", getActivity().getPackageName()), Snackbar.LENGTH_SHORT);
                copiedSnackbar.show();
                break;
            case R.id.crypto_deposit_paymentId_share:
                shareIntent.setType("text/*");// You Can set source type here like video, image text, etc.
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, currencyCode + " deposit adress "+ paymentID.substring(4));
                shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(Intent.createChooser(shareIntent, "Share Deposit Address Using:"));
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void getCryptoDepositAddress(String publicKey, String privateKey, String walletID, String requestData)
    {

        String apiHash = null;
        String uid = null;
        try
        {
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
            uid = createTransactionID();
        }
        catch (Exception e)
        {

        }
        BitBayInterface3 cryptoDepositService = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
        io.reactivex.Observable <CryptoDeposit>  cryptoDepositObservable = cryptoDepositService.cryptoDepositAddressApi3(walletID, publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid);

        cryptoDepositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cryptoDepositAddress -> {
                    if (cryptoDepositAddress.getStatus().equals("Ok"))
                    {
                        if (cryptoDepositAddress.getData().contains("?dt="))
                        {
                            cryptoDepositPaymentIDConstraint.setVisibility(View.VISIBLE);
                            paymentID =cryptoDepositAddress.getData().substring(cryptoDepositAddress.getData().indexOf("?dt="),cryptoDepositAddress.getData().length());
                            depositAddress = cryptoDepositAddress.getData().substring(0, cryptoDepositAddress.getData().indexOf("?dt="));

                            cryptoDepositAddressTextView.setText(depositAddress);
                            cryptoDepositPaymentIDTextView.setText(paymentID.substring(4));
                            //holder.fiatDepositAddressHint.setText(context.getResources().getIdentifier("deposit_payment_id_hint", "string", context.getPackageName()));
                            //holder.cryptoDepositAddressConstraint.setVisibility(View.VISIBLE);
                            //holder.fiatDepositAddressConstraint.setVisibility(View.VISIBLE);
                        }
                        else  if (!cryptoDepositAddress.getData().contains("?dt="))
                        {
                            depositAddress = cryptoDepositAddress.getData();
                            cryptoDepositAddressTextView.setText(depositAddress);
                        }
                    }
                    else if (cryptoDepositAddress.getStatus().equals("Fail") && cryptoDepositAddress.getErrors()[0].equals("CRYPTO_ADDRESS_NOT_FOUND"))
                    {
                        cryptoDepositAddressTextView.setText("CRYPTO_ADDRESS_NOT_FOUND");

                        String apiHashh = null;
                        String uidd = null;
                        JsonObject jsonObjectBodyParameter = new JsonObject();
                        try
                        {
                            apiHashh = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), requestData);
                            uidd = createTransactionID();
                            jsonObjectBodyParameter = (new JsonParser()).parse(requestData).getAsJsonObject();
                        }
                        catch (Exception e)
                        {

                        }

                        BitBayInterface3 cryptoAddressGenerate = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
                        Observable <CryptoAddressGenerated> cryptoAddressGeneratedObservable = cryptoAddressGenerate.cryptoAddressGenerateApi3(walletID, publicKey, apiHashh, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uidd, jsonObjectBodyParameter);

                        cryptoAddressGeneratedObservable
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(o -> {
                                    Log.e(TAG, " CRYPTODEPOSIT ADDRESS FAIL NOT FOUND " + o.getStatus() + " ERRORS " + o.getErrors()[0]);
                                        }, e ->
                                {
                                    Log.e(TAG, "CRYPTODEPOSIT ADDRESS GENERATED ERROR WITH OBSERVABLE " + e);

                                });

                    }
                    //notifyItemChanged(position);

                }, e ->
                {
                    Log.e(TAG, "CRYPTODEPOSIT ADDRESS ERROR WITH OBSERVABLE " + e);

                });
    }

    @SuppressLint("CheckResult")
    private void getFiatDepositAdress(String publicKey, String privateKey, String currencySymbol)
    {
        String apiHash = null;
        String uid = null;
        try
        {
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
            uid = createTransactionID();
        }
        catch (Exception e)
        {

        }
        BitBayInterface3 fiatDepositService = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
        io.reactivex.Observable <FiatDeposit>  fiatDepositObservable = fiatDepositService.fiatDepositAddressApi3(currencySymbol, publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid);

        fiatDepositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fiatDepositAdress -> {

                    if (fiatDepositAdress.getStatus().equals("Ok"))
                    {
                        Log.e(TAG, " FIATODEPOSIT ADDRESS" + fiatDepositAdress.getData());

                        //cryptoDepositAdressTextView.setText(walletList.get(position).getCryptoDepositAdress());
                        //depositConstraint.setVisibility(View.VISIBLE);
                    }
                    else if (fiatDepositAdress.getStatus().equals("Fail"))
                    {
                        Log.e(TAG, " FIATODEPOSIT ADDRESS FAIL " + fiatDepositAdress.getErrors()[0]);
                    }
                    //notifyItemChanged(position);
                }, e ->
                {
                    Log.e(TAG, "FIATODEPOSIT ADDRESS E " + e);

                });
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
