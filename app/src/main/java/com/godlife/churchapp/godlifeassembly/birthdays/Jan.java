package com.godlife.churchapp.godlifeassembly.birthdays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.AboutUs;
import com.godlife.churchapp.godlifeassembly.activities.BirthMonth;
import com.godlife.churchapp.godlifeassembly.activities.FullLyrics;
import com.godlife.churchapp.godlifeassembly.fragments.ChurchSongs;
import com.godlife.churchapp.godlifeassembly.models.LyricsModel;
import com.godlife.churchapp.godlifeassembly.models.MembersModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Jan extends AppCompatActivity {
    private RecyclerView allMembers_RV;
    private DatabaseReference membersDb;
    private Context context;
    private String month,tag;
    private int num;
    private FirebaseRecyclerAdapter<MembersModel, MembersViewHolder> firebaseRecyclerAdapter;
    private Dialog popDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jan);

        popDialog = new Dialog(Jan.this);

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


        allMembers_RV= findViewById(R.id.births_recycler);

        membersDb= FirebaseDatabase.getInstance().getReference().child("Members").child(month);
        //allmembers_RV.setHasFixedSize(true);
        allMembers_RV.setLayoutManager(new LinearLayoutManager(context));
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


    public void showBirthdays(){

        Query firebaseSearchQuery = membersDb.orderByChild("dd");

        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<MembersModel, MembersViewHolder>(
                MembersModel.class,
                R.layout.birthdays_layout,
                MembersViewHolder.class,
                firebaseSearchQuery
        ) {

            @Override
            protected void populateViewHolder(final MembersViewHolder viewHolder, final MembersModel model, final int position) {
                try {
                    viewHolder.person_name.setText(model.getSurname()+" "+model.getFirstName());

                    viewHolder.birth_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String phone = model.getPhone();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                            startActivity(intent);
                        }
                    });

                    viewHolder.birth_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String number = model.getPhone();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));

                        }
                    });

                    viewHolder.birthday.setText(model.getDD()+" / "+model.getMM());



                    Glide.with(Jan.this).load(model.getPhoto()).into(viewHolder.birth_image);


                    viewHolder.birth_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showPopup(R.layout.display_picture,model.getPhoto());

                        }
                    });

                    num = model.getLikes();
                    viewHolder.birth_like.setText(""+num);

                    viewHolder.birth_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            membersDb.child(firebaseRecyclerAdapter.getRef(position).getKey())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            MembersModel newPost = dataSnapshot.getValue(MembersModel.class);
                                            int likes = newPost.getLikes();
                                            likes = likes +1;

                                            Map<String, Object> updates = new HashMap<String,Object>();
                                            updates.put("likes", likes);

                                            membersDb.child(firebaseRecyclerAdapter.getRef(position).getKey()).updateChildren(updates);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MembersViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                return viewHolder;
            }
        };
        allMembers_RV.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        showBirthdays();
    }

    public void showPopup(int view, String img) {
        TextView txtclose;
        ImageView display;
        popDialog.setContentView(view);
        txtclose =popDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        display = popDialog.findViewById(R.id.birth_display_pics);
        Glide.with(Jan.this).load(img).into(display);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDialog.dismiss();
            }
        });
        popDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popDialog.show();
    }
}
