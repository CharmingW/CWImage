package com.charmingwong.cwimage.imagelibrary;

import com.charmingwong.cwimage.base.BaseModel;
import com.charmingwong.cwimage.base.BasePresenter;
import com.charmingwong.cwimage.base.BaseView;

import java.util.List;

/**
 * Created by CharmingWong on 2017/5/27.
 */

public interface ImageLibraryContract {

    interface View extends BaseView<Presenter> {

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
