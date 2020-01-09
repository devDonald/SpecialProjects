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
import com.godlife.churchapp.godlifeassembly.activities.FullMeeting;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.EventsModel;
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
public class ComingMeetings extends Fragment {

    private View events_view;
    private RecyclerView allEvents;
    private static final String TAG = "Events";

    private FirestoreRecyclerAdapter adapter;

    private FirebaseFirestore eventsDB;
    private ListenerRegistration firestoreListener;
    private List<EventsModel> eventsList;
    private FirebaseFirestoreSettings settings;

    public ComingMeetings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        events_view = inflater.inflate(R.layout.fragment_coming_meetings, container, false);
        allEvents = events_view.findViewById(R.id.event_recycler);

        eventsDB = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        eventsDB.setFirestoreSettings(settings);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        allEvents.setLayoutManager(mLayoutManager);
        allEvents.setItemAnimator(new DefaultItemAnimator());

        loadNotices();

        firestoreListener = eventsDB.collection("Meetings")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        eventsList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            EventsModel note = doc.toObject(EventsModel.class);
                            note.setId(doc.getId());
                            eventsList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        allEvents.setAdapter(adapter);
                    }
                });


        return events_view;
    }

    private void loadNotices() {

        Query query = eventsDB.collection("Meetings");

        FirestoreRecyclerOptions<EventsModel> response = new FirestoreRecyclerOptions.Builder<EventsModel>()
                .setQuery(query, EventsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<EventsModel, EventsViewHolder>(response) {
            @Override
            protected void onBindViewHolder(EventsViewHolder holder, int position, EventsModel model) {
                final EventsModel note = eventsList.get(position);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String loveDate= currentDate.format(calendar.getTime());


                holder.title.setText(note.getTitle());


                holder.itemView.findViewById(R.id.meeting_lay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent wholeArticle=new Intent(getContext(), FullMeeting.class);
                        wholeArticle.putExtra("position", note.getId());

                        startActivity(wholeArticle);
                    }
                });
            }

            @Override
            public EventsViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.meeting_layout, parent, false);


                return new EventsViewHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        allEvents.setAdapter(adapter);
    }



    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        public TextView title, details;


        public EventsViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.meeting_title);

        }


        private EventsViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(EventsViewHolder.ClickListener clickListener){
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

        ((MainActivity) getActivity()).setActionBarTitle("Upcoming Meetings");

    }

}
