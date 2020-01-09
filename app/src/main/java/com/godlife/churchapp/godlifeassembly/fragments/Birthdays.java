package com.godlife.churchapp.godlifeassembly.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Birthdays extends Fragment{
    private View birthView;
    private Button first, second, third, fourth, fifth,sixth, seven, eight, nine, ten,eleven, twelve;

    public Birthdays() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       birthView = inflater.inflate(R.layout.fragment_birthdays, container, false);

       first = birthView.findViewById(R.id.birth_january);
       first.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });
        second = birthView.findViewById(R.id.birth_february);
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        third = birthView.findViewById(R.id.birth_march);
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fourth = birthView.findViewById(R.id.birth_april);
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fifth = birthView.findViewById(R.id.birth_may);
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        sixth = birthView.findViewById(R.id.birth_june);
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        seven = birthView.findViewById(R.id.birth_July);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        eight = birthView.findViewById(R.id.birth_august);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        nine = birthView.findViewById(R.id.birth_september);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ten = birthView.findViewById(R.id.birth_october);
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        eleven = birthView.findViewById(R.id.birth_november);
        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        twelve = birthView.findViewById(R.id.birth_december);
        twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return birthView;
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Birthdays");

    }

}
