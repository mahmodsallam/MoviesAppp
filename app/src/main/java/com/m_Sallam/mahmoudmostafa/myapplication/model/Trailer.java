package com.m_Sallam.mahmoudmostafa.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mahmoud mostafa  on 1/27/2018.
 */

public class Trailer implements Parcelable {
    public Trailer(String key) {
        this.key = key;
    }

    private String key;

    protected Trailer(Parcel in) {
        key = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
    }
}
