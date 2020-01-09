package com.godlife.churchapp.godlifeassembly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.models.LoveModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FullLove extends AppCompatActivity {
    private String position;
    private TextView tv_title, tv_author, tv_body,tv_date;
    private FirebaseFirestore loveDB;
    private FirebaseFirestoreSettings settings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_love);

        tv_title = findViewById(R.id.full_love_title);
        tv_author = findViewById(R.id.full_love_author);
        tv_body = findViewById(R.id.full_love_body);
        tv_date = findViewById(R.id.full_love_date);

        loveDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        loveDB.setFirestoreSettings(settings);

        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                position = extras.getString("position");
                if (position != null) {

                    loveDB.collection("Love")
                            .document(position)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()){
                                    LoveModel model = document.toObject(LoveModel.class);

                                    tv_title.setText(model.getTitle());
                                    tv_author.setText(model.getAuthor());
                                    tv_body.setText(model.getContent());
                                    tv_date.setText(model.getLoveDate());
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
