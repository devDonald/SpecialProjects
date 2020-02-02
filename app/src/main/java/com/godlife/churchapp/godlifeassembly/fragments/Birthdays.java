package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.BirthMonth;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.birthdays.Jan;
import com.google.firebase.messaging.FirebaseMessaging;

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

        FirebaseMessaging.getInstance().subscribeToTopic("Birthdays");
        first = birthView.findViewById(R.id.birth_january);
        first.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent jan = new Intent(getContext(), Jan.class);
               jan.putExtra("month", "01");
               jan.putExtra("tag","January Birthdays");
               startActivity(jan);
           }
       });
        second = birthView.findViewById(R.id.birth_february);
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "02");
                jan.putExtra("tag","February Birthdays");
                startActivity(jan);
            }
        });
        third = birthView.findViewById(R.id.birth_march);
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "03");
                jan.putExtra("tag","March Birthdays");
                startActivity(jan);
            }
        });
        fourth = birthView.findViewById(R.id.birth_april);
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "04");
                jan.putExtra("tag","April Birthdays");
                startActivity(jan);
            }
        });
        fifth = birthView.findViewById(R.id.birth_may);
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "05");
                jan.putExtra("tag","May Birthdays");
                startActivity(jan);
            }
        });
        sixth = birthView.findViewById(R.id.birth_june);
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "06");
                jan.putExtra("tag","June Birthdays");
                startActivity(jan);
            }
        });
        seven = birthView.findViewById(R.id.birth_July);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "07");
                jan.putExtra("tag","July Birthdays");
                startActivity(jan);
            }
        });
        eight = birthView.findViewById(R.id.birth_august);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "08");
                jan.putExtra("tag","August Birthdays");
                startActivity(jan);
            }
        });
        nine = birthView.findViewById(R.id.birth_september);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "09");
                jan.putExtra("tag","September Birthdays");
                startActivity(jan);
            }
        });
        ten = birthView.findViewById(R.id.birth_october);
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "10");
                jan.putExtra("tag","October Birthdays");
                startActivity(jan);
            }
        });
        eleven = birthView.findViewById(R.id.birth_november);
        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "11");
                jan.putExtra("tag","November Birthdays");
                startActivity(jan);
            }
        });

        twelve = birthView.findViewById(R.id.birth_december);
        twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jan = new Intent(getContext(), Jan.class);
                jan.putExtra("month", "12");
                jan.putExtra("tag","December Birthdays");
                startActivity(jan);
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
