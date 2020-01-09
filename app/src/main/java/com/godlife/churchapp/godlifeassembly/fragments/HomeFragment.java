package com.godlife.churchapp.godlifeassembly.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button birthdays, notices, service, love,praise, meetings;

    private View homefragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homefragment = inflater.inflate(R.layout.fragment_home, container, false);

        birthdays = homefragment.findViewById(R.id.bt_birthdays_text);
        notices = homefragment.findViewById(R.id.tv_notices_text);
        service = homefragment.findViewById(R.id.tv_service_text);
        meetings = homefragment.findViewById(R.id.tv_upcoming_text);
        love = homefragment.findViewById(R.id.tv_lovenote_text);
        praise = homefragment.findViewById(R.id.tv_praise_report_text);

        notices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notices  toNotices = new Notices();
                addFragment(toNotices);
            }
        });

        birthdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Birthdays  toBirthdays = new Birthdays();
                addFragment(toBirthdays);
            }
        });
        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComingMeetings meetings = new ComingMeetings();
                addFragment(meetings);
            }
        });
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoveNotes  loveNotes = new LoveNotes();
                addFragment(loveNotes);
            }
        });

        praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PraiseReport praiseReport = new PraiseReport();
                addFragment(praiseReport);
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceSummary  summary = new ServiceSummary();
                addFragment(summary);
            }
        });

        return homefragment;
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Home");

    }

    public void addFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
