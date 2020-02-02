package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.Chats;
import com.godlife.churchapp.godlifeassembly.activities.Login;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.live_service.LiveService;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button summary, chat, locate, videos,audios, live_service;
    private RelativeLayout one, two, three, four, five, six;

    private View homefragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homefragment = inflater.inflate(R.layout.fragment_home, container, false);

        summary = homefragment.findViewById(R.id.bt_service_summary);
        chat = homefragment.findViewById(R.id.tv_chat);
        locate = homefragment.findViewById(R.id.bt_locate_church);
        live_service = homefragment.findViewById(R.id.tv_live_streaming);
        videos = homefragment.findViewById(R.id.tv_videos_text);
        audios = homefragment.findViewById(R.id.tv_audio_messages);

        one = homefragment.findViewById(R.id.rv_locate_church);
        two = homefragment.findViewById(R.id.rv_love);
        three = homefragment.findViewById(R.id.rv_testimony);
        four = homefragment.findViewById(R.id.rv_events);
        five = homefragment.findViewById(R.id.rv_articles);
        six = homefragment.findViewById(R.id.rv_service_summary);


        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent my_profile = new Intent(getContext(), Login.class);
                startActivity(my_profile);
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceSummary  toBirthdays = new ServiceSummary();
                addFragment(toBirthdays);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveService meetings = new LiveService();
                addFragment(meetings);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoMessages  loveNotes = new VideoMessages();
                addFragment(loveNotes);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioMessages audioMessages = new AudioMessages();
                addFragment(audioMessages);
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocateChurch  locateChurch = new LocateChurch();
                addFragment(locateChurch);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent my_profile = new Intent(getContext(), Login.class);
                startActivity(my_profile);
            }
        });

        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceSummary  toBirthdays = new ServiceSummary();
                addFragment(toBirthdays);
            }
        });
        live_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveService meetings = new LiveService();
                addFragment(meetings);
            }
        });
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoMessages  loveNotes = new VideoMessages();
                addFragment(loveNotes);
            }
        });

        audios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioMessages audioMessages = new AudioMessages();
                addFragment(audioMessages);
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocateChurch  locateChurch = new LocateChurch();
                addFragment(locateChurch);
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
