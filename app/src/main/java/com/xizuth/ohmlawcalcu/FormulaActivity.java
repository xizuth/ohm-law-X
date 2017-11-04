package com.xizuth.ohmlawcalcu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xizuth.ohmlawcalcu.admob.AdMob;
import com.xizuth.ohmlawcalcu.admob.Key;

public class FormulaActivity extends AppCompatActivity {

    private AdMob adMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);
        adMob = new AdMob(this, Key.FORMULA);
    }
    @Override
    protected void onPause() {
        adMob.pauseAdMob();
        super.onPause();
    }

    @Override
    protected void onResume() {
        adMob.resumeAdMob();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        adMob.destroyAdMob();
        super.onDestroy();
    }
}
