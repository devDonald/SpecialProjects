package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.FullLyrics;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.LyricsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChurchSongs extends Fragment {

    private View church_lyrics;
    private RecyclerView allLyrics_RV;
    private DatabaseReference lyricsDb;
    private Context context;
    private FirebaseRecyclerAdapter<LyricsModel,LyricsViewHolder> firebaseRecyclerAdapter;


    public ChurchSongs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        church_lyrics = inflater.inflate(R.layout.fragment_church_songs, container, false);

        allLyrics_RV= church_lyrics.findViewById(R.id.all_church_songs);
        lyricsDb= FirebaseDatabase.getInstance().getReference().child("Lyrics");
        //allmembers_RV.setHasFixedSize(true);
        allLyrics_RV.setLayoutManager(new LinearLayoutManager(context));

        context = getActivity();
        return church_lyrics;
    }


    @Override
    public void onStart() {
        super.onStart();

        showLyrics();
    }

    public static class LyricsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public LyricsViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(view, getAdapterPosition());

                }
            });
        }
        public void setTitle(String title){
            TextView titles= mView.findViewById(R.id.lyrics_title);
            titles.setText(title);

        }
        public void setAuthor(String author){
            TextView authors = mView.findViewById(R.id.lyrics_author);
            authors.setText(author);
        }


        private LyricsViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(LyricsViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }


    }

    public void showLyrics(){

        Query firebaseSearchQuery = lyricsDb.orderByChild("songTitle");

        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<LyricsModel, LyricsViewHolder>(
                LyricsModel.class,
                R.layout.lyrics_layout,
                LyricsViewHolder.class,
                firebaseSearchQuery
        ) {

            @Override
            protected void populateViewHolder(LyricsViewHolder viewHolder, LyricsModel model, int position) {
                try {
                    viewHolder.setTitle(model.getSongTitle());
                    viewHolder.setAuthor(model.getWrittenBy());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public LyricsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LyricsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new LyricsViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Toast.makeText(getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();

                        Intent wholeLyrics=new Intent(context, FullLyrics.class);
                        wholeLyrics.putExtra("position",firebaseRecyclerAdapter.getRef(position).getKey());

                        startActivity(wholeLyrics);

                    }

                });

                return viewHolder;
            }
        };
        allLyrics_RV.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Lyrics");

    }

}
