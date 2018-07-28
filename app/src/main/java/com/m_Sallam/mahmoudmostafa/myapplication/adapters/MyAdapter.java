package com.m_Sallam.mahmoudmostafa.myapplication.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.m_Sallam.mahmoudmostafa.myapplication.R;
import com.m_Sallam.mahmoudmostafa.myapplication.activities.Detail;
import com.m_Sallam.mahmoudmostafa.myapplication.model.Movie;
import com.m_Sallam.mahmoudmostafa.myapplication.store.MoviesProvider;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Movie> movie_list;

    public MyAdapter(Context context, ArrayList<Movie> movie_list) {
        this.context = context;
        this.movie_list = movie_list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView release_date;
        TextView overview;
        ImageView movie_image;
        ImageView like;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "daisy.ttf");
            title.setTypeface(typeface);
            release_date = (TextView) itemView.findViewById(R.id.releaseDate);
            overview = (TextView) itemView.findViewById(R.id.overview);
            movie_image = (ImageView) itemView.findViewById(R.id.movie_image);

            like = (ImageView) itemView.findViewById(R.id.like);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Movie movie = movie_list.get(position);
        holder.title.setText(movie.getTitle());
        holder.release_date.setText(movie.getReleaseDate());
        holder.overview.setText(movie.getOverview());
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath()).into(holder.movie_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                //animation for shared element transition for movie poster
                //making the transition
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) context,holder.movie_image,
                            holder.movie_image.getTransitionName()).toBundle()  ;
                    //normal
                    Intent intent = new Intent(context, Detail.class);
                    Movie movie = movie_list.get(position);
                    intent.putExtra("movie", movie); //sending movie object
                    context.startActivity(intent , bundle );
                    Toast.makeText(context,movie.getId().toString(), Toast.LENGTH_LONG).show();
                }
                else {

                    //normal
                    Intent intent = new Intent(context, Detail.class);
                    Movie movie = movie_list.get(position);
                    intent.putExtra("movie", movie); //sending movie object
                    context.startActivity(intent);
                    Toast.makeText(context, movie.getId().toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageResource(R.drawable.ic_like);

                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_id", movie.getId());
                    contentValues.put("title", movie.getTitle());
                    contentValues.put("releaseDate", movie.getReleaseDate());
                    contentValues.put("review", movie.getOverview());
                    contentValues.put("posterPath", movie.getPosterPath());
                    contentValues.put("voteAverage" , movie.getVoteAverage());

                    Uri uri = context.getContentResolver().insert(MoviesProvider.CONTENT_URL, contentValues);
                    Toast.makeText(context, "succeeded", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, "Already exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movie_list.size();
    }
}