package com.charmingwong.cwimage.wallpaperdetails;

import android.support.annotation.NonNull;
import android.util.Log;
import com.charmingwong.cwimage.JsonRequestService;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/8.
 */

public class WallpaperDetailsApi {

    private static final String TAG = "WallpaperApi";

    private static final String BASE_PHONE_URL = "http://sj.zol.com.cn/";

    private static final String BASE_DESKTOP_URL = "http://desk.zol.com.cn/";

    private static JsonRequestService jsonRequestService;

    public static WallpaperDetailsApi newInstance(int type) {
        return new WallpaperDetailsApi(type);
    }

    private WallpaperDetailsApi(int type) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(WallpaperDetailsConverterFactory.create());

        if (type == 0) {
            builder.baseUrl(BASE_PHONE_URL);
        } else {
            builder.baseUrl(BASE_DESKTOP_URL);
        }
        Retrofit retrofit = builder.build();
        jsonRequestService = retrofit.create(JsonRequestService.class);
    }

    public interface QueryListener {

        void onSearchCompleted(int position, Wallpaper wallpaper);

        void onSearchFailed();
    }

    private QueryListener mQueryListener;

    private QueryResult mLastQueryResult;

    public void registerSearchListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    private String lastQuery;

    public void search(int position, String url) {
        if (mLastQueryResult != null && Objects.equals(lastQuery, url)) {
            mQueryListener.onSearchCompleted(position, mLastQueryResult.mWallpaper);
        } else {
            searchImages(position, url);
        }
    }

    private void searchImages(final int position, final String url) {
        Call<Wallpaper> call = jsonRequestService.getWallPaper(url);
        call.enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(@NonNull Call<Wallpaper> call, @NonNull Response<Wallpaper> response) {
                Wallpaper wallpaper = response.body();
                if (response.isSuccessful() && wallpaper != null) {
                    mQueryListener.onSearchCompleted(position, wallpaper);
                    mLastQueryResult = new QueryResult(wallpaper);
                    lastQuery = url;
                } else {
                    Log.i(TAG, "onResponse: responseCode = " + response.code());
                    mQueryListener.onSearchFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Wallpaper> call, @NonNull Throwable t) {
                mQueryListener.onSearchFailed();
            }
        });
    }

    private static class QueryResult {

        private final Wallpaper mWallpaper;

        public QueryResult(Wallpaper wallpaper) {
            this.mWallpaper = wallpaper;
        }
    }

}
