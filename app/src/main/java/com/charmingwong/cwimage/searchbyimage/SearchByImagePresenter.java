package com.charmingwong.cwimage.searchbyimage;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/23.
 */

public class SearchByImagePresenter implements SearchByImageContract.Presenter {

    private static final int REQUEST_SEARCH = 1;

    private static final int REQUEST_APPEND = 2;

    private static final int REQUEST_REFRESH = 3;

    private static final String TAG = "SearchByImagePresenter";

    private final SearchByImageContract.View mView;

    private final PostApi mPostApi;

    private final SoApi mSoApi;

    private int mCurrentIndex = 0;

    private String mQueryImageUrl;

    private File mImage;

    public SearchByImagePresenter(@NonNull SearchByImageContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mPostApi = PostApi.newInstance();
        mPostApi.registerSearchSuggestionsListener(new PostApi.QueryListener() {
            @Override
            public void onSearchCompleted(String imageUrl) {
                searchSoImagesByUrl(imageUrl);
                mQueryImageUrl = imageUrl;
            }

            @Override
            public void onSearchFailed(Exception e) {
                e.printStackTrace();
                mView.showImagesFailed();
            }
        });

        mSoApi = SoApi.newInstance();
        mSoApi.registerSearchSuggestionsListener(new SoApi.QueryListener() {
            @Override
            public void onSearchCompleted(int requestCode, List<SoImage> soImages) {
                if (requestCode == REQUEST_SEARCH) {
                    mView.showSoImages(soImages);
                } else if (requestCode == REQUEST_APPEND) {
                    mView.showAppendedSoImages(soImages);
                } else {
                    mView.showRefreshedSoImages(soImages);
                }
                updateIndex();
            }

            @Override
            public void onSearchFailed(int requestCode, int errorCode) {
                if (errorCode == SoApi.ERROR_JSON) {
                    if (requestCode == REQUEST_REFRESH) {
                        resetIndex();
                        refreshSoImages();
                    }
                } else {
                    mView.showImagesFailed();
                }
            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void searchSoImagesByPost(File image) {
        mPostApi.postImage(image);
        mImage = image;
    }

    @Override
    public void appendSoImages() {
        if (mQueryImageUrl != null) {
            mSoApi.search(REQUEST_APPEND, mQueryImageUrl, mCurrentIndex);
        } else {
            mView.showImagesFailed();
        }
    }

    @Override
    public void refreshSoImages() {
        if (mQueryImageUrl != null) {
            mSoApi.search(REQUEST_REFRESH, mQueryImageUrl, mCurrentIndex);
        } else {
            mPostApi.postImage(mImage);
        }
    }

    @Override
    public void searchSoImagesByUrl(String url) {
        if (url != null) {
            mSoApi.search(REQUEST_SEARCH, url, mCurrentIndex);
            mQueryImageUrl = url;
        } else {
            mView.showImagesFailed();
        }
    }

    private void updateIndex() {
        mCurrentIndex += 40;
        Log.i(TAG, "updateIndex: " + mCurrentIndex);
    }

    private void resetIndex() {
        mCurrentIndex = 0;
    }

}
