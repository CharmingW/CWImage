package com.charmingwong.cwimage.wallpaperdetails;

import android.support.annotation.NonNull;
import android.util.Log;
import com.charmingwong.cwimage.JsonRequestService;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/9.
 */

public class WallpaperDetailsStartApi {

    private static final String TAG = "WallpaperDetailsStartApi";

    private static final String BASE_PHONE_URL = "http://sj.zol.com.cn/";

    private static final String BASE_DESKTOP_URL = "http://desk.zol.com.cn/";

    private static JsonRequestService jsonRequestService;


    public static WallpaperDetailsStartApi newInstance(String size, int type) {
        return new WallpaperDetailsStartApi(size, type);
    }

    private WallpaperDetailsStartApi(String size, int type) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(WallpaperDetailsStartConverterFactory.create(size));

        if (type == 0) {
            builder.baseUrl(BASE_PHONE_URL);
        } else {
            builder.baseUrl(BASE_DESKTOP_URL);
        }
        Retrofit retrofit = builder.build();
        jsonRequestService = retrofit.create(JsonRequestService.class);
    }

    public interface QueryListener {

        void onSearchCompleted(List<String> urls);

        void onSearchFailed();
    }

    private QueryListener mQueryListener;

    private QueryResult mLastQueryResult;

    public void registerSearchListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    private String lastQuery;

    public void search(String url) {
        if (mLastQueryResult != null && Objects.equals(lastQuery, url)) {
            mQueryListener.onSearchCompleted(mLastQueryResult.urls);
        } else {
            searchImages(url);
        }
    }

    private void searchImages(final String url) {
        Call<List<String>> call = jsonRequestService.getWallpaperStart(url);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                List<String> urls = response.body();
                if (response.isSuccessful() && urls != null) {
                    mQueryListener.onSearchCompleted(urls);
                    mLastQueryResult = new QueryResult(urls);
                    lastQuery = url;
                } else {
                    Log.i(TAG, "onResponse: responseCode = " + response.code());
                    mQueryListener.onSearchFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                mQueryListener.onSearchFailed();
            }
        });
    }

    private static class QueryResult {

        private final List<String> urls;

        public QueryResult(List<String> urls) {
            this.urls = urls;
        }
    }
}
