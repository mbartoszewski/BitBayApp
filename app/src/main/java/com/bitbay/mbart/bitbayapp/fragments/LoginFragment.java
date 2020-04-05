package com.bitbay.mbart.bitbayapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.bitbay.mbart.bitbayapp.activities.MainActivity;
import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.activities.ScannedBarcodeActivity;
import com.bitbay.mbart.bitbayapp.interfaces.BitBayInterface3;
import com.bitbay.mbart.bitbayapp.models.wallet.WalletLists;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.createTransactionID;
import static com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment.encode;

public class LoginFragment extends Fragment implements View.OnClickListener
{

    CheckBox checkBoxRememberMe;
    TextInputEditText editTextPublicKey, editTextPrivateKey;
    static final int PICK_KEY_REQUEST = 1;  // The request code
    public static String publicKey, privateKey;
    ConstraintLayout mLoginConstraintLayout;
    public static boolean isConnected = false;
    private static final String TAG = LoginFragment.class.getName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        editTextPublicKey = rootView.findViewById(R.id.EditTextPublicKey);
        editTextPrivateKey = rootView.findViewById(R.id.EditTextPrivateKey);
        ImageButton barCodeScanner = rootView.findViewById(R.id.qr_code_imagebutton);
        Button btnLogin = rootView.findViewById(R.id.login_button);
        mLoginConstraintLayout = rootView.findViewById(R.id.login_constraint);
        checkBoxRememberMe = rootView.findViewById(R.id.remember_me_checkbox);

        barCodeScanner.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        editTextPublicKey.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(editTextPublicKey.getText().toString())) {
                    publicKey = editTextPublicKey.getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPrivateKey.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(editTextPrivateKey.getText().toString())) {
                    privateKey = editTextPrivateKey.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.qr_code_imagebutton:
                startActivityForResult(new Intent(getActivity(), ScannedBarcodeActivity.class), PICK_KEY_REQUEST);
                break;
            case R.id.login_button:
                checkLogin(publicKey, privateKey);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request we're responding to
        if (requestCode == PICK_KEY_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                publicKey = data.getStringExtra("publicKey");
                privateKey = data.getStringExtra("privateKey");

                editTextPublicKey.setText(publicKey);
                editTextPrivateKey.setText(privateKey);
                // Do something with the contact here (bigger example below)
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @SuppressLint("CheckResult")
    private void checkLogin(String publicKey, String privateKey)
    {
        if (publicKey != null && privateKey != null) {
            try {
                String apiHash = null;
                String uid = null;
                apiHash = encode(privateKey, publicKey, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), "");
                uid = createTransactionID();

                BitBayInterface3 checkLoginService = BitBayInterface3.retrofitAPI3.create(BitBayInterface3.class);
                Observable<WalletLists> checkLoginObservable = checkLoginService.balanceListsApi3(publicKey, apiHash, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()), uid);
                checkLoginObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(checkLogin -> {

                            if (checkLogin.getStatus().equals("Ok"))
                            {
                                isConnected = true;
                                hideSoftKeyboard(getActivity());
                                if (checkBoxRememberMe.isChecked()) {
                                    savePreferences(publicKey, privateKey);
                                }
                                if (getFragmentManager().getBackStackEntryCount() > 0)
                                {
                                    getFragmentManager().popBackStack(null,
                                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }
                            }
                            else if (checkLogin.getStatus().equals("Fail"))
                            {
                                Activity activity = getActivity();
                                if (activity != null )
                                {
                                    Snackbar emptyKeyError = Snackbar.make(mLoginConstraintLayout, checkLogin.getErrors()[0], Snackbar.LENGTH_SHORT);
                                    emptyKeyError.show();
                                }
                            }
                        }, e ->
                        {
                            Log.e(TAG, "ORDERBOOK SERVICE " + e);

                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Activity activity = getActivity();
            if (activity != null )
            {
            Snackbar emptyKeyError = Snackbar.make(mLoginConstraintLayout, getResources().getString(R.string.emptyKeyError), Snackbar.LENGTH_SHORT);
            emptyKeyError.show();
            }
        }
    }

    public void savePreferences(String k, String pk)
    {
        new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor preferencesEditor = pref.edit();
                preferencesEditor.putString("k", k);
                preferencesEditor.putString("pk", pk);
                preferencesEditor.apply();
                Log.e(TAG, " NEW RUNNABLE " + " savePreference");
            }
        }.run();

    }

    @Override
    public void onResume()
    {
        Log.e("SELECTED", "onResume of CurrencyFragment");
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(getContext().getString(R.string.menu_login));
    }
}
