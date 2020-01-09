package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.FullNotice;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.NoticeModel;
import com.godlife.churchapp.godlifeassembly.models.PraiseModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notices extends Fragment {
    private View notice_view;
    private RecyclerView allNotices;
    private static final String TAG = "Notices";

    private FirestoreRecyclerAdapter adapter;

    private FirebaseFirestore noticeDB;
    private ListenerRegistration firestoreListener;
    private List<NoticeModel> noticeList;
    private FirebaseFirestoreSettings settings;


    public Notices() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        notice_view = inflater.inflate(R.layout.fragment_notices, container, false);

        allNotices = notice_view.findViewById(R.id.notices_recycler);

        noticeDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        noticeDB.setFirestoreSettings(settings);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        allNotices.setLayoutManager(mLayoutManager);
        allNotices.setItemAnimator(new DefaultItemAnimator());

        loadNotices();

        firestoreListener = noticeDB.collection("Notices")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                         noticeList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            NoticeModel note = doc.toObject(NoticeModel.class);
                            note.setId(doc.getId());
                            noticeList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        allNotices.setAdapter(adapter);
                    }
                });

        return notice_view;
    }


    private void loadNotices() {

        Query query = noticeDB.collection("Notices").orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<NoticeModel> response = new FirestoreRecyclerOptions.Builder<NoticeModel>()
                .setQuery(query, NoticeModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<NoticeModel, NoticesViewHolder>(response) {
            @Override
            protected void onBindViewHolder(NoticesViewHolder holder, int position, NoticeModel model) {
                final NoticeModel note = noticeList.get(position);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String loveDate= currentDate.format(calendar.getTime());


                holder.title.setText(note.getTitle());


                holder.itemView.findViewById(R.id.notice_lay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent wholeArticle=new Intent(getContext(), FullNotice.class);
                        wholeArticle.putExtra("position", note.getId());

                        startActivity(wholeArticle);
                    }
                });
            }

            @Override
            public NoticesViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notice_layout, parent, false);


                return new NoticesViewHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        allNotices.setAdapter(adapter);
    }


    public static class NoticesViewHolder extends RecyclerView.ViewHolder {
        public TextView title, details;


        public NoticesViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.notices_title);

        }


        private NoticesViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(NoticesViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }

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

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Notices");

    }
}
