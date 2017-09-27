package com.charmingwong.cwimage.imagelibrary;

import com.charmingwong.cwimage.base.BaseModel;
import com.charmingwong.cwimage.dao.CollectionImageDao;
import com.charmingwong.cwimage.dao.CollectionTagDao;
import com.charmingwong.cwimage.dao.DaoSession;
import com.charmingwong.cwimage.dao.DownloadImageDao;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/27.
 */

public class ImageLibraryPresenter implements ImageLibraryContract.Presenter {

    private final ImageLibraryContract.View mView;

    private final DaoSession mDaoSession;

    private List<BaseModel> mDownloadImages;

    private List<BaseModel> mCollectionImages;

    private List<CollectionTag> mCollectionTags;

    public ImageLibraryPresenter(ImageLibraryContract.View view, DaoSession daoSession) {
        mView = view;
        mView.setPresenter(this);
        mDaoSession = daoSession;
    }

    @Override
    public void start() {
        loadDownload();
        loadCollection();
        loadTags();
        mView.getInitialData(mDownloadImages, mCollectionImages, mCollectionTags);
    }

    @Override
    public void loadDownloadImage() {
        if (mDownloadImages != null) {
            mView.showDownloads(mDownloadImages);
            return;
        }
        loadDownload();
        mView.showDownloads(mDownloadImages);
    }

    private void loadDownload() {
        DownloadImageDao downloadImageDao = mDaoSession.getDownloadImageDao();
        Query query = downloadImageDao.queryBuilder().orderDesc(DownloadImageDao.Properties.Date).build();
        mDownloadImages = query.list();
    }

    @Override
    public void loadCollectionImage() {
        if (mCollectionImages != null) {
            mView.showCollection(mCollectionImages);
            return;
        }
        loadCollection();
        mView.showCollection(mCollectionImages);
    }

    private void loadCollection() {
        CollectionImageDao collectionImageDao = mDaoSession.getCollectionImageDao();
        Query query = collectionImageDao.queryBuilder().orderDesc(CollectionImageDao.Properties.Date).build();
        mCollectionImages = query.list();
    }

    @Override
    public void loadCollectionTags() {
        if (mCollectionTags != null) {
            mView.showCollectedTags(mCollectionTags);
            return;
        }
        loadTags();
        mView.showCollectedTags(mCollectionTags);
    }

    private void loadTags() {
        CollectionTagDao collectionTagDao = mDaoSession.getCollectionTagDao();
        Query<CollectionTag> query = collectionTagDao.queryBuilder().orderDesc(CollectionTagDao.Properties.Date).build();
        mCollectionTags = query.list();
    }

    @Override
    public void deleteDownloadImages(List<Integer> indexes) {
        DownloadImageDao downloadImageDao = mDaoSession.getDownloadImageDao();
        List<DownloadImage> deleted = new ArrayList<>(indexes.size());
        for (int index : indexes) {
            deleted.add(((DownloadImage) mDownloadImages.get(index)));
        }
        downloadImageDao.deleteInTx(deleted);

        for (DownloadImage downloadImage : deleted) {
            mDownloadImages.remove(downloadImage);
        }
    }

    @Override
    public void deleteCollectionImages(List<Integer> indexes) {
        CollectionImageDao collectionImageDao = mDaoSession.getCollectionImageDao();
        List<CollectionImage> deleted = new ArrayList<>(indexes.size());
        for (int index : indexes) {
            deleted.add(((CollectionImage) mCollectionImages.get(index)));
        }
        collectionImageDao.deleteInTx(deleted);
        for (CollectionImage collectionImage : deleted) {
            mCollectionImages.remove(collectionImage);
        }
    }

    @Override
    public void deleteCollectionTags(List<Integer> indexes) {
        CollectionTagDao collectionTagDao = mDaoSession.getCollectionTagDao();
        List<CollectionTag> deleted = new ArrayList<>(indexes.size());
        for (int index : indexes) {
            deleted.add(( mCollectionTags.get(index)));
        }
        collectionTagDao.deleteInTx(deleted);

        for (CollectionTag collectionTag : deleted) {
            mCollectionTags.remove(collectionTag);
        }
    }
}
