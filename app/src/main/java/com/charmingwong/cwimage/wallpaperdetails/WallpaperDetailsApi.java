package com.charmingwong.cwimage.wallpaperdetails;

import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.Objects;

/**
 * Created by CharmingWong on 2017/6/8.
 */

public class WallpaperDetailsApi {

    private static final String TAG = "WallpaperApi";

    private static final String BASE_PHONE_URL = "http://sj.zol.com.cn/";

    private static final String BASE_DESKTOP_URL = "http://desk.zol.com.cn/";

    private static JsonRequestService jsonRequestService;

    private int mType;

    public static WallpaperDetailsApi newInstance(int type) {
        return new WallpaperDetailsApi(type);
    }

    private WallpaperDetailsApi(int type) {
        mType = type;
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(WallpaperDetailsConverterFactory.create());
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
        String baseUrl;
        if (mType == 0) {
            baseUrl = BASE_PHONE_URL;
        } else {
            baseUrl = BASE_DESKTOP_URL;
        }
        jsonRequestService.getWallPaper(baseUrl + url)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<Wallpaper>() {
                @Override
                public void accept(Wallpaper wallpaper) {
                    if (wallpaper != null) {
                        mQueryListener.onSearchCompleted(position, wallpaper);
                        mLastQueryResult = new QueryResult(wallpaper);
                        lastQuery = url;
                    } else {
                        mQueryListener.onSearchFailed();
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    mQueryListener.onSearchFailed();
                }
            })
            .subscribe();
    }

    private static class QueryResult {

        private final Wallpaper mWallpaper;

        public QueryResult(Wallpaper wallpaper) {
            this.mWallpaper = wallpaper;
        }
    }

}
