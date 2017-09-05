package com.charmingwong.cwimage.search.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by CharmingWong on 2017/6/1.
 */

@Entity
public class SearchRecord {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String query;

    private Date date;

    @Generated(hash = 1267500706)
    public SearchRecord(Long id, String query, Date date) {
        this.id = id;
        this.query = query;
        this.date = date;
    }

    @Generated(hash = 839789598)
    public SearchRecord() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
