package com.charmingwong.cwimage.imagesearch;

import com.charmingwong.cwimage.base.BasePresenter;
import com.charmingwong.cwimage.base.BaseView;

import java.util.List;

/**
 * Created by CharmingWong on 2017/5/12.
 */

public interface ImageSearchContract {

    interface View extends BaseView<Presenter> {

        void showSearchedImages(List<QImage> QImages);

        void showAppendedImages(List<QImage> QImages);

        void showRefreshedImages(List<QImage> QImages);

        void showImagesFailed();

        void showCollectingResult(boolean isSuccessful);
    }

    interface Presenter extends BasePresenter {

        void searchImages(String searched);

        void appendImages(String searched);

        void refreshImages(String refreshed);

        void collect(String query);

    }
}
