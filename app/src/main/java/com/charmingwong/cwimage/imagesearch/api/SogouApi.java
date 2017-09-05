package com.charmingwong.cwimage.imagesearch.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.charmingwong.cwimage.JsonRequestService;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.converter.SogouConverterFactory;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/30.
 */

public class SogouApi implements BaseApi {


    private static final String BASE_URL = "http://pic.sogou.com/";

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SogouConverterFactory.create())
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
        Call<List<QImage>> call = jsonRequestService.getSogouImages(
                query,
                REQ_TYPE,
                index,
                mWidth,
                mHeight,
                DM
        );

//        query:(unable to decode value)
//        mode:1
//        dm:4
//        leftp:44230501
//        cwidth:1080
//        cheight:1920
//        st:0
//        start:144
//        reqType:ajax
//        reqFrom:result
//        tn:0
        call.enqueue(new Callback<List<QImage>>() {
            @Override
            public void onResponse(@NonNull Call<List<QImage>> call, @NonNull Response<List<QImage>> response) {
                List<QImage> QImages = response.body();
                if (QImages != null) {
                    mQueryListener.onSearchCompleted(requestCode, QImages);
                    mLastQueryResult = new QueryResult(QImages);
                    lastQuery = query + index;
                } else {
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
