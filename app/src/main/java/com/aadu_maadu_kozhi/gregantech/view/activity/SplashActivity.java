package com.aadu_maadu_kozhi.gregantech.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.aadu_maadu_kozhi.gregantech.R;
import com.aadu_maadu_kozhi.gregantech.util.CommonUtils;
import com.aadu_maadu_kozhi.gregantech.util.SharedPref;



public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      //  CommonUtils.getInstance().printHashKey(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateNextScreen();

            }
        }, 0);
    }

    private void navigateNextScreen() {
        if (SharedPref.getInstance().getUserId(this) != null) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, LogInActivity.class));
        }
        finish();
    }

}
