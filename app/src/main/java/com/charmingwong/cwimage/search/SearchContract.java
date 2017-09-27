package com.charmingwong.cwimage.search;

import com.charmingwong.cwimage.base.BasePresenter;
import com.charmingwong.cwimage.base.BaseView;
import com.charmingwong.cwimage.imagelibrary.CollectionTag;
import com.charmingwong.cwimage.search.model.HotSearch;
import com.charmingwong.cwimage.search.model.SearchRecord;

import java.util.List;

/**
 * Created by CharmingWong on 2017/5/21.
 */

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showSearchSuggestions(List<ImageSearchSuggestion> suggestions);

        void showSuggestionsFailed();

        void showSearchRecord(List<SearchRecord> records);

        void showHotSearches(List<HotSearch> hotSearches);

        void showHotSearchesFailed();

        void showTags(List<CollectionTag> collectionTags);

    }

    interface Presenter extends BasePresenter {

        void loadSearchSuggestions(String searched);

        void saveSearchRecord(String query);

        void loadSearchRecord();

        void loadHotSearches();

        void loadTags();
    }
}
