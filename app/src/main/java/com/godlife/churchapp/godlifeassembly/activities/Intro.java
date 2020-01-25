package com.godlife.churchapp.godlifeassembly.activities;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;
import com.godlife.churchapp.godlifeassembly.R;
import com.valdesekamdem.library.mdtoast.MDToast;

public class Intro extends AppIntro2 {

    private String prevStarted = "prevStarted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();


        } else {
            Intent mainIntent = new Intent(Intro.this,MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        }
        getSupportActionBar().hide();

        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
        addSlide(SampleSlide.newInstance(R.layout.intro5));


        showStatusBar(true);

        setDepthAnimation();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        loadMainActivity();
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
        MDToast.makeText(getApplicationContext(), getString(R.string.skip), MDToast.LENGTH_SHORT,
                MDToast.TYPE_INFO).show();
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}
