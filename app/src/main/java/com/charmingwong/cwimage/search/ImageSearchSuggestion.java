package com.charmingwong.cwimage.search;

import android.annotation.SuppressLint;
import android.os.Parcel;

/**
 * Created by CharmingWong on 2017/5/21.
 */

@SuppressLint("ParcelCreator")
public class ImageSearchSuggestion implements com.arlib.floatingsearchview.suggestions.model.SearchSuggestion {

    public static final Creator<ImageSearchSuggestion> CREATOR = new Creator<ImageSearchSuggestion>() {
        @Override
        public ImageSearchSuggestion createFromParcel(Parcel parcel) {
            return new ImageSearchSuggestion(parcel);
        }

        @Override
        public ImageSearchSuggestion[] newArray(int i) {
            return new ImageSearchSuggestion[i];
        }
    };

    private final String suggestion;

    public boolean isSearchRecord() {
        return isSearchRecord;
    }

    public void setSearchRecord(boolean searchRecord) {
        isSearchRecord = searchRecord;
    }

    private boolean isSearchRecord;

    public ImageSearchSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String getBody() {
        return suggestion;
    }

    private ImageSearchSuggestion(Parcel in) {
        suggestion = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(suggestion);
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

        return true;
    }
}
