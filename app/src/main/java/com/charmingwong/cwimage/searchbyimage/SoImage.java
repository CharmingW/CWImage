package com.charmingwong.cwimage.searchbyimage;

import android.os.Parcel;

import com.charmingwong.cwimage.base.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class SoImage extends BaseModel {

    public static final Creator<SoImage> CREATOR = new Creator<SoImage>() {
        @Override
        public SoImage createFromParcel(Parcel parcel) {
            return new SoImage(parcel);
        }

        @Override
        public SoImage[] newArray(int i) {
            return new SoImage[i];
        }
    };

    private final String url;

    private final String thumbUrl;

    private final int width;

    private final int height;

    private final int index;

    public static Creator<SoImage> getCREATOR() {
        return CREATOR;
    }

    public SoImage(JSONObject jsonImage) throws JSONException {
        url = jsonImage.getString("objURL");
        thumbUrl = jsonImage.getString("thumbURL");
        width = jsonImage.getInt("width");
        height = jsonImage.getInt("height");
        index = jsonImage.getInt("ImageIndexNumber");
    }

    private SoImage(Parcel in) {
        url = in.readString();
        thumbUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
        index = in.readInt();
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
        parcel.writeInt(index);
    }

    public String getUrl() {
        return url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public int getIndex() {
        return index;
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

        SoImage soImage = (SoImage) o;

        if (!url.equals(soImage.url)) {
            return false;
        }
        if (!thumbUrl.equals(soImage.thumbUrl)) {
            return false;
        }
        if (width != soImage.width) {
            return false;
        }
        if (height != soImage.height) {
            return false;
        }
        if (index != soImage.index) {
            return false;
        }

        return true;
    }
}
