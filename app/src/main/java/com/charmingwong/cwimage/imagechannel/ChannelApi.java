package com.charmingwong.cwimage.imagechannel;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/15.
 */

public class ChannelApi {

    private static final String TAG = "ChannelApi";

    public static final int ERROR_JSON = 1;

    public static final int ERROR_NETWORK = 2;

    private static volatile ChannelApi api = null;

    private static final String LIST_TYPE = "new";

    private static final int TEMP = 1;

    private static final int IMAGE_PN = 40;

    private JsonRequestService jsonRequestService;

    public static ChannelApi get() {
        if (api == null) {
            api = new ChannelApi();
        }
        return api;
    }

    private ChannelApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(ChannelImageJsonConverterFactory.create());
    }

    private QueryListener queryListener;

    public void registerSearchListener(ChannelApi.QueryListener queryListener) {
        this.queryListener = queryListener;
    }

    public void query(int requestCode, String channel, int tag, int index) {
        queryImages(requestCode, channel, tag, index);
    }

    private void queryImages(final int requestCode, final String channel, int tag,
        final int index) {
        Observable<List<ChannelImage>> observable;
        if (channel.equals("food")) {
            observable = jsonRequestService
                .getChannelImages(channel, index, IMAGE_PN, 293, tag, LIST_TYPE, TEMP);
        } else {
            observable = jsonRequestService
                .getChannelImages(channel, index, IMAGE_PN, tag, 0, LIST_TYPE, TEMP);
        }
        observable.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<List<ChannelImage>>() {
                @Override
                public void accept(List<ChannelImage> channelImages) {
                    if (channelImages != null && !channelImages.isEmpty()) {
                        Log.d(TAG, "queryImages succeed: " + channelImages);
                        queryListener.onSearchCompleted(requestCode, channel, channelImages);
                    } else {
                        Log.i(TAG, "onResponse: 请求太频繁，后台返回的 json 的 list 为空");
                        queryListener.onSearchFailed(requestCode, ERROR_JSON, channel);
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.d(TAG, "queryImages failed: ");
                    queryListener.onSearchFailed(requestCode, ERROR_NETWORK, channel);
                }
            })
            .subscribe();
    }

    public interface QueryListener {

        void onSearchCompleted(int requestCode, String channel, List<ChannelImage> images);

        void onSearchFailed(int requestCode, int errorCode, String channel);
    }

}
