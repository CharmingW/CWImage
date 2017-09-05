package com.charmingwong.cwimage.search;

import android.support.annotation.NonNull;

import com.charmingwong.cwimage.dao.CollectionTagDao;
import com.charmingwong.cwimage.dao.DaoSession;
import com.charmingwong.cwimage.dao.SearchRecordDao;
import com.charmingwong.cwimage.imagelibrary.CollectionTag;
import com.charmingwong.cwimage.search.api.HotSearchApi;
import com.charmingwong.cwimage.search.api.SuggestionsApi;
import com.charmingwong.cwimage.search.model.HotSearch;
import com.charmingwong.cwimage.search.model.SearchRecord;

import org.greenrobot.greendao.query.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/21.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private final SuggestionsApi mSuggestionsApi;

    private final HotSearchApi mHotSearchApi;

    private SearchContract.View mView;

    private DaoSession mDaoSession;

    public SearchPresenter(@NonNull SearchContract.View view, DaoSession daoSession) {
        mView = view;
        mSuggestionsApi = SuggestionsApi.newInstance();
        mHotSearchApi = HotSearchApi.newInstance();
        mView.setPresenter(this);
        mDaoSession = daoSession;

        SuggestionsApi.SearchSuggestionsListener listener = new SuggestionsApi.SearchSuggestionsListener() {
            @Override
            public void onSearchCompleted(List<ImageSearchSuggestion> suggestions) {
                mView.showSearchSuggestions(suggestions);
            }

            @Override
            public void onSearchFailed(Exception e) {
                e.printStackTrace();
                mView.showSuggestionsFailed();
            }
        };
        mSuggestionsApi.registerSearchSuggestionsListener(listener);

        HotSearchApi.HotSearchesListener hotSearchesListener = new HotSearchApi.HotSearchesListener() {
            @Override
            public void onSearchCompleted(List<HotSearch> hotSearches) {
                mView.showHotSearches(hotSearches);
            }

            @Override
            public void onSearchFailed(Exception e) {
                mView.showHotSearchesFailed();
            }
        };
        mHotSearchApi.registerHotSearchesListener(hotSearchesListener);
    }

    @Override
    public void start() {
    }

    @Override
    public void loadSearchSuggestions(String searched) {
        mSuggestionsApi.search(searched);
    }

    @Override
    public void saveSearchRecord(String query) {
        SearchRecordDao searchRecordDao = mDaoSession.getSearchRecordDao();
        searchRecordDao.insertOrReplace(new SearchRecord(null, query, new Date()));
    }

    @Override
    public void loadSearchRecord() {
        SearchRecordDao searchRecordDao = mDaoSession.getSearchRecordDao();
        Query<SearchRecord> query =  searchRecordDao.queryBuilder().limit(10).orderDesc(SearchRecordDao.Properties.Date).build();
        List<SearchRecord> searchRecords = query.list();
        Collections.reverse(searchRecords);
        mView.showSearchRecord(searchRecords);
    }

    @Override
    public void loadHotSearches() {
        mHotSearchApi.getHotSearches();
    }

    @Override
    public void loadTags() {
        CollectionTagDao collectionTagDao = mDaoSession.getCollectionTagDao();
        Query<CollectionTag> query = collectionTagDao.queryBuilder().orderDesc(CollectionTagDao.Properties.Date).build();
        mView.showTags(query.list());
    }
}
