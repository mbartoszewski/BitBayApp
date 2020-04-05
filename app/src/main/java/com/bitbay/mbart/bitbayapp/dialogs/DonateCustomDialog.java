package com.bitbay.mbart.bitbayapp.dialogs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitbay.mbart.bitbayapp.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DonateCustomDialog extends DialogFragment implements View.OnClickListener
{
    public DonateCustomDialog()
    {
    }

    ImageButton payPalMe, copyBTCAddress, copyETHAddress, copyLSKAddress;
    TextView btcAddress, ethAddress, lskAddress;
    ConstraintLayout mFragmentDonateConstraintLayout;
    ClipboardManager clipboardManager;
    ClipData clip;
    Snackbar copiedSnackbar;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = getActivity().getLayoutInflater().inflate(R.layout.donate_card, container);
        payPalMe = v.findViewById(R.id.paypal_donate_button);
        copyBTCAddress = v.findViewById(R.id.btc_address_copy_button);
        copyETHAddress = v.findViewById(R.id.eth_address_copy_button);
        copyLSKAddress = v.findViewById(R.id.lsk_address_copy_button);
        btcAddress = v.findViewById(R.id.btc_address);
        ethAddress = v.findViewById(R.id.eth_address);
        lskAddress = v.findViewById(R.id.lsk_address);
        mFragmentDonateConstraintLayout = v.findViewById(R.id.donate_constraint);

        clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);

        payPalMe.setOnClickListener(this);
        copyBTCAddress.setOnClickListener(this);
        copyETHAddress.setOnClickListener(this);
        copyLSKAddress.setOnClickListener(this);
        return v;
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.paypal_donate_button:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://paypal.me/bartoszewskim"));
                startActivity(browserIntent);
                break;
            case R.id.btc_address_copy_button:
                clip = ClipData.newPlainText("BTC address",btcAddress.getText());
                clipboardManager.setPrimaryClip(clip);
                copiedSnackbar = Snackbar.make(mFragmentDonateConstraintLayout, getActivity().getResources().getIdentifier("copy_donate_addres", "string", getActivity().getPackageName()), Snackbar.LENGTH_SHORT);
                copiedSnackbar.show();
                break;
            case R.id.eth_address_copy_button:
                clip = ClipData.newPlainText("ETH address",ethAddress.getText());
                clipboardManager.setPrimaryClip(clip);
                copiedSnackbar = Snackbar.make(mFragmentDonateConstraintLayout, getActivity().getResources().getIdentifier("copy_donate_addres", "string", getActivity().getPackageName()), Snackbar.LENGTH_SHORT);
                copiedSnackbar.show();
                break;
            case R.id.lsk_address_copy_button:
                clip = ClipData.newPlainText("LSK address",lskAddress.getText());
                clipboardManager.setPrimaryClip(clip);
                copiedSnackbar = Snackbar.make(mFragmentDonateConstraintLayout, getActivity().getResources().getIdentifier("copy_donate_addres", "string", getActivity().getPackageName()), Snackbar.LENGTH_SHORT);
                copiedSnackbar.show();
                break;
        }
    }
}
