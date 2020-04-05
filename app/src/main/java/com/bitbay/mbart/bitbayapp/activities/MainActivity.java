package com.bitbay.mbart.bitbayapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bitbay.mbart.bitbayapp.dialogs.DepositCustomDialog;
import com.bitbay.mbart.bitbayapp.R;
import com.bitbay.mbart.bitbayapp.dialogs.RssFeedSourceCustomDialog;
import com.bitbay.mbart.bitbayapp.dialogs.WithdrawCustomDialog;
import com.bitbay.mbart.bitbayapp.fragments.CalculatorFragment;
import com.bitbay.mbart.bitbayapp.fragments.DetailCardFragment;
import com.bitbay.mbart.bitbayapp.fragments.ExchangeFragment;
import com.bitbay.mbart.bitbayapp.fragments.LoginFragment;
import com.bitbay.mbart.bitbayapp.fragments.MyOffersFragment;
import com.bitbay.mbart.bitbayapp.fragments.RssFeedFragment;
import com.bitbay.mbart.bitbayapp.fragments.TransactionHistoryFragment;
import com.bitbay.mbart.bitbayapp.fragments.WalletFragment;
import com.bitbay.mbart.bitbayapp.fragments.YouTubeChannelFragment;
import com.bitbay.mbart.bitbayapp.models.RssSource;
import com.bitbay.mbart.bitbayapp.helpers.GdprHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.isConnected;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.privateKey;
import static com.bitbay.mbart.bitbayapp.fragments.LoginFragment.publicKey;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements RssFeedFragment.sendDataToRssDialog, ExchangeFragment.SendDetailCardViewData, WalletFragment.sendDataToDepositDialog, NavigationView.OnNavigationItemSelectedListener {

    DetailCardFragment detailCardFragment;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    Fragment calculatorFragment, myOfferFragment, walletFragment, loginFragment, exchangeFragment, newsFragment, transactionHistoryFragment, youTubeChannelFragment;
    DrawerLayout navDrawerLayout;
    ActionBarDrawerToggle navDrawerToggle;
    NavigationView navigationView;
    boolean isNightModeEneabled;
    MenuItem navMenuOffers, navMenuTransactionHistory, navMenuWallet, navMenuLogin, navMenuLogout;
    Switch nightModeSwitch;
    public static DecimalFormat df2;
    public static DecimalFormat df8;
    GdprHelper gdprHelper;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        nightModeSwitch = navigationView.getMenu().findItem(R.id.nav_menu_night_mode).getActionView().findViewById(R.id.night_mode_switch);
        calculatorFragment = new CalculatorFragment();
        newsFragment = new RssFeedFragment();
        loginFragment = new LoginFragment();
        exchangeFragment = new ExchangeFragment();
        myOfferFragment = new MyOffersFragment();
        walletFragment = new WalletFragment();
        transactionHistoryFragment = new TransactionHistoryFragment();
        youTubeChannelFragment = new YouTubeChannelFragment();
        //getPreferences();
        new getSharedPreferences().execute();

        AdView adView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        transaction = getSupportFragmentManager().beginTransaction();
        fragmentManager= getSupportFragmentManager();
        transaction.replace(R.id.fragment_container, exchangeFragment, "ExchangeFragment").commit();

        navDrawerToggle = new ActionBarDrawerToggle(this, navDrawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_closed)
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset > 0.33)
                {
                    showHideMenuItems(isConnected, navigationView.getMenu());
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        if (isNightModeEneabled)
        {
            nightModeSwitch.setChecked(true);
        }
        else if (!isNightModeEneabled)
        {
            nightModeSwitch.setChecked(false);
        }
        navDrawerLayout.addDrawerListener(navDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                savePreferences(isChecked);
                new Runnable() {
                    @Override
                    public void run()
                    {

                        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                        {
                            getSupportFragmentManager().popBackStack(null,
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        if (isChecked && AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
                        {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme
                            MainActivity.this.recreate();
                        }
                        else if (!isChecked && AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                        {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //For night mode theme
                            MainActivity.this.recreate();
                        }
                    }
                }.run();
                navDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        gdprHelper = new GdprHelper(MainActivity.this);
        gdprHelper.initialise();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        df2 = new DecimalFormat("#.##", symbols);
        df2.setRoundingMode(RoundingMode.HALF_DOWN);
        df8 = new DecimalFormat("#.########", symbols);
        df8.setRoundingMode(RoundingMode.HALF_DOWN);
    }

    public void showHideMenuItems(boolean isConnected, Menu menu)
    {

        navMenuLogin = menu.findItem(R.id.nav_menu_login);
        navMenuLogout = menu.findItem(R.id.nav_menu_logout);
        navMenuWallet = menu.findItem(R.id.nav_menu_wallet);
        navMenuOffers = menu.findItem(R.id.nav_menu_offers);
        navMenuTransactionHistory = menu.findItem(R.id.nav_menu_transaction_history);

        if (isConnected)
        {
            navMenuLogin.setVisible(false);
            navMenuLogout.setVisible(true);
            navMenuOffers.setVisible(true);
            navMenuWallet.setVisible(true);
            navMenuTransactionHistory.setVisible(true);
        }
        else if (!isConnected)
        {
            navMenuLogin.setVisible(true);
            navMenuLogout.setVisible(false);
            navMenuOffers.setVisible(false);
            navMenuWallet.setVisible(false);
            navMenuTransactionHistory.setVisible(false);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (navDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            navDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void sendData(String fiatCurrency, String cryptoCurrency, String lastRate)
    {
        //DetailCardFragment detailCardViewTag = (DetailCardFragment) getSupportFragmentManager().findFragmentByTag(tag);

        if (detailCardFragment != null)
        {
            detailCardFragment.displayReceivedData(fiatCurrency, cryptoCurrency, lastRate);

            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailCardFragment, "detailCardViewFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else
        {
            detailCardFragment = new DetailCardFragment();
            detailCardFragment.displayReceivedData(fiatCurrency, cryptoCurrency, lastRate);

            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailCardFragment, "detailCardViewFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void sendDepositData(String walletID, String currencyCode, RecyclerView mFragmentWalletRecyclerView)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("depositDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DepositCustomDialog dialogFragment = new DepositCustomDialog();
        dialogFragment.getDepositData(walletID, currencyCode, mFragmentWalletRecyclerView);
        dialogFragment.show(ft, "depositDialog");
    }

    @Override
    public void sendWithdrawData(String walletID, String currencyCode, RecyclerView mFragmentWalletRecyclerView)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("withdrawDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WithdrawCustomDialog dialogFragment = new WithdrawCustomDialog();
        dialogFragment.getWithdrawData(walletID, currencyCode, mFragmentWalletRecyclerView);
        dialogFragment.show(ft, "withdrawDialog");
    }

    @Override
    public void sendRssSourceData(List<RssSource> rssSourceList)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("rssSourceDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        RssFeedSourceCustomDialog dialogFragment = new RssFeedSourceCustomDialog();
        dialogFragment.getRssSourceData(rssSourceList);
        dialogFragment.show(ft, "rssSourceDialog");
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (navDrawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    navDrawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (!navDrawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    navDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_menu_exchange:

                if (!exchangeFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, exchangeFragment, "ExchangeFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;

            case R.id.nav_menu_offers:
                if (!myOfferFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, myOfferFragment, "MyOffersFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;

            case R.id.nav_menu_wallet:
                if (!walletFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, walletFragment, "WalletFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;

            case R.id.nav_menu_transaction_history:
                if (!transactionHistoryFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, transactionHistoryFragment, "TransactionHistoryFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.nav_menu_login:
                if (!loginFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, loginFragment, "LoginFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;

            case R.id.nav_menu_logout:
                if (!exchangeFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, exchangeFragment, "ExchangeFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                deletePreferences();
                break;

            case R.id.nav_menu_calculator:
                if (!calculatorFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, calculatorFragment, "CalculatorFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.nav_menu_rss_feed:
                if (!newsFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newsFragment, "RssFeedFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;

            case R.id.nav_menu_youtube_channel:
                if (!youTubeChannelFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, youTubeChannelFragment, "YouTubeChannelFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;

            case R.id.nav_menu_donate_paypal:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://paypal.me/bartoszewskim"));
                startActivity(browserIntent);
                break;

            case R.id.nav_menu_reset_ad_preferences:
                gdprHelper.resetConsent();
                gdprHelper.initialise();
                break;
        }

        navDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deletePreferences()
    {
        new Runnable()
        {
            @Override
            public void run() {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor preferencesEditor = pref.edit();
                if (pref.contains("k") && pref.contains("pk"))
                {
                    preferencesEditor.remove("k");
                    preferencesEditor.remove("pk");
                    preferencesEditor.apply();
                }

                Log.e(TAG, " NEW RUNNABLE " + " deletePreference");
            }
        }.run();

        isConnected = false;
        publicKey = null;
        privateKey = null;
    }

    public void savePreferences(boolean isNightModeEneabled)
    {
        new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor preferencesEditor = pref.edit();
                preferencesEditor.putBoolean("isNightMode", isNightModeEneabled);
                preferencesEditor.apply();
            }
        }.run();

    }

    public class getSharedPreferences extends AsyncTask <Context, Void, Void>
    {

        @Override
        protected Void doInBackground(Context... contexts)
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            isNightModeEneabled = pref.getBoolean("isNightMode", false);
            if (pref.contains("k") && pref.contains("pk"))
            {
                publicKey = pref.getString("k", null);
                privateKey = pref.getString("pk", null);
                isConnected = true;
            }
            if (isNightModeEneabled)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme
            }
            else if (!isNightModeEneabled)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //For night mode theme
            }

            return null;
        }
    }

    @Override
    public void onPause()
    {
        Log.e("ACTIVITYMAIN", "OnPause of ACTIVITYMAIN");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.e("ACTIVITYMAIN", "onResume of ACTIVITYMAIN");
        super.onResume();
    }
}
