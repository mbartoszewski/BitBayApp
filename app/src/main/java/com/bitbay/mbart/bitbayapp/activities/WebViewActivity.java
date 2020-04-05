package com.bitbay.mbart.bitbayapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bitbay.mbart.bitbayapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    AdView adView;
    InterstitialAd interstitialAd;
    Intent intent;
    Random rand;
    int r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.fragment_rss_feed_web_view);
        adView = findViewById(R.id.ad_view);
        MobileAds.initialize(this, getResources().getString(R.string.interstitialAds));
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitialAds));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                finish();
            }
        });
        intent = getIntent();

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        rand = new Random();
        r = rand.nextInt((4-1)+1) +1;
        webView.loadUrl(intent.getStringExtra("url"));
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed()
    {
        if (webView.canGoBack())
        {
         webView.goBack();
        }
        else if (interstitialAd.isLoaded() && (r == 2 || r == 4))
        {
            interstitialAd.show();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
