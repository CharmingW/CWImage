package com.charmingwong.cwimage.imagesearch.api;

import com.charmingwong.cwimage.imagesearch.QImage;

import java.util.List;

/**
 * Created by CharmingWong on 2017/5/30.
 */

public interface BaseApi {

    int ERROR_JSON = 1;

    int ERROR_NETWORK = 2;

    void search(int requestCode, String query, int index);

    void registerSearchListener(QueryListener queryListener);

    interface QueryListener {

        void onSearchCompleted(int requestCode, List<QImage> QImages);

        void onSearchFailed(int requestCode, int errorCode);
    }

    void setSearchFlag(int searchFlag);

}
