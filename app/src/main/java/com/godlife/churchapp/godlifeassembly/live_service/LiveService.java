package com.godlife.churchapp.godlifeassembly.live_service;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.godlife.churchapp.godlifeassembly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveService extends Fragment {

    private View liveView;
    private Button fb_live, youtube_live, live_radio1, live_radio2;

    public LiveService() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        liveView = inflater.inflate(R.layout.fragment_live, container, false);

        youtube_live = liveView.findViewById(R.id.bt_youtube_video);
//        live_radio1 = liveView.findViewById(R.id.bt_audio_stream1);



        youtube_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent yt = new Intent(getContext(),YoutubeLive.class);
                startActivity(yt);
            }
        });

//        live_radio1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent liveRadio = new Intent(getContext(),LiveRadio.class);
//                startActivity(liveRadio);
//            }
//        });


        return liveView;


    }

}
