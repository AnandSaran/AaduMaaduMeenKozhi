package com.aadu_maadu_kozhi.gregantech.common;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatActivity;

import com.aadu_maadu_kozhi.gregantech.R;
import com.google.android.gms.ads.MobileAds;



public class BaseProject extends Application implements  DialogInterface.OnDismissListener {

    private static BaseProject mAppController;
    private AppCompatActivity activity;

    public static BaseProject getInstance() {
        return mAppController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
        mAppController = this;



        //setupAdvertisement();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public String getStringFromRes(int resId) throws Exception {
        return getString(resId);
    }




    @Override
    public void onDismiss(DialogInterface dialog) {
        if (activity != null) {
            activity.onBackPressed();
        }
    }
}
