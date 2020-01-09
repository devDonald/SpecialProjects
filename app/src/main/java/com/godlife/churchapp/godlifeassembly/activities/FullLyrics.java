package com.godlife.churchapp.godlifeassembly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.models.LyricsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FullLyrics extends AppCompatActivity {
    private TextView tv_title, tv_author, tv_body;
    private String title,body,author,position;
    private DatabaseReference detailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_lyrics);

        tv_title = findViewById(R.id.full_lyrics_title);
        tv_author = findViewById(R.id.full_lyrics_author);
        tv_body = findViewById(R.id.full_lyrics_body);

        detailsRef = FirebaseDatabase.getInstance().getReference().child("Lyrics");


        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            position = extras.getString("position");
            if (position!=null){
                DatabaseReference userRef = detailsRef.child(position);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Log.d("ds",""+ds);

                        LyricsModel lyricsModel = dataSnapshot.getValue(LyricsModel.class);
                        tv_title.setText(lyricsModel.getSongTitle());
                        tv_author.setText(lyricsModel.getWrittenBy());
                        tv_body.setText(lyricsModel.getFullLyrics());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        databaseError.getMessage();
                    }
                };
                userRef.addListenerForSingleValueEvent(valueEventListener);


            }



        }
    }
}
