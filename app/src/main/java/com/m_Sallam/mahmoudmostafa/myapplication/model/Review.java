package com.m_Sallam.mahmoudmostafa.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String review;
    public Review(String review) {
        this.review = review;
    }

    protected Review(Parcel in) {
        review = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(review);
    }
}
