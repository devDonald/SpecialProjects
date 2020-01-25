package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.Chats;
import com.godlife.churchapp.godlifeassembly.activities.Login;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.live_service.LiveService;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button birthdays, chat, service, videos,audios, live_service;

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
        chat = homefragment.findViewById(R.id.tv_chat);
        service = homefragment.findViewById(R.id.tv_service_text);
        live_service = homefragment.findViewById(R.id.tv_live_streaming);
        videos = homefragment.findViewById(R.id.tv_videos_text);
        audios = homefragment.findViewById(R.id.tv_audio_messages);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent my_profile = new Intent(getContext(), Login.class);
                startActivity(my_profile);
            }
        });

        birthdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Birthdays  toBirthdays = new Birthdays();
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
