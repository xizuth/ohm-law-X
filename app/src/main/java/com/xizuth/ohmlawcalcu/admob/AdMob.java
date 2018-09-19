package com.xizuth.ohmlawcalcu.admob;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.xizuth.ohmlawcalcu.R;

/**
 * Created by josef on 11/4/17.
 */

public class AdMob {

    private final static String TAG = AdMob.class.getSimpleName();
    private AdView adView;
    private Activity context;
    private int key;

    public AdMob(Activity context, int key) {
        this.context = context;
        MobileAds.initialize(context, context.getString(R.string.app_id));
        this.key = key;
        activityFocus(key);
    }

    public AdView getAdView() {
        return adView;
    }

    public AdView pauseAdMob() {
        if (adView != null) {
            adView.pause();
        } else {
            adView = new AdMob(context, key).getAdView();
        }
        return adView;
    }

    public AdView destroyAdMob() {
        if (adView != null) {
            adView.destroy();
        }
        return adView;
    }

    public AdView resumeAdMob() {
        if (adView != null) {
            adView.resume();
        } else {
            adView = new AdMob(context, key).getAdView();
        }
        return adView;
    }

    private void activityFocus(final int key) {
        switch (key) {
            case Key.MAIN:
                adView = context.findViewById(R.id.ad_view_main);
                break;
            case Key.FORMULA:
                adView = context.findViewById(R.id.ad_view_formula);
                break;

        }
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}
