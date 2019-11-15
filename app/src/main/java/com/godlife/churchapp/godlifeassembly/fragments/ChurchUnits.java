package com.godlife.churchapp.godlifeassembly.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.godlife.churchapp.godlifeassembly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChurchUnits extends Fragment {
    private View church_units_fragment;

    public ChurchUnits() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        church_units_fragment = inflater.inflate(R.layout.fragment_church_units, container, false);


        return church_units_fragment;
    }

}
