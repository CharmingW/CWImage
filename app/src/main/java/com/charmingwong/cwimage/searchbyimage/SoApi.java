package com.charmingwong.cwimage.searchbyimage;

import android.support.annotation.NonNull;

import com.charmingwong.cwimage.JsonRequestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/23.
 */

public class SoApi {

    public static final int ERROR_JSON = 1;

    public static final int ERROR_NETWORK = 2;

    private static final String BASE_URL = "http://image.baidu.com/";

    private static final int RN = 40;

    private static JsonRequestService jsonRequestService;

    public static SoApi newInstance() {
        return new SoApi();
    }

    private SoApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SoJsonConverterFactory.create())
                .build();
        jsonRequestService = retrofit.create(JsonRequestService.class);
    }

    private QueryListener mQueryListener;

    public void registerSearchSuggestionsListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }


    public void search(int requestCode, String queryImageUrl, int index) {
        searchImages(requestCode, queryImageUrl, index);
    }

    private void searchImages(final int requestCode, final String queryImageUrl, final int index) {
        Call<List<SoImage>> call = jsonRequestService.getSoImages(queryImageUrl, index, RN);
        call.enqueue(new Callback<List<SoImage>>() {
            @Override
            public void onResponse(@NonNull Call<List<SoImage>> call, @NonNull Response<List<SoImage>> response) {
                List<SoImage> soImages = response.body();
                if (soImages != null) {
                    mQueryListener.onSearchCompleted(requestCode, soImages);
                } else {
                    mQueryListener.onSearchFailed(requestCode, ERROR_JSON);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SoImage>> call, @NonNull Throwable t) {
                t.printStackTrace();
                mQueryListener.onSearchFailed(requestCode, ERROR_NETWORK);
            }
        });
    }

    public interface QueryListener {

        void onSearchCompleted(int requestCode, List<SoImage> soImages);

        void onSearchFailed(int requestCode, int errorCode);
    }

}
