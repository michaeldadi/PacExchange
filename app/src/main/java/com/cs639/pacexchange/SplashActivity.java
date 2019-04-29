package com.cs639.pacexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);
                    //After 5 seconds redirect to intent
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    //Remove activity
                    finish();
                } catch (Exception e) {
                    Log.d("SplashActivity: ", "An error occurred: " + e);
                }
            }
        };
        // start thread
        background.start();
    }
}