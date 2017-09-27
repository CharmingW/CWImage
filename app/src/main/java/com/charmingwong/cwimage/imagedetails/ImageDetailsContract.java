package com.charmingwong.cwimage.imagedetails;

import android.net.Uri;

import com.charmingwong.cwimage.base.BaseModel;
import com.charmingwong.cwimage.base.BasePresenter;
import com.charmingwong.cwimage.base.BaseView;

import java.io.File;

/**
 * Created by CharmingWong on 2017/5/13.
 */

public interface ImageDetailsContract {

    interface View extends BaseView<Presenter> {

        void showDownloadResult(File file);

        void shareImage(Uri uri);

        void showWallPagerResult(boolean isSuccessful);

        void showCollectResult();
    }

    interface Presenter extends BasePresenter {

        void downloadImage(BaseModel baseModel);

        void prepareShareImage(BaseModel baseModel);

        void setAsWallPager(BaseModel baseModel);

        void collectImage(BaseModel baseModel);
    }
}
