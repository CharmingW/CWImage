package com.charmingwong.cwimage.searchbyimage;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by CharmingWong on 2017/5/23.
 */

public class PostApi {

    private static final String TAG = "PostApi";

    private static final String FROM = "html5";

    private static final boolean NEED_JSON = true;

    private static JsonRequestService jsonRequestService;

    public static PostApi newInstance() {
        return new PostApi();
    }

    private PostApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(PostJsonConverterFactory.create());
    }

    private QueryListener mQueryListener;

    public void registerSearchSuggestionsListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    public void postImage(File imageFile) {
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), imageFile);
        jsonRequestService.postImage(image, FROM, NEED_JSON)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<String>() {
                @Override
                public void accept(String url) {
                    Log.d(TAG, "postImage succeed: " + url);
                    mQueryListener.onSearchCompleted(url);
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    mQueryListener.onSearchFailed(new Exception(throwable));
                }
            })
            .subscribe();
    }

    public interface QueryListener {

        void onSearchCompleted(String imageUrl);

        void onSearchFailed(Exception e);
    }

}
