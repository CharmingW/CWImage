package com.charmingwong.cwimage.wallpaper;

import android.util.Log;

import java.util.List;

/**
 * Created by CharmingWong on 2017/6/4.
 */

public class WallpaperPresenter implements WallpaperContract.Presenter {

    private static final String TAG = "WallpaperPresenter";
    private WallpaperContract.View mView;

    private WallpaperApi mApi;

    private DesktopApi mDesktopApi;

    private int mPage = 1;

    private String mCurrentPath;

    private int mType;

    public WallpaperPresenter(WallpaperContract.View view, WallpaperApi api, DesktopApi desktopApi) {
        mView = view;
        mView.setPresenter(this);
        mApi = api;
        mApi.registerSearchListener(new WallpaperApi.QueryListener() {
            @Override
            public void onSearchCompleted(List<WallpaperCover> wallpaperCovers) {
                if (mPage == 1) {
                    mView.showQueryWallpaperCovers(wallpaperCovers);
                } else {
                    mView.showAppendedWallpaperCovers(wallpaperCovers);
                }
                mPage++;
            }

            @Override
            public void onSearchFailed() {

            }
        });
        mDesktopApi = desktopApi;
        mDesktopApi.registerSearchListener(new DesktopApi.QueryListener() {
            @Override
            public void onSearchCompleted(List<WallpaperCover> wallpaperCovers) {
                if (mPage == 1) {
                    mView.showQueryWallpaperCovers(wallpaperCovers);
                } else {
                    mView.showAppendedWallpaperCovers(wallpaperCovers);
                }
                mPage++;
            }

            @Override
            public void onSearchFailed() {

            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void queryWallpaperCovers(String tag1, String tag2, String tag3, String tag4) {

        String path = tagsToPath(tag1, tag2, tag3, tag4);
        if (!path.equals(mCurrentPath)) {
            mPage = 1;
        }
        mCurrentPath = path;
        path = appendPage(path);

        if ("电脑壁纸".equals(tag4)) {
            mDesktopApi.search(path);
            mType = 1;
        } else {
            mApi.search(path);
            mType = 0;
        }
        Log.i(TAG, "queryWallpaperCovers: " + path);
    }

    @Override
    public void appendWallpaperCovers() {
        String temp = appendPage(mCurrentPath);
        if (mType == 0) {
            mApi.search(temp);
        } else {
            mDesktopApi.search(temp);
        }

        Log.i(TAG, "appendWallpaperCovers: " + temp);
    }

    private String tagsToPath(String tag1, String tag2, String tag3, String tag4) {
        StringBuilder sb = new StringBuilder();
        tag1 = typeToCode(tag1);
        if (!"".equals(tag1)) {
            sb.append("/").append(tag1);
        }
        tag4 = orientationToCode(tag4);
        if (!"".equals(tag2) && !"".equals(tag4)) {
            sb.append("/").append(tag2).append("_").append(tag4);
        } else {
            if (!"".equals(tag2)) {
                sb.append("/").append(tag2);
            }
            if (!"".equals(tag4)) {
                sb.append("/").append(tag4);
            }
        }
        tag3 = orderToCode(tag3);
        if (!"".equals(tag3)) {
            sb.append("/").append(tag3);
        }
        return sb.toString();
    }

    private String appendPage(String path) {
        return path + "_" + mPage + ".html";
    }

    private String typeToCode(String type) {
        switch (type) {
            case "全部":
                return "";
            case "风景":
                return "fengjing";
            case "美女":
                return "meinv";
            case "动漫":
                return "dongman";
            case "创意":
                return "chuangyi";
            case "爱情":
                return "aiqing";
            case "卡通":
                return "katong";
            case "可爱":
                return "keai";
            case "明星":
                return "mingxing";
            case "游戏":
                return "youxi";
            case "车模":
                return "chemo";
            case "汽车":
                return "qiche";
            case "品牌":
                return "pinpai";
            case "体育":
                return "tiyu";
            case "节日":
                return "jieri";
            case "影视":
                return "yingshi";
            case "建筑":
                return "jianzhu";
            case "动物":
                return "dongwu";
            case "植物":
                return "zhiwu";
            case "星座":
                return "xingzuo";
            case "美食":
                return "meishi";
            case "其他":
                return "qita";
            default:
                return "";
        }
    }

    private String orientationToCode(String orientation) {
        switch (orientation) {
            case "全部":
                return "p1";
            case "竖屏":
                return "p2";
            case "宽屏":
                return "p3";
            default:
                return "";
        }
    }

    private String orderToCode(String order) {
        switch (order) {
            case "最新":
                return "new";
            case "按推荐数":
                return "good";
            case "按下载量":
                return "hot";
            default:
                return "";
        }
    }
}
