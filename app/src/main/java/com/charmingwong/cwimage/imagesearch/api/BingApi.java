package com.charmingwong.cwimage.imagesearch.api;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.converter.BingConverterFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;

/**
 * Created by CharmingWong on 2017/5/31.
 */

public class BingApi implements BaseApi {

    private static final String TAG = "BingApi";

    private static final String BASE_URL = "http://cn.bing.com/";

    public static String NEXT_URL;

    private String mCustomSize = "";

    private static JsonRequestService jsonRequestService;

    public static BingApi newInstance() {
        return new BingApi();
    }

    private BingApi() {
        jsonRequestService = ApiManager.getInstance()
            .getJsonRequestService(BingConverterFactory.create());
    }

    private QueryListener mQueryListener;

    private QueryResult mLastQueryResult;

    public void registerSearchListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    @Override
    public void setSearchFlag(int searchFlag) {
        if (searchFlag == 1) {
            mCustomSize = "filterui:imagesize-custom_1080_1920";
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
        Call<List<QImage>> call;
        String url;
        if (index == 0) {
            url = BASE_URL + "images/async?q=" + query + "&first=0&count=40&qft=" + mCustomSize;
        } else {
            url = BASE_URL + NEXT_URL;
        }
        jsonRequestService.getBingImages(url)
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
