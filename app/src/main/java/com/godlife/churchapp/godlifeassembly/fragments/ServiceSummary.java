package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Context;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.ArticleModel;
import com.godlife.churchapp.godlifeassembly.models.LoveModel;
import com.godlife.churchapp.godlifeassembly.models.LyricsModel;
import com.godlife.churchapp.godlifeassembly.models.ServiceModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
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
public class ServiceSummary extends Fragment {

    private View service_summary;
    private RecyclerView allSummary_RV;
    private DatabaseReference summaryDb;
    private Context context;
    private FirebaseRecyclerAdapter<ServiceModel,ServiceViewHolder> firebaseRecyclerAdapter;


    private List<ServiceModel> serviceList;


    public ServiceSummary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        service_summary = inflater.inflate(R.layout.fragment_service_summary, container, false);

        allSummary_RV = service_summary.findViewById(R.id.service_summary_rv);
        summaryDb = FirebaseDatabase.getInstance().getReference().child("Updates");

        context = getActivity();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        allSummary_RV.setLayoutManager(mLayoutManager);
        allSummary_RV.setItemAnimator(new DefaultItemAnimator());

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
        Query firebaseSearchQuery = summaryDb.orderByChild("time");

        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<ServiceModel, ServiceViewHolder>(
                ServiceModel.class,
                R.layout.service_layout,
                ServiceViewHolder.class,
                firebaseSearchQuery
        ) {

            @Override
            protected void populateViewHolder(ServiceViewHolder holder, ServiceModel model, int position) {

                String todayDate,yesterdayDate;
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
                try {
                    holder.serviceTag.setText(model.getService_tag());
                    holder.serviceNugget.setText(model.getService_nugget());
                    holder.serviceMinister.setText(model.getService_minister());
                    holder.serviceTime.setText(model.getService_time());

                    if (todayDate.matches(model.getService_date())){
                        holder.serviceDate.setText("Today");
                    } else if (yesterdayDate.matches(model.getService_date())){
                        holder.serviceDate.setText("Yesterday");
                    } else{
                        holder.serviceDate.setText(model.getService_date());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ServiceViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                return viewHolder;
            }
        };
        allSummary_RV.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        allSummary_RV.smoothScrollToPosition(allSummary_RV.getAdapter().getItemCount());
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Live Service Update");

    }


    @Override
    public void onStart() {
        super.onStart();
        loadSummaryList();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
