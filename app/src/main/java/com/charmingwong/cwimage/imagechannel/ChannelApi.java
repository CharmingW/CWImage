package com.charmingwong.cwimage.imagechannel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.charmingwong.cwimage.JsonRequestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/15.
 */

public class ChannelApi {

    private static final String TAG = "ChannelApi";

    public static final int ERROR_JSON = 1;

    public static final int ERROR_NETWORK = 2;

    private static volatile ChannelApi api = null;

    private static final String BASE_URL = "http://image.so.com/";

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
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ChannelImageJsonConverterFactory.create())
            .build();
        jsonRequestService = retrofit.create(JsonRequestService.class);
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
        Call<List<ChannelImage>> call;
        if (channel.equals("food")) {
            call = jsonRequestService
                .getChannelImages(channel, index, IMAGE_PN, 293, tag, LIST_TYPE, TEMP);
        } else {
            call = jsonRequestService
                .getChannelImages(channel, index, IMAGE_PN, tag, 0, LIST_TYPE, TEMP);
        }

        call.enqueue(new Callback<List<ChannelImage>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChannelImage>> call,
                @NonNull Response<List<ChannelImage>> response) {
                Log.d(TAG, "onResponse: ");
                List<ChannelImage> images = response.body();
                if (response.isSuccessful() && images != null) {
                    queryListener.onSearchCompleted(requestCode, channel, images);
                } else {
                    Log.i(TAG, "onResponse: responseCode = " + response.code());
                    Log.i(TAG, "onResponse: 请求太频繁，后台返回的 json 的 list 为空");
                    queryListener.onSearchFailed(requestCode, ERROR_JSON, channel);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChannelImage>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ");
                queryListener.onSearchFailed(requestCode, ERROR_NETWORK, channel);
            }
        });
    }

    public interface QueryListener {

        void onSearchCompleted(int requestCode, String channel, List<ChannelImage> images);

        void onSearchFailed(int requestCode, int errorCode, String channel);
    }

}
