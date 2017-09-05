package com.charmingwong.cwimage.wallpaper;

import android.support.annotation.NonNull;

import com.charmingwong.cwimage.JsonRequestService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/7.
 */

public class WallpaperApi {

    private static final String TAG = "WallpaperApi";

    private static final String BASE_URL = "http://sj.zol.com.cn/";

    private static JsonRequestService jsonRequestService;

    public static WallpaperApi newInstance() {
        return new WallpaperApi();
    }

    private WallpaperApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(WallpaperConverterFactory.create())
                .build();
        jsonRequestService = retrofit.create(JsonRequestService.class);
    }

    public interface QueryListener {

        void onSearchCompleted(List<WallpaperCover> wallpaperCovers);

        void onSearchFailed();
    }


    private QueryListener mQueryListener;

    private QueryResult mLastQueryResult;

    public void registerSearchListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    private String lastQuery;

    public void search(String path) {
        if (mLastQueryResult != null && Objects.equals(lastQuery, path)) {
            mQueryListener.onSearchCompleted(mLastQueryResult.mWallpaperCovers);
        } else {
            searchImages(path);
        }
    }

    private void searchImages(final String path) {
        Call<List<WallpaperCover>> call = jsonRequestService.getWallPaperCover("bizhi" + path);
        call.enqueue(new Callback<List<WallpaperCover>>() {
            @Override
            public void onResponse(@NonNull Call<List<WallpaperCover>> call, @NonNull Response<List<WallpaperCover>> response) {
                List<WallpaperCover> wallpaperCovers = response.body();
                if (wallpaperCovers != null) {
                    mQueryListener.onSearchCompleted(wallpaperCovers);
                    mLastQueryResult = new QueryResult(wallpaperCovers);
                    lastQuery = path;
                } else {
                    mQueryListener.onSearchFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WallpaperCover>> call, @NonNull Throwable t) {
                mQueryListener.onSearchFailed();
            }
        });
    }

    private static class QueryResult {

        private final List<WallpaperCover> mWallpaperCovers;

        public QueryResult(List<WallpaperCover> results) {
            this.mWallpaperCovers = results;
        }
    }

}
