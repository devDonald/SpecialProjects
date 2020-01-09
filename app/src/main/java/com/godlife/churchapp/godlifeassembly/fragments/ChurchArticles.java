package com.godlife.churchapp.godlifeassembly.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.FullArticle;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.godlife.churchapp.godlifeassembly.models.ArticleModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ChurchArticles extends Fragment {

    private View church_articles;
    private RecyclerView allArticles_RV;
    private DatabaseReference articlesDb;
    private Context context;
    private FirebaseRecyclerAdapter<ArticleModel, ArticlesViewHolder> firebaseRecyclerAdapter;


    public ChurchArticles() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        church_articles = inflater.inflate(R.layout.fragment_church_articles, container, false);

        allArticles_RV = church_articles.findViewById(R.id.articles_recycler);
        articlesDb = FirebaseDatabase.getInstance().getReference().child("Articles");
        //allmembers_RV.setHasFixedSize(true);
        allArticles_RV.setLayoutManager(new LinearLayoutManager(context));

        context = getActivity();
        return church_articles;
    }


    @Override
    public void onStart() {
        super.onStart();

        showArticle();
        firebaseRecyclerAdapter.startListening();

    }

    public static class ArticlesViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ArticlesViewHolder(View itemView) {
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
            TextView titles= mView.findViewById(R.id.articles_title);
            titles.setText(title);

        }
        public void setAuthor(String author){
            TextView authors = mView.findViewById(R.id.articles_author);
            authors.setText(author);
        }
        public void setDate(String date){
            TextView pdate = mView.findViewById(R.id.articles_date);
            pdate.setText(date);
        }


        private ArticlesViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(ArticlesViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }


    }

    public void showArticle(){

        Query firebaseSearchQuery = articlesDb.orderByChild("date");

        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<ArticleModel, ArticlesViewHolder>(
                ArticleModel.class,
                R.layout.articles_layout,
                ArticlesViewHolder.class,
                firebaseSearchQuery
        ) {

            @Override
            protected void populateViewHolder(ArticlesViewHolder viewHolder, ArticleModel model, int position) {
                try {
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setAuthor(model.getAuthor());
                    viewHolder.setDate(model.getDate() +" at "+ model.getTime());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ArticlesViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ArticlesViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Toast.makeText(getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();

                        Intent wholeArticle=new Intent(context, FullArticle.class);
                        wholeArticle.putExtra("position",firebaseRecyclerAdapter.getRef(position).getKey());

                        startActivity(wholeArticle);

                    }

                });

                return viewHolder;
            }
        };
        allArticles_RV.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Church Articles");

    }

}
