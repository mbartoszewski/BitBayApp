package com.bitbay.mbart.bitbayapp.dialogs;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.cryptoWithdraw.CryptoWithdraw;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;

public class WithdrawCustomDialog extends DialogFragment implements View.OnClickListener
{

    public WithdrawCustomDialog()
    {}
    private static final String TAG = WithdrawCustomDialog.class.getName();
    String walletID, currencyCode, paymentID, cryptoComment, countryCodeAndAddress, withdrawAmount, recipientName;
    ConstraintLayout cryptoWithdrawConstraint, fiatWithdrawConstraint, withdrawConstraint;
    Button cryptoWithdrawButton, fiatWithdrawButton;
    TextInputLayout cryptoWithdrawPaymentID;
    TextInputEditText cryptoWithdrawPaymentIDEditText, cryptoWithdrawAddressEditText, cryptoWithdrawCommentEditText, cryptoWithdrawAmountEditText,
    fiatWithdrawRecipientNameEditText, fiatWithdrawAccountNumberEditText, fiatWithdrawAmountEditText;
    RecyclerView mFragmentWalletRecyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = getActivity().getLayoutInflater().inflate(R.layout.withdraw_dialog, container);
        withdrawConstraint = v.findViewById(R.id.withdraw_constraint);
        fiatWithdrawConstraint = v.findViewById(R.id.fiat_withdraw_constraint);
        fiatWithdrawButton = v.findViewById(R.id.fiat_withdraw_button);
        fiatWithdrawAccountNumberEditText = v.findViewById(R.id.fiat_account_number_edit_text);
        fiatWithdrawAmountEditText = v.findViewById(R.id.fiat_amount_edit_text);
        fiatWithdrawRecipientNameEditText = v.findViewById(R.id.fiat_recipient_name_edit_text);
        fiatWithdrawButton.setOnClickListener(this);

        cryptoWithdrawConstraint = v.findViewById(R.id.crypto_withdraw_constraint);
        cryptoWithdrawPaymentID = v.findViewById(R.id.crypto_withdraw_paymentID_text_input);
        cryptoWithdrawButton = v.findViewById(R.id.crypto_withdraw_button);
        cryptoWithdrawAmountEditText = v.findViewById(R.id.crypto_withdraw_amount_edit_text);
        cryptoWithdrawCommentEditText = v.findViewById(R.id.crypto_withdraw_comment_edit_text);
        cryptoWithdrawPaymentIDEditText = v.findViewById(R.id.crypto_withdraw_paymentID_edit_text);
        cryptoWithdrawAddressEditText = v.findViewById(R.id.crypto_withdraw_address_edit_text);
        cryptoWithdrawButton.setOnClickListener(this);

        cryptoWithdrawAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(cryptoWithdrawAmountEditText.getText().toString()))
                {
                    withdrawAmount = cryptoWithdrawAmountEditText.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cryptoWithdrawAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(cryptoWithdrawAddressEditText.getText().toString()))
                {
                    countryCodeAndAddress = cryptoWithdrawAddressEditText.getText().toString();
                }
                else  if (TextUtils.isEmpty(cryptoWithdrawAddressEditText.getText().toString()))
                {
                    countryCodeAndAddress = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cryptoWithdrawPaymentIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(cryptoWithdrawPaymentIDEditText.getText().toString()))
                {
                    paymentID = cryptoWithdrawPaymentIDEditText.getText().toString();
                }
                else if (TextUtils.isEmpty(cryptoWithdrawPaymentIDEditText.getText().toString()))
                {
                    paymentID = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cryptoWithdrawCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(cryptoWithdrawCommentEditText.getText().toString()))
                {
                    cryptoComment = cryptoWithdrawCommentEditText.getText().toString();
                }
                else if (TextUtils.isEmpty(cryptoWithdrawCommentEditText.getText().toString()))
                {
                    cryptoComment = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (!currencyCode.matches("PLN|USD|EUR"))
        {
            cryptoWithdrawConstraint.setVisibility(View.VISIBLE);
            if (currencyCode.matches("XMR|XRP|"))
            {
                cryptoWithdrawPaymentID.setVisibility(View.VISIBLE);
            }
        }
        else if (currencyCode.matches("PLN|USD|EUR"))
        {
            fiatWithdrawConstraint.setVisibility(View.VISIBLE);
        }

        return v;
    }

    public void getWithdrawData(String walletID, String currencyCode, RecyclerView mFragmentWalletRecyclerView)
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
            case R.id.crypto_withdraw_button:
                if (cryptoWithdrawPaymentID.getVisibility() == View.VISIBLE)
                {
                    if (countryCodeAndAddress != null && paymentID != null)
                    {
                        cryptoWithdraw(publicKey, privateKey, walletID, "{\"address\":\"" + countryCodeAndAddress + "?dt=" + paymentID +"\",\"amount\":" + withdrawAmount + ",\"comment\":\"" + cryptoComment + "\"}");
                    }
                }
                else if (cryptoWithdrawPaymentID.getVisibility() == View.GONE)
                {
                    if (countryCodeAndAddress != null)
                    {
                        cryptoWithdraw(publicKey, privateKey, walletID, "{\"address\":\"" + countryCodeAndAddress +"\",\"amount\":" + withdrawAmount + ",\"comment\":\"" + cryptoComment + "\"}");
                    }
                }
                break;
            case R.id.fiat_withdraw_button:

                break;
        }
    }

    @SuppressLint("CheckResult")
    private void cryptoWithdraw(String publicKey, String privateKey, String walletID, String requestData)
    {
        String apiHash = null;
        String uid = null;
        JsonObject jsonObjectBodyParameter = new JsonObject();
        try {
            apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), requestData);
            jsonObjectBodyParameter = (new JsonParser()).parse(requestData).getAsJsonObject();
            uid = createTransactionID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BitBayInterface3 cryptoWithdrawService = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
        io.reactivex.Observable <CryptoWithdraw>  cryptoWithdrawObservable = cryptoWithdrawService.withdrawCryptoApi3(walletID, publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid, jsonObjectBodyParameter);

        cryptoWithdrawObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cryptoWithdrawO -> {

                    if (cryptoWithdrawO.getStatus().equals("Ok"))
                    {
                        Snackbar withdrawErrorSnackbar = Snackbar.make(withdrawConstraint, cryptoWithdrawO.getStatus(), Snackbar.LENGTH_LONG);
                        withdrawErrorSnackbar.show();

                    }
                    else if (cryptoWithdrawO.getStatus().equals("Fail"))
                    {
                        Snackbar withdrawErrorSnackbar = Snackbar.make(withdrawConstraint, cryptoWithdrawO.getErrors()[0], Snackbar.LENGTH_LONG);
                        withdrawErrorSnackbar.show();
                    }
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
