package com.godlife.churchapp.godlifeassembly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.models.ArticleModel;
import com.godlife.churchapp.godlifeassembly.models.LyricsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FullArticle extends AppCompatActivity {

    private TextView tv_title, tv_author, tv_body,tv_date;
    private String title,body,author,position;
    private DatabaseReference detailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);


        tv_title = findViewById(R.id.full_article_title);
        tv_author = findViewById(R.id.full_article_author);
        tv_body = findViewById(R.id.full_article_body);
        tv_date = findViewById(R.id.full_article_date);


        detailsRef = FirebaseDatabase.getInstance().getReference().child("Articles");


        try {

            Bundle extras = getIntent().getExtras();
            if (extras!=null){
                position = extras.getString("position");
                if (position!=null){
                    DatabaseReference userRef = detailsRef.child(position);

                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Log.d("ds",""+ds);

                            ArticleModel articleModel = dataSnapshot.getValue(ArticleModel.class);
                            tv_title.setText(articleModel.getTitle());
                            tv_author.setText(articleModel.getAuthor());
                            tv_body.setText(articleModel.getContent());
                            tv_date.setText("On "+articleModel.getDate());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            databaseError.getMessage();
                        }
                    };
                    userRef.addListenerForSingleValueEvent(valueEventListener);


                }



            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
