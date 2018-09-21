package com.charmingwong.cwimage.imagesearch.api;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.converter.GoogleConverterFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;

/**
 * Created by CharmingWong on 2017/6/1.
 */

public class GoogleApi implements BaseApi {

    private static final String TAG = "GoogleApi";
    private static final String HL = "zh-CN";
    private static final String ASEARCH = "ichunk";
    private static final String TBM = "isch";
    private static final String ASYNC = "_id:rg_s,_pms:s,_fmt:pc";
    public static final int COUNT_PER_PAGE_GOOGLE = 100;

    private String mCustomSize;

    private int ijn = 0;

    private static JsonRequestService jsonRequestService;

    public static GoogleApi newInstance() {
        return new GoogleApi();
    }

    private GoogleApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(GoogleConverterFactory.create());
    }

    private QueryListener mQueryListener;

    private QueryResult mLastQueryResult;

    public void registerSearchListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    @Override
    public void setSearchFlag(int searchFlag) {
        if (searchFlag == 1) {
            mCustomSize = "isz:ex,iszw:1080,iszh:1920";
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
        jsonRequestService.getGoogleImage(
                HL,
                ASEARCH,
                TBM,
                query,
                index,
                ijn,
            mCustomSize,
            ASYNC
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
                        ijn++;
                    } else {
                        Log.i(TAG, "onResponse: json null");
                        ijn = 0;
                        mQueryListener.onSearchFailed(requestCode, ERROR_JSON);
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
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
