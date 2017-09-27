package com.charmingwong.cwimage.searchbyimage;

import com.charmingwong.cwimage.base.BasePresenter;
import com.charmingwong.cwimage.base.BaseView;

import java.io.File;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/12.
 */

public interface SearchByImageContract {

    interface View extends BaseView<Presenter> {

        void showSoImages(List<SoImage> soImages);

        void showAppendedSoImages(List<SoImage> images);

        void showRefreshedSoImages(List<SoImage> images);

        void showImagesFailed();
    }

    interface Presenter extends BasePresenter {

        void searchSoImagesByPost(File image);

        void appendSoImages();

        void refreshSoImages();

        void searchSoImagesByUrl(String url);
    }
}
