package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Intent;
import android.os.Build;
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
import com.godlife.churchapp.godlifeassembly.activities.FullPraise;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.PraiseModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PraiseReport extends Fragment {
    private View praise_view;
    private RecyclerView allPraiseNotes;
    private static final String TAG = "Praise Report";

    private FirestoreRecyclerAdapter adapter;

    private FirebaseFirestore praiseDB;
    private ListenerRegistration firestoreListener;
    private List<PraiseModel> praiseList;
    private FirebaseFirestoreSettings settings;
    public PraiseReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        praise_view = inflater.inflate(R.layout.fragment_praise_report, container, false);


        allPraiseNotes = praise_view.findViewById(R.id.praise_recycler);

        praiseDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        praiseDB.setFirestoreSettings(settings);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        allPraiseNotes.setLayoutManager(mLayoutManager);
        allPraiseNotes.setItemAnimator(new DefaultItemAnimator());

        loadPraiseList();

        firestoreListener = praiseDB.collection("Praise")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        praiseList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            PraiseModel note = doc.toObject(PraiseModel.class);
                            note.setId(doc.getId());
                            praiseList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        allPraiseNotes.setAdapter(adapter);
                    }
                });


        return praise_view;
    }


    public static class PraiseViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author, date;


        public PraiseViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.praise_title);

            author = view.findViewById(R.id.praise_author);
            date = view.findViewById(R.id.praise_date);
        }


        private PraiseViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(PraiseViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        firestoreListener.remove();
    }

    private void loadPraiseList() {

        Query query = praiseDB.collection("Praise").orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<PraiseModel> response = new FirestoreRecyclerOptions.Builder<PraiseModel>()
                .setQuery(query, PraiseModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PraiseModel, PraiseViewHolder>(response) {
            @Override
            protected void onBindViewHolder(PraiseViewHolder holder, int position, PraiseModel model) {
                final PraiseModel note = praiseList.get(position);
                String todayDate, yesterdayDate;

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                {
                    LocalDate localDate = LocalDate.now();
                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
                    todayDate = localDate.format(formatter);
                    yesterdayDate = yesterday.format(formatter);
                }
                else
                {
                    Date c = Calendar.getInstance().getTime();
                    DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
                    todayDate = df.format(c);
                    final Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    yesterdayDate=df.format(cal.getTime());
                }

                holder.title.setText(note.getTitle());
                holder.author.setText(note.getAuthor());

                if (todayDate.equals(note.getPraiseDate())){
                    holder.date.setText("Today");
                }else if (yesterdayDate.equals(note.getPraiseDate())){
                    holder.date.setText("Yesterday");
                }
                else{
                    holder.date.setText(note.getPraiseDate());

                }

                holder.itemView.findViewById(R.id.praise_lay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent wholeArticle=new Intent(getContext(), FullPraise.class);
                        wholeArticle.putExtra("position", note.getId());

                        startActivity(wholeArticle);
                    }
                });
            }

            @Override
            public PraiseViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.praise_layout, parent, false);


                return new PraiseViewHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        allPraiseNotes.setAdapter(adapter);
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

        ((MainActivity) getActivity()).setActionBarTitle("Praise Report");

    }


}
