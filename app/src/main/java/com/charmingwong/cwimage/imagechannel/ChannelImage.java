package com.charmingwong.cwimage.imagechannel;

import android.os.Parcel;
import android.os.Parcelable;

import com.charmingwong.cwimage.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CharmingWong on 2017/5/15.
 */

public class ChannelImage extends BaseModel {

    public static final Creator<ChannelImage> CREATOR = new Creator<ChannelImage>() {
        @Override
        public ChannelImage createFromParcel(Parcel parcel) {
            return new ChannelImage(parcel);
        }

        @Override
        public ChannelImage[] newArray(int i) {
            return new ChannelImage[i];
        }
    };

    private final String id;
    private final String url;
    private final String thumbUrl;
    private final int width;
    private final int height;
    private final int thumbWidth;
    private final int thumbHeight;
    private final String title;
    private final String label;
    private final int index;


    public static Parcelable.Creator<ChannelImage> getCREATOR() {
        return CREATOR;
    }

    public ChannelImage(JSONObject jsonImage) throws JSONException {
        id = jsonImage.getString("id");
        url = jsonImage.getString("qhimg_url");
        thumbUrl = jsonImage.getString("qhimg_thumb_url");
        width = jsonImage.getInt("cover_width");
        height = jsonImage.getInt("cover_height");
        thumbWidth = jsonImage.getInt("qhimg_width");
        thumbHeight = jsonImage.getInt("qhimg_height");
        title = jsonImage.getString("group_title");
        String l;
        try {
            l = jsonImage.getString("label");
        } catch (JSONException e) {
            l = "";
        }
        label = l;
        index = jsonImage.getInt("index");
    }

    private ChannelImage(Parcel in) {
        id = in.readString();
        url = in.readString();
        thumbUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
        thumbWidth = in.readInt();
        thumbHeight = in.readInt();
        title = in.readString();
        label = in.readString();
        index = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(url);
        parcel.writeString(thumbUrl);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeInt(thumbWidth);
        parcel.writeInt(thumbHeight);
        parcel.writeString(title);
        parcel.writeString(label);
        parcel.writeInt(index);
    }


    public String getId() {
        return id;
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

    public int getThumbWidth() {
        return thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public String getTitle() {
        return title;
    }

    public String getLabel() {
        return label;
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

        ChannelImage image = (ChannelImage) o;

        if (!id.equals(image.id)) {
            return false;
        }
        if (!url.equals(image.url)) {
            return false;
        }
        if (index != image.index) {
            return false;
        }
        if (width != image.width) {
            return false;
        }
        if (height != image.height) {
            return false;
        }
        if (!thumbUrl.equals(image.thumbUrl)) {
            return false;
        }
        if (thumbWidth != image.thumbWidth) {
            return false;
        }
        if (!title.equals(image.title)) {
            return false;
        }
        if (!label.equals(image.label)) {
            return false;
        }
        if (thumbHeight != image.thumbHeight) {
            return false;
        }

        return true;
    }
}
