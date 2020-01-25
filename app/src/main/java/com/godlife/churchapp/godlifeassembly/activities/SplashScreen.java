package com.godlife.churchapp.godlifeassembly.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.godlife.churchapp.godlifeassembly.R;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH= 1500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //SharedPreferences pref = getPreferences()

        setContentView(R.layout.activity_splash_screen);


        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this,Intro.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}
