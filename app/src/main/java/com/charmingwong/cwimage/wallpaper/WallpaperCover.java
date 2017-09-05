package com.charmingwong.cwimage.wallpaper;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CharmingWong on 2017/6/7.
 */

public class WallpaperCover implements Parcelable {

    public static final Parcelable.Creator<WallpaperCover> CREATOR = new Parcelable.Creator<WallpaperCover>() {
        @Override
        public WallpaperCover createFromParcel(Parcel parcel) {
            return new WallpaperCover(parcel);
        }

        @Override
        public WallpaperCover[] newArray(int i) {
            return new WallpaperCover[i];
        }
    };

    private final String imageUrl;
    private final String title;
    private final String detailsUrl;
    private final int width;
    private final int height;

    public static Parcelable.Creator<WallpaperCover> getCREATOR() {
        return CREATOR;
    }

    public WallpaperCover(JSONObject jsonImage) throws JSONException {
        imageUrl = jsonImage.getString("imageUrl");
        width = jsonImage.getInt("width");
        height = jsonImage.getInt("height");
        title = jsonImage.getString("title");
        detailsUrl = jsonImage.getString("detailsUrl");
    }

    private WallpaperCover(Parcel in) {
        imageUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
        title = in.readString();
        detailsUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(title);
        parcel.writeString(detailsUrl);
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public String getDetailsUrl() {
        return detailsUrl;
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

        WallpaperCover cover = (WallpaperCover) o;

        if (!imageUrl.equals(cover.imageUrl)) {
            return false;
        }
        if (!title.equals(cover.title)) {
            return false;
        }
        if (width != cover.width) {
            return false;
        }
        if (height != cover.height) {
            return false;
        }
        if (!detailsUrl.equals(cover.detailsUrl)) {
            return false;
        }

        return true;
    }

}
