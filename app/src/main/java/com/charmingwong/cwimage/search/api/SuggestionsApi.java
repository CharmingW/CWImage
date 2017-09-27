package com.charmingwong.cwimage.search.api;

import android.support.annotation.NonNull;
import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import com.charmingwong.cwimage.search.ImageSearchSuggestion;
import com.charmingwong.cwimage.search.converter.SuggestionsJsonConverterFactory;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CharmingWong on 2017/5/21.
 */

public class SuggestionsApi {

    //http://sug.image.so.com/suggest/word?callback=suggest_so&encodein=utf-8&encodeout=utf-8&word=fds

    private static final String BASE_URL = "http://sug.image.so.com/";

    private static final String CALLBACK = "";

    private static final String ENCODE_IN = "utf-8";

    private static final String ENCODE_OUT = "utf-8";
    private static final String TAG = "SuggestionsApi";

    private static JsonRequestService jsonRequestService;

    public static SuggestionsApi newInstance() {
        return new SuggestionsApi();
    }

    private SuggestionsApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(SuggestionsJsonConverterFactory.create());
    }

    private SearchSuggestionsListener mSearchSuggestionsListener;

    private SearchResult mLastSearchResult;

    public void registerSearchSuggestionsListener(SearchSuggestionsListener searchSuggestionsListener) {
        this.mSearchSuggestionsListener = searchSuggestionsListener;
    }

    private String lastQuery;

    public void search(String query) {
        if (mLastSearchResult != null && Objects.equals(lastQuery, query)) {
            mSearchSuggestionsListener.onSearchCompleted(mLastSearchResult.images);
        } else {
            searchImages(query);
        }
    }

    private void searchImages(final String query) {
        Call<List<ImageSearchSuggestion>> call = jsonRequestService.getSearchSuggestions(CALLBACK, ENCODE_IN, ENCODE_OUT, query);
        call.enqueue(new Callback<List<ImageSearchSuggestion>>() {
            @Override
            public void onResponse(@NonNull Call<List<ImageSearchSuggestion>> call, @NonNull Response<List<ImageSearchSuggestion>> response) {
                List<ImageSearchSuggestion> suggestions = response.body();
                if (response.isSuccessful() && suggestions != null) {
                    mSearchSuggestionsListener.onSearchCompleted(suggestions);
                    mLastSearchResult = new SearchResult(suggestions);
                    lastQuery = query;
                } else {
                    Log.i(TAG, "onResponse: responseCode = " + response.code());
                    mSearchSuggestionsListener.onSearchFailed(new Exception("请求太频繁，后台返回的 json 为空"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ImageSearchSuggestion>> call, @NonNull Throwable t) {
                mSearchSuggestionsListener.onSearchFailed(new Exception(t));
            }
        });
    }

    public interface SearchSuggestionsListener {

        void onSearchCompleted(List<ImageSearchSuggestion> suggestions);

        void onSearchFailed(Exception e);
    }

    private static class SearchResult {
        private final List<ImageSearchSuggestion> images;

        public SearchResult(List<ImageSearchSuggestion> results) {
            this.images = results;
        }
    }
}
