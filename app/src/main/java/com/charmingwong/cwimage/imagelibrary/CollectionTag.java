package com.charmingwong.cwimage.imagelibrary;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by CharmingWong on 2017/5/28.
 */

@Entity
public class CollectionTag {

    //id
    @Id(autoincrement = true)
    private Long id;

    //收藏文本
    @Unique
    private String tag;

    //收藏日期
    private Date date;

    @Generated(hash = 802307606)
    public CollectionTag(Long id, String tag, Date date) {
        this.id = id;
        this.tag = tag;
        this.date = date;
    }

    @Generated(hash = 1137925087)
    public CollectionTag() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



}
