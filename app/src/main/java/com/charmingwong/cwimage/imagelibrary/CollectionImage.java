package com.charmingwong.cwimage.imagelibrary;

import android.os.Parcel;

import com.charmingwong.cwimage.base.BaseModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by CharmingWong on 2017/5/28.
 */

@Entity
public class CollectionImage extends BaseModel {


    public static final Creator<CollectionImage> CREATOR = new Creator<CollectionImage>() {
        @Override
        public CollectionImage createFromParcel(Parcel parcel) {
            return new CollectionImage(parcel);
        }

        @Override
        public CollectionImage[] newArray(int i) {
            return new CollectionImage[i];
        }
    };

    //id
    @Id(autoincrement = true)
    private Long id;

    //图片url
    @Unique
    private String url;

    //图片高度
    private Integer height;

    //图片宽度
    private Integer width;

    //下载日期
    private Date date;

    @Generated(hash = 642627025)
    public CollectionImage(Long id, String url, Integer height, Integer width,
            Date date) {
        this.id = id;
        this.url = url;
        this.height = height;
        this.width = width;
        this.date = date;
    }

    @Generated(hash = 1554607247)
    public CollectionImage() {
    }

    public CollectionImage(Parcel parcel) {
        url = parcel.readString();
        thumbUrl = parcel.readString();
        width = parcel.readInt();
        height = parcel.readInt();
        date = (Date) parcel.readSerializable();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(url);
        parcel.writeString(thumbUrl);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeSerializable(date);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
