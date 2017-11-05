package com.xizuth.ohmlawcalcu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private final String web = "http://www.alejandro-leyva.com/";
    private final String fb = "https://www.facebook.com/leyva.consult/";
    private final String tw = "https://twitter.com/jalm_x";
    private final String gh = "https://github.com/jalmx89";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        loadListenerViews();
    }

    private void loadListenerViews() {
        findViewById(R.id.icon_fb).setOnClickListener(this);
        findViewById(R.id.icon_tw).setOnClickListener(this);
        findViewById(R.id.icon_github).setOnClickListener(this);
        findViewById(R.id.name).setOnClickListener(this);
        findViewById(R.id.logo).setOnClickListener(this);
    }

    private void openWeb(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.icon_fb:
                openWeb(fb);
                break;
            case R.id.icon_tw:
                openWeb(tw);
                break;
            case R.id.icon_github:
                openWeb(gh);
                break;
            case R.id.name:
            case R.id.logo:
                openWeb(web);
                break;
        }
    }

}
