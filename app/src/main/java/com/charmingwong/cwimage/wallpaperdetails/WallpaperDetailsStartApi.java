package com.charmingwong.cwimage.wallpaperdetails;

import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;

/**
 * Created by CharmingWong on 2017/6/9.
 */

public class WallpaperDetailsStartApi {

    private static final String TAG = "WallpaperDetailsStartApi";

    private static final String BASE_PHONE_URL = "http://sj.zol.com.cn/";

    private static final String BASE_DESKTOP_URL = "http://desk.zol.com.cn/";

    private static JsonRequestService jsonRequestService;

    private int mType;

    public static String size;

    public static WallpaperDetailsStartApi newInstance(String size, int type) {
        return new WallpaperDetailsStartApi(size, type);
    }

    private WallpaperDetailsStartApi(String size, int type) {
        mType = type;
        WallpaperDetailsStartApi.size = size;
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(WallpaperDetailsStartConverterFactory.create());
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
        String baseUrl;
        if (mType == 0) {
            baseUrl = BASE_PHONE_URL;
        } else {
            baseUrl = BASE_DESKTOP_URL;
        }
        jsonRequestService.getWallpaperStart(baseUrl + url)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<List<String>>() {
                @Override
                public void accept(List<String> urls) {
                    if (urls != null) {
                        mQueryListener.onSearchCompleted(urls);
                        mLastQueryResult = new QueryResult(urls);
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

        private final List<String> urls;

        public QueryResult(List<String> urls) {
            this.urls = urls;
        }
    }
}
