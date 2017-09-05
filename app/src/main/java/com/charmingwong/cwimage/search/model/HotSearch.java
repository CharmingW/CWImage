package com.charmingwong.cwimage.search.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CharmingWong on 2017/6/2.
 */

public class HotSearch implements Parcelable {


    public static final Creator<HotSearch> CREATOR = new Creator<HotSearch>() {
        @Override
        public HotSearch createFromParcel(Parcel parcel) {
            return new HotSearch(parcel);
        }

        @Override
        public HotSearch[] newArray(int i) {
            return new HotSearch[i];
        }
    };

    private final String imageUrl;

    public String getQuery() {
        return query;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    private final String query;


    public static Parcelable.Creator<HotSearch> getCREATOR() {
        return CREATOR;
    }

    public HotSearch(String imageUrl, String query) {
        this.imageUrl = imageUrl;
        this.query = query;

    }

    private HotSearch(Parcel in) {
        imageUrl = in.readString();
        query = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(query);
    }

    @SuppressWarnings({"PMD.SimplifyBooleanReturns", "RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HotSearch image = (HotSearch) o;

        if (!image.imageUrl.equals(imageUrl)) {
            return false;
        }

        if (!image.query.equals(query)) {
            return false;
        }

        return true;
    }
}
