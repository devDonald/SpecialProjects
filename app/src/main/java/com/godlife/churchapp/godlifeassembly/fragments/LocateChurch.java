package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.JosChurchMap;
import com.godlife.churchapp.godlifeassembly.activities.ZariaChurchMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocateChurch extends Fragment {

    private Button jos_church, zaria_church, minna_church;
    private View locate_church_view;
    public LocateChurch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        locate_church_view = inflater.inflate(R.layout.fragment_locate_church, container, false);

        jos_church = locate_church_view.findViewById(R.id.bt_jos_church);
        zaria_church = locate_church_view.findViewById(R.id.bt_zaria_church);
//        minna_church = locate_church_view.findViewById(R.id.bt_minna_church);

        jos_church.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jos = new Intent(getContext(), JosChurchMap.class);
                startActivity(jos);
            }
        });

        zaria_church.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent zaria = new Intent(getContext(), ZariaChurchMap.class);
                startActivity(zaria);
            }
        });


        return locate_church_view;
    }

}
