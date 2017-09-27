package com.charmingwong.cwimage.search.api;

import android.support.annotation.NonNull;
import android.util.Log;
import com.charmingwong.cwimage.JsonRequestService;
import com.charmingwong.cwimage.search.converter.HotSearchesConverterFactory;
import com.charmingwong.cwimage.search.model.HotSearch;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/2.
 */

public class HotSearchApi {

    private static final String BASE_URL = "http://image.baidu.com/";
    private static final String TAG = "HotSearchApi";

    private static JsonRequestService jsonRequestService;

    public static HotSearchApi newInstance() {
        return new HotSearchApi();
    }

    private HotSearchApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(HotSearchesConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        jsonRequestService = retrofit.create(JsonRequestService.class);
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
        Call<List<HotSearch>> call = jsonRequestService.getHotSearches();
        call.enqueue(new Callback<List<HotSearch>>() {
            @Override
            public void onResponse(@NonNull Call<List<HotSearch>> call, @NonNull Response<List<HotSearch>> response) {
                List<HotSearch> hotSearches = response.body();
                if (response.isSuccessful() && hotSearches != null) {
                    mHotSearchesListener.onSearchCompleted(hotSearches);
                    mLastSearchResult = new SearchResult(hotSearches);
                } else {
                    Log.i(TAG, "onResponse: responseCode = " + response.code());
                    mHotSearchesListener.onSearchFailed(new Exception("无法正确获取热门搜索数据"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HotSearch>> call, @NonNull Throwable t) {
                mHotSearchesListener.onSearchFailed(new Exception(t));
            }
        });
    }

    public interface HotSearchesListener {

        void onSearchCompleted(List<HotSearch> hotSearches);

        void onSearchFailed(Exception e);
    }

    private static class SearchResult {
        private final List<HotSearch> mHotSearches;

        public SearchResult(List<HotSearch> results) {
            this.mHotSearches = results;
        }
    }
}
