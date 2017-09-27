package com.charmingwong.cwimage.imagesearch;

import android.os.Parcel;

import com.charmingwong.cwimage.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A POJO representing a JSON object returned from Flickr's api representing a single image.
 */
public class QImage extends BaseModel {

    public static final Creator<QImage> CREATOR = new Creator<QImage>() {
        @Override
        public QImage createFromParcel(Parcel parcel) {
            return new QImage(parcel);
        }

        @Override
        public QImage[] newArray(int i) {
            return new QImage[i];
        }
    };

    //    private final String id;
//    private final String title;
    private final String url;
    private final String thumbUrl;
    private final String imageSize;
    //    private final String imageType;
    private final int width;
    private final int height;
//    private final int thumbWidth;
//    private final int thumbHeight;

    public static Creator<QImage> getCREATOR() {
        return CREATOR;
    }

    public QImage(JSONObject jsonImage, int api) throws JSONException {
//        id = jsonImage.getString("id");
//        title = jsonImage.getString("title");
        if (api == ImageFinder.QH360) {
            url = jsonImage.getString("img");
            thumbUrl = jsonImage.getString("thumb");
            width = jsonImage.getInt("width");
            height = jsonImage.getInt("height");
//            thumbWidth = jsonImage.getInt("thumbWidth");
//            thumbHeight = jsonImage.getInt("thumbHeight");
//            index = jsonImage.getInt("index");
            imageSize = jsonImage.getString("imgsize");
//            imageType = jsonImage.getString("imgtype");
        } else if (api == ImageFinder.SOGOU) {
            url = jsonImage.getString("pic_url");
            thumbUrl = jsonImage.getString("thumbUrl");
            width = jsonImage.getInt("width");
            height = jsonImage.getInt("height");
//            thumbWidth = jsonImage.getInt("thumb_width");
//            thumbHeight = jsonImage.getInt("thumb_height");
//            index = jsonImage.getInt("index");
            imageSize = jsonImage.getString("size");
//            imageType = jsonImage.getString("imgtype");
        } else if (api == ImageFinder.BAIDU) {
            if (jsonImage.has("replaceUrl")) {
                JSONArray replaceUrl = jsonImage.getJSONArray("replaceUrl");
                if (replaceUrl.length() == 2) {
                    url = replaceUrl.getJSONObject(1).getString("ObjURL");
                } else {
                    url = jsonImage.getString("thumbURL");
                }
            } else {
                url = jsonImage.getString("thumbURL");
            }
            thumbUrl = jsonImage.getString("thumbURL");
            width = jsonImage.getInt("width");
            height = jsonImage.getInt("height");
//            thumbWidth = jsonImage.getInt("width");
//            thumbHeight = jsonImage.getInt("height");
//            index = jsonImage.getInt("index");
            imageSize = jsonImage.getString("filesize");
//            imageType = jsonImage.getString("imgtype");
        } else if (api == ImageFinder.CHINASO) {
            url = jsonImage.getString("url");
            thumbUrl = jsonImage.getString("url");
            width = jsonImage.getInt("ImageWidth");
            height = jsonImage.getInt("ImageHeight");
//            thumbWidth = jsonImage.getInt("width");
//            thumbHeight = jsonImage.getInt("height");
//            index = jsonImage.getInt("index");
            imageSize = jsonImage.getString("image_content_length");
//            imageType = jsonImage.getString("imgtype");
        } else if (api == ImageFinder.BING) {
            url = jsonImage.getString("url");
            thumbUrl = jsonImage.getString("thumbUrl");
            width = jsonImage.getInt("width");
            height = jsonImage.getInt("height");
            imageSize = "";
        } else {
            url = jsonImage.getString("url");
            thumbUrl = jsonImage.getString("thumbUrl");
            width = jsonImage.getInt("width");
            height = jsonImage.getInt("height");
            imageSize = "";
        }
    }

    private QImage(Parcel in) {
//        id = in.readString();
//        title = in.readString();
        url = in.readString();
        thumbUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
//        thumbWidth = in.readInt();
//        thumbHeight = in.readInt();
//        index = in.readInt();
        imageSize = in.readString();
//        imageType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(id);
//        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(thumbUrl);
        parcel.writeInt(width);
        parcel.writeInt(height);
//        parcel.writeInt(thumbWidth);
//        parcel.writeInt(thumbHeight);
//        parcel.writeInt(index);
        parcel.writeString(imageSize);
//        parcel.writeString(imageType);
    }


//    public String getId() {
//        return id;
//    }

    public String getUrl() {
        return url;
    }

//    public String getTitle() {
//        return title;
//    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

//    public int getThumbWidth() {
//        return thumbWidth;
//    }
//
//    public int getThumbHeight() {
//        return thumbHeight;
//    }

//    public int getIndex() {
//        return index;
//    }

    public String getImageSize() {
        return imageSize;
    }

//    public String getImageType() {
//        return imageType;
//    }

    @SuppressWarnings({"PMD.SimplifyBooleanReturns", "RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QImage QImage = (QImage) o;

//        if (!id.equals(QImage.id)) {
//            return false;
//        }
        if (!url.equals(QImage.url)) {
            return false;
        }
//        if (!title.equals(QImage.title)) {
//            return false;
//        }
//        if (index != QImage.index) {
//            return false;
//        }
        if (width != QImage.width) {
            return false;
        }
        if (height != QImage.height) {
            return false;
        }
        if (!thumbUrl.equals(QImage.thumbUrl)) {
            return false;
        }
//        if (thumbWidth != QImage.thumbWidth) {
//            return false;
//        }
//        if (thumbHeight != QImage.thumbHeight) {
//            return false;
//        }
        if (imageSize != QImage.imageSize) {
            return false;
        }
//        if (imageType != QImage.imageType) {
//            return false;
//        }

        return true;
    }
}
