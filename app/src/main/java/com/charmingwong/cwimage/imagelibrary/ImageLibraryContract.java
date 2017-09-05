package com.charmingwong.cwimage.imagelibrary;

import com.charmingwong.cwimage.BaseModel;
import com.charmingwong.cwimage.BasePresenter;
import com.charmingwong.cwimage.BaseView;

import java.util.List;

/**
 * Created by CharmingWong on 2017/5/27.
 */

public interface ImageLibraryContract {

    interface View extends BaseView<Presenter> {

        void getInitialData(List<BaseModel> l1, List<BaseModel> l2, List<CollectionTag> l3);

        void showDownloads(List<BaseModel> imageModels);

        void showCollection(List<BaseModel> imageModels);

        void showCollectedTags(List<CollectionTag> tags);
    }

    interface Presenter extends BasePresenter {

        void loadDownloadImage();

        void loadCollectionImage();

        void loadCollectionTags();

        void deleteDownloadImages(List<Integer> indexes);

        void deleteCollectionImages(List<Integer> indexes);

        void deleteCollectionTags(List<Integer> indexes);


    }
}
