package com.charmingwong.cwimage.imagesearch.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.charmingwong.cwimage.JsonRequestService;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.converter.GoogleConverterFactory;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/1.
 */

public class GoogleApi implements BaseApi {

    private static final String TAG = "GoogleApi";

    public static final int COUNT_PER_PAGE_GOOGLE = 100;

    private static final String BASE_URL = "https://www.google.com/";

    private static final String HL = "zh-CN";

    private static final String ASEARCH = "ichunk";

    private static final String TBM = "isch";

    private String mCustonSize;

    private int ijn = 0;

    private static JsonRequestService jsonRequestService;

    public static GoogleApi newInstance() {
        return new GoogleApi();
    }

    private GoogleApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GoogleConverterFactory.create())
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
            mCustonSize = "isz:ex,iszw:1080,iszh:1920";
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
        call = jsonRequestService.getGoogleImage(
                HL,
                ASEARCH,
                TBM,
                query,
                index,
                ijn,
                mCustonSize
        );
        call.enqueue(new Callback<List<QImage>>() {
            @Override
            public void onResponse(@NonNull Call<List<QImage>> call, @NonNull Response<List<QImage>> response) {
                List<QImage> QImages = response.body();
                if (QImages != null) {
                    mQueryListener.onSearchCompleted(requestCode, QImages);
                    mLastQueryResult = new QueryResult(QImages);
                    lastQuery = query + index;
                    ijn++;
                } else {
                    Log.i(TAG, "onResponse: json null");
                    ijn = 0;
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
