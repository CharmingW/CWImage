package com.charmingwong.cwimage.wallpaperdetails;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.charmingwong.cwimage.base.BaseModel;
import com.charmingwong.cwimage.dao.CollectionImageDao;
import com.charmingwong.cwimage.dao.DaoSession;
import com.charmingwong.cwimage.dao.DownloadImageDao;
import com.charmingwong.cwimage.imagelibrary.CollectionImage;
import com.charmingwong.cwimage.imagelibrary.DownloadImage;
import com.charmingwong.cwimage.util.ApplicationUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by CharmingWong on 2017/6/8.
 */

public class WallpaperDetailsPresenter implements WallpaperDetailsContact.Presenter {

    private static final String TAG = "WpDetailsPresenter";

    private WallpaperDetailsContact.View mView;

    private WallpaperDetailsApi mApi;

    private WallpaperDetailsStartApi mStartApi;

    private DaoSession mDaoSession;

    private String mUrl;

    private List<Integer> mImagePages;

    private int mType;

    public WallpaperDetailsPresenter(WallpaperDetailsContact.View view, DaoSession daoSession, WallpaperDetailsApi api, WallpaperDetailsStartApi startApi, int type) {
        mView = view;
        mView.setPresenter(this);
        mDaoSession = daoSession;
        mApi = api;
        mApi.registerSearchListener(new WallpaperDetailsApi.QueryListener() {
            @Override
            public void onSearchCompleted(int position, Wallpaper wallpaper) {
                mView.showWallPaperDetails(position, wallpaper);
                Log.i(TAG, "onSearchCompleted: " + wallpaper.getUrl());
            }

            @Override
            public void onSearchFailed() {
                Log.i(TAG, "onSearchFailed: load image failed");
            }
        });

        mStartApi = startApi;
        mStartApi.registerSearchListener(new WallpaperDetailsStartApi.QueryListener() {
            @Override
            public void onSearchCompleted(List<String> urls) {
                mUrl = urls.get(0);
                Log.i(TAG, "onSearchCompleted: " + mUrl);
                mImagePages = urlsToPages(urls.subList(1, urls.size()));
                mView.show(mImagePages.size());
            }

            @Override
            public void onSearchFailed() {
                Log.i(TAG, "onSearchFailed: load image list failed");
            }
        });
        mType = type;
    }

    private List<Integer> urlsToPages(List<String> urls) {
        List<Integer> pages = new ArrayList<>(urls.size());
        for (String url : urls) {
            int start;
            int end;
            if (mType == 0) {
                start = url.lastIndexOf('_') + 1;
                end = url.lastIndexOf('.');
            } else {
                start = url.indexOf('_') + 1;
                end = url.lastIndexOf('_');
            }
            int page = Integer.parseInt(url.substring(start, end));
            pages.add(page);
            Log.i(TAG, "urlsToPages: " + page);
        }
        return pages;
    }

    private String buildUrl(int position) {
        int start = mUrl.indexOf('_') + 1;
        int end = mUrl.lastIndexOf('_');
        return mUrl.substring(0, start) + mImagePages.get(position) + mUrl.substring(end, mUrl.length());
    }

    @Override
    public void start() {

    }

    @Override
    public void loadStartUrl(String url) {
        mStartApi.search(url);
    }

    @Override
    public void loadWallPaperUrl(int position) {
        String url = buildUrl(position);
        Log.i(TAG, "loadWallPaperUrl: " + url);
        mApi.search(position, url);
    }

    @Override
    public void downloadImage(final BaseModel baseModel) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = baseModel.getUrl();
                final String thumbUrl = baseModel.getThumbUrl();

                File source = download(url);
                if (source == null) {
                    source = download(thumbUrl);
                }

                String urlSuffix = ApplicationUtils.getImageUrlSuffix(url);
                String thumbUrlSuffix = ApplicationUtils.getImageUrlSuffix(thumbUrl);
                String suffix = "".equals(urlSuffix) ? thumbUrlSuffix : urlSuffix;

                final File file = insertIntoGallery(source, suffix);
                ((Fragment) mView).getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.showDownloadResult(file);
                        if (file != null) {
                            DownloadImageDao downloadImageDao = mDaoSession.getDownloadImageDao();
                            downloadImageDao.insertOrReplace(new DownloadImage(null, url, baseModel.getHeight(), baseModel.getWidth(), new Date()));
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void prepareShareImage(final BaseModel baseModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = baseModel.getUrl();
                final String thumbUrl = baseModel.getThumbUrl();

                File sharedImage = download(url);
                if (sharedImage == null) {
                    sharedImage = download(thumbUrl);
                }

                String urlSuffix = ApplicationUtils.getImageUrlSuffix(url);
                String thumbUrlSuffix = ApplicationUtils.getImageUrlSuffix(thumbUrl);
                String suffix = "".equals(urlSuffix) ? thumbUrlSuffix : urlSuffix;

                final File file = insertIntoGallery(sharedImage, suffix);
                ((Fragment) mView).getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (file == null) {
                            mView.shareImage(null);
                            return;
                        }
                        mView.shareImage(Uri.fromFile(file));
                    }
                });
            }
        }).start();
    }

    @Override
    public void setAsWallPager(final BaseModel baseModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File wallPaper = download(baseModel.getUrl());
                WallpaperManager wpm = WallpaperManager.getInstance(((Fragment) mView).getActivity());
                DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
                int desiredMinimumWidth = dm.widthPixels;
                int desiredMinimumHeight = dm.heightPixels;
                Log.v("ss", "" + desiredMinimumWidth);
                Log.v("ss", "" + desiredMinimumHeight);
                wpm.suggestDesiredDimensions(desiredMinimumWidth, desiredMinimumHeight);
                try {
                    wpm.setStream(new FileInputStream(wallPaper));
                    ((Fragment) mView).getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.showWallPagerResult(true);
                        }
                    });
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((Fragment) mView).getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.showWallPagerResult(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public void collectImage(BaseModel baseModel) {
        CollectionImageDao collectionImageDao = mDaoSession.getCollectionImageDao();
        long row = collectionImageDao.insertOrReplace(new CollectionImage(null, baseModel.getUrl(), baseModel.getHeight(), baseModel.getWidth(), new Date()));
        if (row >= 0) {
            mView.showCollectResult();
        }
    }

    private File insertIntoGallery(File source, String suffix) {
        if (source == null) {
            return null;
        }
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        final File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CWImage");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null;
            }
        }

        final File file = new File(dir, System.currentTimeMillis() + suffix);
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (fis.read(buffer) != -1) {
                fos.write(buffer, 0, buffer.length);
            }
            ((Fragment) mView).getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private File download(String url) {

        try {
            return Glide.with(((Fragment) mView).getActivity())
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
