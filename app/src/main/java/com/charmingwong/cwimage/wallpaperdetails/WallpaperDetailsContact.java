package com.charmingwong.cwimage.wallpaperdetails;

import android.net.Uri;

import com.charmingwong.cwimage.BaseModel;
import com.charmingwong.cwimage.BasePresenter;
import com.charmingwong.cwimage.BaseView;

import java.io.File;

/**
 * Created by CharmingWong on 2017/6/8.
 */

public interface WallpaperDetailsContact {

    interface View extends BaseView<WallpaperDetailsContact.Presenter> {

        void show(int count);

        void showWallPaperDetails(int position, Wallpaper wallpaper);

        void showDownloadResult(File file);

        void shareImage(Uri uri);

        void showWallPagerResult(boolean isSuccessful);

        void showCollectResult();
    }

    interface Presenter extends BasePresenter {

        void loadStartUrl(String url);

        void loadWallPaperUrl(int position);

        void downloadImage(BaseModel baseModel);

        void prepareShareImage(BaseModel baseModel);

        void setAsWallPager(BaseModel baseModel);

        void collectImage(BaseModel baseModel);
    }
}
