package com.godlife.churchapp.godlifeassembly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.godlife.churchapp.godlifeassembly.R;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH= 1500;

    private int firstLaunch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        getSupportActionBar().hide();

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity

            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //SharedPreferences pref = getPreferences()

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRun", false).apply();

                    Intent mainIntent = new Intent(SplashScreen.this,Intro.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}
