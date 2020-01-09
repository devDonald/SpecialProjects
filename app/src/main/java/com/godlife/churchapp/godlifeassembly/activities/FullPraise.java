package com.godlife.churchapp.godlifeassembly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.fragments.PraiseReport;
import com.godlife.churchapp.godlifeassembly.models.PraiseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;

public class FullPraise extends AppCompatActivity {

    private String position;
    private TextView tv_title, tv_author, tv_body,tv_date;
    private FirebaseFirestore praiseDB;
    private FirebaseFirestoreSettings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_praise);


        tv_title = findViewById(R.id.full_praise_title);
        tv_author = findViewById(R.id.full_praise_author);
        tv_body = findViewById(R.id.full_praise_body);
        tv_date = findViewById(R.id.full_praise_date);

        praiseDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        praiseDB.setFirestoreSettings(settings);

        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                position = extras.getString("position");
                if (position != null) {

                    praiseDB.collection("Praise")
                            .document(position)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()){
                                    PraiseModel model = document.toObject(PraiseModel.class);

                                    tv_title.setText(model.getTitle());
                                    tv_author.setText(model.getAuthor());
                                    tv_body.setText(model.getContent());
                                    tv_date.setText(model.getPraiseDate());
                                }
                            }
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
