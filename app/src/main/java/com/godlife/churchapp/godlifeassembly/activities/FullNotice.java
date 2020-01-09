package com.godlife.churchapp.godlifeassembly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.models.NoticeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FullNotice extends AppCompatActivity {

    private String position;
    private TextView tv_title, tv_details;
    private FirebaseFirestore noticeDB;
    private FirebaseFirestoreSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_notice);

        tv_title = findViewById(R.id.full_title);
        tv_details = findViewById(R.id.full_notice);

        noticeDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        noticeDB.setFirestoreSettings(settings);

        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                position = extras.getString("position");
                if (position != null) {

                    noticeDB.collection("Notices")
                            .document(position)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()){
                                    NoticeModel model = document.toObject(NoticeModel.class);

                                    tv_title.setText(model.getTitle());
                                    tv_details.setText(model.getDetails());

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
