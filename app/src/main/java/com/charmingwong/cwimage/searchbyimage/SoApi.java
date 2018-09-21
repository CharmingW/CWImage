package com.charmingwong.cwimage.searchbyimage;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/23.
 */

public class SoApi {

    public static final int ERROR_JSON = 1;

    public static final int ERROR_NETWORK = 2;

    private static final String BASE_URL = "http://image.baidu.com/";

    private static final int RN = 40;
    private static final String TAG = "SoApi";

    private static JsonRequestService jsonRequestService;

    public static SoApi newInstance() {
        return new SoApi();
    }

    private SoApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(SoJsonConverterFactory.create());
    }

    private QueryListener mQueryListener;

    public void registerSearchSuggestionsListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }


    public void search(int requestCode, String queryImageUrl, int index) {
        searchImages(requestCode, queryImageUrl, index);
    }

    private void searchImages(final int requestCode, final String queryImageUrl, final int index) {
        jsonRequestService.getSoImages(queryImageUrl, index, RN)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<List<SoImage>>() {
                @Override
                public void accept(List<SoImage> soImages) {
                    if (soImages != null && !soImages.isEmpty()) {
                        mQueryListener.onSearchCompleted(requestCode, soImages);
                    } else {
                        mQueryListener.onSearchFailed(requestCode, ERROR_JSON);
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    mQueryListener.onSearchFailed(requestCode, ERROR_NETWORK);
                    Log.e(TAG, "search images failed: ", throwable);
                }
            })
            .subscribe();
    }

    public interface QueryListener {

        void onSearchCompleted(int requestCode, List<SoImage> soImages);

        void onSearchFailed(int requestCode, int errorCode);
    }

}
