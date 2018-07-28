package com.m_Sallam.mahmoudmostafa.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m_Sallam.mahmoudmostafa.myapplication.R;
import com.m_Sallam.mahmoudmostafa.myapplication.model.Trailer;

import java.util.ArrayList;

/**
 * Created by Mahmoud mustafa  on 1/27/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.myHolder> {
    ArrayList<Trailer> trailers;
    Context context;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    //declaring class as a view holder
    class myHolder extends RecyclerView.ViewHolder {

        TextView text;

        public myHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.reviewtextRow);
        }
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(myHolder holder, int position) {
        final Trailer trailer = trailers.get(position);
        holder.text.setText("trailer" + position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" +
                        trailer.getKey()));
                context.startActivity(youtube);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
