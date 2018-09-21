package com.charmingwong.cwimage.imagesearch;

import android.support.annotation.NonNull;
import android.util.Log;
import com.charmingwong.cwimage.dao.CollectionTagDao;
import com.charmingwong.cwimage.dao.DaoSession;
import com.charmingwong.cwimage.imagelibrary.CollectionTag;
import com.charmingwong.cwimage.imagesearch.api.BaseApi;
import com.charmingwong.cwimage.imagesearch.api.GoogleApi;
import com.charmingwong.cwimage.imagesearch.api.QHApi;
import java.util.Date;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/12.
 */

public class ImageSearchPresenter implements ImageSearchContract.Presenter {

    public static final int COUNT_PER_PAGE = 48;

    private static final int REQUEST_SEARCH = 1;

    private static final int REQUEST_APPEND = 2;

    private static final int REQUEST_REFRESH = 3;

    private static final String TAG = "ImageSearchPresenter";

    private final ImageSearchContract.View mView;

    private final BaseApi mImageApi;

    private String mCurrentQuery;

    private DaoSession mDaoSession;

    private int index = 0;

    public ImageSearchPresenter(@NonNull ImageSearchContract.View view, BaseApi api, DaoSession daoSession) {
        mView = view;
        mView.setPresenter(this);
        mImageApi = api;
        mImageApi.registerSearchListener(mQueryListener);
        mDaoSession = daoSession;
    }

    QHApi.QueryListener mQueryListener = new QHApi.QueryListener() {
        @Override
        public void onSearchCompleted(int requestCode, List<QImage> QImages) {
            if (requestCode == REQUEST_SEARCH) {
                mView.showSearchedImages(QImages);
            } else if (requestCode == REQUEST_APPEND) {
                mView.showAppendedImages(QImages);
            } else {
                mView.showRefreshedImages(QImages);
            }
            if (mImageApi instanceof GoogleApi) {
                index += GoogleApi.COUNT_PER_PAGE_GOOGLE;
            } else {
                index += COUNT_PER_PAGE;
            }
            Log.i(TAG, "onSearchCompleted: " + index);
        }

        @Override
        public void onSearchFailed(int requestCode, int errorCode) {
            if (errorCode == BaseApi.ERROR_JSON) {
                if (requestCode == REQUEST_REFRESH) {
                    index = 0;
                    refreshImages(mCurrentQuery);
                }
            } else {
                mView.showImagesFailed();
            }
        }
    };

    @Override
    public void start() {
    }

    @Override
    public void searchImages(String query) {
        index = 0;
        mImageApi.search(REQUEST_SEARCH, query, index);
        mCurrentQuery = query;
    }

    @Override
    public void appendImages(String query) {
        mImageApi.search(REQUEST_APPEND, query, index);
    }

    @Override
    public void refreshImages(String query) {
        mImageApi.search(REQUEST_REFRESH, query, index);
    }

    @Override
    public void collect(String query) {
        CollectionTagDao collectionTagDao = mDaoSession.getCollectionTagDao();
        long row = collectionTagDao.insertOrReplace(new CollectionTag(null, query, new Date()));
        mView.showCollectingResult(row >= 0);
    }
}
