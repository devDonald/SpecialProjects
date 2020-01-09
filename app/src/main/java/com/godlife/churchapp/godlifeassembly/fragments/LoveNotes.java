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
import com.godlife.churchapp.godlifeassembly.activities.FullLove;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.LoveModel;
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
public class LoveNotes extends Fragment {

    private View love_view;
    private RecyclerView allLoveNotes;
    private static final String TAG = "Love Notes";

    private FirestoreRecyclerAdapter adapter;

    private FirebaseFirestore loveDB;
    private ListenerRegistration firestoreListener;
    private List<LoveModel> notesList;
    private FirebaseFirestoreSettings settings;



    public LoveNotes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        love_view = inflater.inflate(R.layout.fragment_love_notes, container, false);


        allLoveNotes = love_view.findViewById(R.id.love_recycler);
        loveDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        loveDB.setFirestoreSettings(settings);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        allLoveNotes.setLayoutManager(mLayoutManager);
        allLoveNotes.setItemAnimator(new DefaultItemAnimator());

        loadLoveNotesList();

        firestoreListener = loveDB.collection("Love")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        notesList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            LoveModel note = doc.toObject(LoveModel.class);
                            note.setId(doc.getId());
                            notesList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        allLoveNotes.setAdapter(adapter);
                    }
                });
        return love_view;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author, date;


        public NoteViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.love_title);

            author = view.findViewById(R.id.love_author);
            date = view.findViewById(R.id.love_date);
        }


        private NoteViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(NoteViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        firestoreListener.remove();
    }

    private void loadLoveNotesList() {

        Query query = loveDB.collection("Love").orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<LoveModel> response = new FirestoreRecyclerOptions.Builder<LoveModel>()
                .setQuery(query, LoveModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<LoveModel, NoteViewHolder>(response) {
            @Override
            protected void onBindViewHolder(NoteViewHolder holder, int position, LoveModel model) {
                final LoveModel note = notesList.get(position);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String loveDate= currentDate.format(calendar.getTime());
                holder.title.setText(note.getTitle());
                holder.author.setText(note.getAuthor());

                if (loveDate.equals(note.getLoveDate())){
                    holder.date.setText("Today");
                }


                holder.itemView.findViewById(R.id.love_lay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent wholeArticle=new Intent(getContext(), FullLove.class);
                        wholeArticle.putExtra("position", note.getId());

                        startActivity(wholeArticle);
                    }
                });
            }

            @Override
            public NoteViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.love_layout, parent, false);


                return new NoteViewHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        allLoveNotes.setAdapter(adapter);
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

        ((MainActivity) getActivity()).setActionBarTitle("Love Note");

    }

}
