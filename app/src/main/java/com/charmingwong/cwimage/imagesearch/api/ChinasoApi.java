package com.charmingwong.cwimage.imagesearch.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.charmingwong.cwimage.JsonRequestService;
import com.charmingwong.cwimage.imagesearch.ImageSearchPresenter;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.converter.ChinasoConverterFactory;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/31.
 */

public class ChinasoApi implements BaseApi {

    private static final String TAG = "ChinasoApi";

        private static final String BASE_URL = "http://image.chinaso.com/";

        private static JsonRequestService jsonRequestService;

    public static ChinasoApi newInstance() {
        return new ChinasoApi();
    }

    private ChinasoApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ChinasoConverterFactory.create())
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
        Call<List<QImage>> call = jsonRequestService.getChinasoImages(
                query,
                index,
                ImageSearchPresenter.COUNT_PER_PAGE
        );
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
