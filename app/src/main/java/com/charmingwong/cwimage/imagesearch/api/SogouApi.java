package com.charmingwong.cwimage.imagesearch.api;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.converter.SogouConverterFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;

/**
 * Created by CharmingWong on 2017/5/30.
 */

public class SogouApi implements BaseApi {

    private static final String REQ_TYPE = "ajax";

    private static final String TAG = "SogouApi";

    private static final int DM = 4;

    private String mWidth;

    private String mHeight;

    private static JsonRequestService jsonRequestService;

    public static SogouApi newInstance() {
        return new SogouApi();
    }

    private SogouApi() {
        jsonRequestService
            = ApiManager
            .getInstance()
            .getJsonRequestService(SogouConverterFactory.create());
    }

    private QueryListener mQueryListener;

    private QueryResult mLastQueryResult;

    public void registerSearchListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    @Override
    public void setSearchFlag(int searchFlag) {
        if (searchFlag == 1) {
            mWidth = "1080";
            mHeight = "1920";
        }
    }

    private String lastQuery;

    public void search(int requestCode, String query, int index) {
        if (mLastQueryResult != null && Objects.equals(lastQuery, query + index)) {
            mQueryListener.onSearchCompleted(requestCode, mLastQueryResult.mQImages);
        } else {
            searchImages(requestCode, query, index);
        }
    }

    private void searchImages(final int requestCode, final String query, final int index) {
        Log.i(TAG, "searchImages: " + index);
        jsonRequestService.getSogouImages(
                query,
                REQ_TYPE,
                index,
                mWidth,
                mHeight,
                DM
        )
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<List<QImage>>() {
                @Override
                public void accept(List<QImage> qImages) {
                    if (qImages != null && !qImages.isEmpty()) {
                        mQueryListener.onSearchCompleted(requestCode, qImages);
                        mLastQueryResult = new QueryResult(qImages);
                        lastQuery = query + index;
                    } else {
                        Log.i(TAG, "onResponse: json null");
                        mQueryListener.onSearchFailed(requestCode, ERROR_JSON);
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    mQueryListener.onSearchFailed(requestCode, ERROR_NETWORK);
                }
            })
            .subscribe();
    }

    private static class QueryResult {
        private final List<QImage> mQImages;
        public QueryResult(List<QImage> results) {
            this.mQImages = results;
        }
    }

}
