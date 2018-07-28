package com.m_Sallam.mahmoudmostafa.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.m_Sallam.mahmoudmostafa.myapplication.R;
import com.m_Sallam.mahmoudmostafa.myapplication.model.Review;
import java.util.ArrayList;
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.myViewHolder> {

    Context context;
    ArrayList<Review> reviewsList;

    public ReviewsAdapter(Context context, ArrayList<Review> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;

    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public myViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.reviewtextRow);
        }
    }
    @Override
    public ReviewsAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new myViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ReviewsAdapter.myViewHolder holder, int position) {
        Review R = reviewsList.get(position);
        holder.text.setText(R.getReview()); //getting the review and adding it
    }
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
