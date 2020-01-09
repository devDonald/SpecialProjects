package com.godlife.churchapp.godlifeassembly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.models.EventsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FullMeeting extends AppCompatActivity {
    private String position;
    private ImageView picture;
    private TextView tv_title, tv_details;
    private FirebaseFirestore eventsDB;
    private FirebaseFirestoreSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_meeting);


        tv_title = findViewById(R.id.full_meet_title);
        tv_details = findViewById(R.id.full_meeting);
        picture = findViewById(R.id.meeting_image);

        eventsDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        eventsDB.setFirestoreSettings(settings);

        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                position = extras.getString("position");
                if (position != null) {

                    eventsDB.collection("Meetings")
                            .document(position)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()){
                                    EventsModel model = document.toObject(EventsModel.class);

                                    tv_title.setText(model.getTitle());
                                    tv_details.setText(model.getDetails());

                                    if (model.getImage()!=null){
                                        Glide.with(getApplicationContext())
                                                .load(model.getImage())
                                                .into(picture);
                                    }

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
