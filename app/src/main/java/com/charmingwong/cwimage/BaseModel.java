package com.charmingwong.cwimage;

import android.os.Parcelable;

/**
 * Created by CharmingWong on 2017/5/25.
 */

public abstract class BaseModel implements Parcelable {

    public String url;

    public String thumbUrl;

    private Integer height;

    private Integer width;

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getUrl() {
        return url;
    }
}
