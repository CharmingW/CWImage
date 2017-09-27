package com.charmingwong.cwimage.imagesearch.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.charmingwong.cwimage.JsonRequestService;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.converter.BingConverterFactory;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(BingConverterFactory.create())
            .build();
        jsonRequestService = retrofit.create(JsonRequestService.class);
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
        if (index == 0) {
            call = jsonRequestService.getBingImages("images/async?q=" + query + "&first=0&count=40&qft=" + mCustomSize);
        } else {
            call = jsonRequestService.getBingImages(NEXT_URL);
        }
        call.enqueue(new Callback<List<QImage>>() {
            @Override
            public void onResponse(@NonNull Call<List<QImage>> call, @NonNull Response<List<QImage>> response) {
                List<QImage> QImages = response.body();
                if (response.isSuccessful() && QImages != null) {
                    mQueryListener.onSearchCompleted(requestCode, QImages);
                    mLastQueryResult = new QueryResult(QImages);
                    lastQuery = query + index;
                } else {
                    Log.i(TAG, "onResponse: responseCode = " + response.code());
                    Log.i(TAG, "onResponse: json null");
                    mQueryListener.onSearchFailed(requestCode, ERROR_JSON);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<QImage>> call, @NonNull Throwable t) {
                mQueryListener.onSearchFailed(requestCode, ERROR_NETWORK);
            }
        });
    }

    private static class QueryResult {

        private final List<QImage> mQImages;

        public QueryResult(List<QImage> results) {
            this.mQImages = results;
        }
    }
}
