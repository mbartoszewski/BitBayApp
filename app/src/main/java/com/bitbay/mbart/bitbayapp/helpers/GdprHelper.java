package com.bitbay.mbart.bitbayapp.helpers;

import android.content.Context;
import android.widget.Toast;

import com.bitbay.mbart.bitbayapp.BuildConfig;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import java.net.MalformedURLException;
import java.net.URL;

public class GdprHelper {

    private static final String PUBLISHER_ID = "pub-3238620931225368";
    private static final String PRIVACY_URL = "https://1drv.ms/w/s!AuyJJTUjndGQiecPg4mO3CSdCD-z4w";

    private final Context context;

    private ConsentForm consentForm;

    public GdprHelper(Context context) {
        this.context = context;
    }

    // Initialises the consent information and displays consent form if needed
    public void initialise() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        consentInformation.requestConsentInfoUpdate(new String[]{PUBLISHER_ID}, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                if (consentStatus == ConsentStatus.UNKNOWN) {
                    displayConsentForm();
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // Consent form error. Would be nice to have proper error logging. Happens also when user has no internet connection
                if (BuildConfig.BUILD_TYPE.equals("debug")) {
                    Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Resets the consent. User will be again displayed the consent form on next call of initialise method
    public void resetConsent() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        consentInformation.reset();
    }

    private void displayConsentForm() {

        consentForm = new ConsentForm.Builder(context, getPrivacyUrl())
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form has loaded successfully, now show it
                        consentForm.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed. This callback method contains all the data about user's selection, that you can use.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error. Would be nice to have some proper logging
                        if (BuildConfig.BUILD_TYPE.equals("debug")) {
                            Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build();
        consentForm.load();
    }

    private URL getPrivacyUrl() {
        URL privacyUrl = null;
        try {
            privacyUrl = new URL(PRIVACY_URL);
        } catch (MalformedURLException e) {
            // Since this is a constant URL, the exception should never(or always) occur
            e.printStackTrace();
        }
        return privacyUrl;
    }

}
