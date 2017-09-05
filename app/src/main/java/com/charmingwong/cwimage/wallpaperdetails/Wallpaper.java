package com.charmingwong.cwimage.wallpaperdetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.charmingwong.cwimage.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CharmingWong on 2017/6/7.
 */

public class Wallpaper extends BaseModel {

    public static final Parcelable.Creator<Wallpaper> CREATOR = new Parcelable.Creator<Wallpaper>() {
        @Override
        public Wallpaper createFromParcel(Parcel parcel) {
            return new Wallpaper(parcel);
        }

        @Override
        public Wallpaper[] newArray(int i) {
            return new Wallpaper[i];
        }
    };

    private final String url;
    private final String thumbUrl;
    private final int width;
    private final int height;

    public static Parcelable.Creator<Wallpaper> getCREATOR() {
        return CREATOR;
    }

    public Wallpaper(JSONObject jsonImage) throws JSONException {
        url = jsonImage.getString("url");
        thumbUrl = jsonImage.getString("thumbUrl");
        width = jsonImage.getInt("width");
        height = jsonImage.getInt("height");
    }

    private Wallpaper(Parcel in) {
        url = in.readString();
        thumbUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(thumbUrl);
        parcel.writeInt(width);
        parcel.writeInt(height);
    }


    public String getUrl() {
        return url;
    }

    @Override
    public String getThumbUrl() {
        return thumbUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
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

        Wallpaper wallpaper = (Wallpaper) o;

        if (!url.equals(wallpaper.url)) {
            return false;
        }
        if (!thumbUrl.equals(wallpaper.thumbUrl)) {
            return false;
        }
        if (width != wallpaper.width) {
            return false;
        }
        if (height != wallpaper.height) {
            return false;
        }
        return true;
    }

}
