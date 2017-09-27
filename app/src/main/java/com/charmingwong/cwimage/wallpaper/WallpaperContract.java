package com.charmingwong.cwimage.wallpaper;

import com.charmingwong.cwimage.base.BasePresenter;
import com.charmingwong.cwimage.base.BaseView;

import java.util.List;

/**
 * Created by CharmingWong on 2017/6/4.
 */

public interface WallpaperContract {

    interface View extends BaseView<Presenter> {

        void showQueryWallpaperCovers(List<WallpaperCover> wallpaperCovers);

        void showAppendedWallpaperCovers(List<WallpaperCover> wallpaperCovers);
    }

    interface Presenter extends BasePresenter {

        void queryWallpaperCovers(String tag1, String tag2, String tag3, String tag4);

        void appendWallpaperCovers();

    }
}
