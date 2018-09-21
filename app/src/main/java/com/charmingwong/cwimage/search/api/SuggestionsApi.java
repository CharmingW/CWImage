package com.charmingwong.cwimage.search.api;

import android.util.Log;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import com.charmingwong.cwimage.search.ImageSearchSuggestion;
import com.charmingwong.cwimage.search.converter.SuggestionsJsonConverterFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;

/**
 * Created by CharmingWong on 2017/5/21.
 */

public class SuggestionsApi {

    private static final String CALLBACK = "";
    private static final String ENCODE_IN = "utf-8";
    private static final String ENCODE_OUT = "utf-8";
    private static final String TAG = "SuggestionsApi";

    private static JsonRequestService jsonRequestService;

    public static SuggestionsApi newInstance() {
        return new SuggestionsApi();
    }

    private SuggestionsApi() {
        jsonRequestService = ApiManager.getInstance()
            .getJsonRequestService(SuggestionsJsonConverterFactory.create());
    }

    private SearchSuggestionsListener mSearchSuggestionsListener;

    private SearchResult mLastSearchResult;

    public void registerSearchSuggestionsListener(
        SearchSuggestionsListener searchSuggestionsListener) {
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
        jsonRequestService.getSearchSuggestions(CALLBACK, ENCODE_IN, ENCODE_OUT, query)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<List<ImageSearchSuggestion>>() {
                @Override
                public void accept(List<ImageSearchSuggestion> suggestions) {
                    if (suggestions != null && !suggestions.isEmpty()) {
                        Log.d(TAG, "get search suggestion succeed: " + suggestions);
                        mSearchSuggestionsListener.onSearchCompleted(suggestions);
                        mLastSearchResult = new SearchResult(suggestions);
                        lastQuery = query;
                    } else {
                        mSearchSuggestionsListener
                            .onSearchFailed(new Exception("请求太频繁，后台返回的 json 为空"));
                    }
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    Log.d(TAG, "get search suggestion failed: ");
                    mSearchSuggestionsListener.onSearchFailed(new Exception(throwable));
                }
            })
            .subscribe();
    }

    public interface SearchSuggestionsListener {

        void onSearchCompleted(List<ImageSearchSuggestion> suggestions);

        void onSearchFailed(Exception e);
    }

    private static class SearchResult {

        private final List<ImageSearchSuggestion> images;

        SearchResult(List<ImageSearchSuggestion> results) {
            this.images = results;
        }
    }
}
