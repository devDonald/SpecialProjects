package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Context;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.ArticleModel;
import com.godlife.churchapp.godlifeassembly.models.LoveModel;
import com.godlife.churchapp.godlifeassembly.models.ServiceModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
public class ServiceSummary extends Fragment {

    private View service_summary;
    private RecyclerView allSummary_RV;
    private FirebaseFirestore summaryDb;
    private Context context;
    private FirestoreRecyclerAdapter adapter;

    private ListenerRegistration firestoreListener;
    private List<ServiceModel> serviceList;
    private FirebaseFirestoreSettings settings;


    public ServiceSummary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        service_summary = inflater.inflate(R.layout.fragment_service_summary, container, false);

        allSummary_RV = service_summary.findViewById(R.id.service_summary_rv);

        summaryDb = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        summaryDb.setFirestoreSettings(settings);
        context = getActivity();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        allSummary_RV.setLayoutManager(mLayoutManager);
        allSummary_RV.setItemAnimator(new DefaultItemAnimator());

        loadSummaryList();

        firestoreListener = summaryDb.collection("Updates")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Summary", "Listen failed!", e);
                            return;
                        }

                        serviceList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            ServiceModel note = doc.toObject(ServiceModel.class);
                            note.setId(doc.getId());
                            serviceList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        allSummary_RV.setAdapter(adapter);
                    }
                });

        return service_summary;
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder{

        public TextView serviceTag,serviceMinister, serviceNugget,serviceTime, serviceDate;


        public ServiceViewHolder(View view) {
            super(view);
            serviceTag = view.findViewById(R.id.view_service_tag);

            serviceMinister = view.findViewById(R.id.view_service_minister);
            serviceNugget = view.findViewById(R.id.view_service_nugget);
            serviceTime = view.findViewById(R.id.view_service_time);
            serviceDate = view.findViewById(R.id.view_service_date);
        }


    }

    public void loadSummaryList(){


        final Query query = summaryDb.collection("Updates")
                .orderBy("service_date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ServiceModel> response = new FirestoreRecyclerOptions.Builder<ServiceModel>()
                .setQuery(query, ServiceModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ServiceModel, ServiceViewHolder>(response) {
            @Override
            protected void onBindViewHolder(ServiceViewHolder holder, int position, ServiceModel model) {

                final ServiceModel note = serviceList.get(position);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String serviceDate= currentDate.format(calendar.getTime());
                holder.serviceTag.setText(note.getService_tag());
                holder.serviceNugget.setText(note.getService_nugget());
                holder.serviceMinister.setText(note.getService_minister());
                holder.serviceTime.setText(note.getService_time());

                if (serviceDate.equals(note.getService_date())){
                    holder.serviceDate.setText("Today");
                }


            }

            @Override
            public ServiceViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.service_layout, parent, false);

                return new ServiceViewHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        allSummary_RV.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Live Service Update");

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
