package com.charmingwong.cwimage.wallpaper;

import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;

/**
 * Created by CharmingWong on 2017/6/15.
 */

public class DesktopApi {

    private static final String TAG = "DesktopApi";

    private static final String BASE_URL = "http://desk.zol.com.cn/";

    private static JsonRequestService jsonRequestService;

    public static DesktopApi newInstance() {
        return new DesktopApi();
    }

    private DesktopApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(WallpaperConverterFactory.create());
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
        jsonRequestService.getWallPaperCover(BASE_URL + "pc" + path)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<List<WallpaperCover>>() {
                @Override
                public void accept(List<WallpaperCover> wallpaperCovers) {
                    if (wallpaperCovers != null) {
                        mQueryListener.onSearchCompleted(wallpaperCovers);
                        mLastQueryResult = new QueryResult(wallpaperCovers);
                        lastQuery = path;
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

        private final List<WallpaperCover> mWallpaperCovers;

        public QueryResult(List<WallpaperCover> results) {
            this.mWallpaperCovers = results;
        }
    }

}
