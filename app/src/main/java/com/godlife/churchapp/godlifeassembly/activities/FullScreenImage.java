package com.godlife.churchapp.godlifeassembly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.godlife.churchapp.godlifeassembly.R;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);


        getSupportActionBar().hide();


        ImageView fullScreenImageView = findViewById(R.id.fullScreenImageView);
        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            String imageUri = getIntent().getExtras().get("image").toString();
            if(imageUri != null) {
                Glide.with(this)
                        .load(imageUri)
                        .into(fullScreenImageView);
            }
        }
    }
}
