package com.charmingwong.cwimage.base;

import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by CharmingWong on 2017/5/25.
 */

public abstract class BaseModel extends ArrayList<Parcelable> implements Parcelable {

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
