package com.godlife.churchapp.godlifeassembly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.models.MembersModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BirthMonth extends AppCompatActivity {
    private String month, tag;
    private RecyclerView birthdays_rv;

    private FirebaseFirestore membersDB;
    private Context context;
    private ListenerRegistration firestoreListener;
    private List<MembersModel> eventsList;
    private FirebaseFirestoreSettings settings;
    private FirestoreRecyclerAdapter adapter;
    private int num;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_month);



        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                month = extras.getString("month");
                tag = extras.getString("tag");
                getSupportActionBar().setTitle(tag);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        birthdays_rv = findViewById(R.id.birth_recycler);

        membersDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        membersDB.setFirestoreSettings(settings);


        mLayoutManager = new LinearLayoutManager(BirthMonth.this);
        birthdays_rv.setLayoutManager(mLayoutManager);
        birthdays_rv.setItemAnimator(new DefaultItemAnimator());

        loadBirths();

        firestoreListener = membersDB.collection("Members")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("BirthMonth", "Listen failed!", e);
                            return;
                        }

                        eventsList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            MembersModel note = doc.toObject(MembersModel.class);
                            note.setId(doc.getId());
                            eventsList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        birthdays_rv.setAdapter(adapter);
                    }
                });


    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public TextView person_name, birthday, birth_call, birth_text,birth_chat,birth_like;
        public ImageView birth_image;
        public CardView birth_view;


        public MembersViewHolder(View view) {
            super(view);

            person_name= view.findViewById(R.id.birth_person_name);
            birthday = view.findViewById(R.id.birth_person_date);
            birth_call= view.findViewById(R.id.birth_person_phone);
            birth_text= view.findViewById(R.id.birth_person_text);
            birth_image= view.findViewById(R.id.birth_person_image);
            //birth_chat =view.findViewById(R.id.birth_person_chat);
            birth_like = view.findViewById(R.id.birth_person_like);
            birth_view = view.findViewById(R.id.birth_parent);
        }


        private MembersViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(MembersViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }

    }

    private void loadBirths() {

        Query query = membersDB.collection("Members").orderBy("dd", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<MembersModel> response = new FirestoreRecyclerOptions.Builder<MembersModel>()
                .setQuery(query, MembersModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MembersModel, MembersViewHolder>(response) {
            @Override
            protected void onBindViewHolder(final MembersViewHolder holder, final int position, MembersModel model) {
                try {
                    final MembersModel note = eventsList.get(position);

                    if (note.getMM().matches(month)){
                        holder.person_name.setText(note.getFirstName()+" "+note.getSurname());
                        holder.birth_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String number = note.getPhone();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));

                            }
                        });

                        num = note.getLikes();
                        holder.birth_like.setText(""+num);

                        holder.birth_like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                num = num+1;

                                membersDB.collection("Members")
                                        .document(note.getId())
                                        .update("likes",num);
                            }
                        });

                        holder.birth_call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String phone = note.getPhone();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                startActivity(intent);
                            }
                        });
                        holder.birthday.setText(note.getDD()+" / "+note.getMM());



                        Glide.with(BirthMonth.this).load(note.getPhoto()).into(holder.birth_image);


                        holder.birth_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                            }
                        });
                    }
                    else {
                        holder.birth_view.setVisibility(View.GONE);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public MembersViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.birthdays_layout, parent, false);


                return new MembersViewHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        birthdays_rv.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
