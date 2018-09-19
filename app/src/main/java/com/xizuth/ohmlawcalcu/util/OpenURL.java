package com.xizuth.ohmlawcalcu.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public final class OpenURL {

    private OpenURL(){

    }

    public static void open(Activity activity, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }
}
