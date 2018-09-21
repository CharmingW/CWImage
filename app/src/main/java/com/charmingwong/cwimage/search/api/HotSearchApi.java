package com.charmingwong.cwimage.search.api;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import com.charmingwong.cwimage.search.converter.HotSearchesConverterFactory;
import com.charmingwong.cwimage.search.model.HotSearch;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by CharmingWong on 2017/6/2.
 */

public class HotSearchApi {

    private static final String TAG = "HotSearchApi";

    private static JsonRequestService jsonRequestService;

    public static HotSearchApi newInstance() {
        return new HotSearchApi();
    }

    private HotSearchApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(HotSearchesConverterFactory.create());
    }

    private HotSearchesListener mHotSearchesListener;

    private SearchResult mLastSearchResult;

    public void registerHotSearchesListener(HotSearchesListener hotSearchesListener) {
        this.mHotSearchesListener = hotSearchesListener;
    }

    public void getHotSearches() {
        if (mLastSearchResult != null) {
            mHotSearchesListener.onSearchCompleted(mLastSearchResult.mHotSearches);
        } else {
            get();
        }
    }

    private void get() {
        jsonRequestService.getHotSearches()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<List<HotSearch>>() {
                @Override
                public void accept(List<HotSearch> hotSearches) {
                    if (hotSearches != null && !hotSearches.isEmpty()) {
                        Log.d(TAG, "get hot search succeed: " + hotSearches);
                        mHotSearchesListener.onSearchCompleted(hotSearches);
                        mLastSearchResult = new SearchResult(hotSearches);
                    } else {
                        Log.d(TAG, "get hot search failed:");
                        mHotSearchesListener.onSearchFailed(new Exception("无法正确获取热门搜索数据"));
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    mHotSearchesListener.onSearchFailed(new Exception(throwable));
                }
            })
            .subscribe();
    }

    public interface HotSearchesListener {

        void onSearchCompleted(List<HotSearch> hotSearches);

        void onSearchFailed(Exception e);
    }

    private static class SearchResult {
        private final List<HotSearch> mHotSearches;

        SearchResult(List<HotSearch> results) {
            this.mHotSearches = results;
        }
    }
}
