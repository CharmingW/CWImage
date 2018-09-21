package com.charmingwong.cwimage.imagelibrary;

import com.charmingwong.cwimage.base.BaseModel;
import com.charmingwong.cwimage.dao.CollectionImageDao;
import com.charmingwong.cwimage.dao.CollectionTagDao;
import com.charmingwong.cwimage.dao.DaoSession;
import com.charmingwong.cwimage.dao.DownloadImageDao;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.query.Query;

/**
 * Created by CharmingWong on 2017/5/27.
 */

public class ImageLibraryPresenter implements ImageLibraryContract.Presenter {

    private final ImageLibraryContract.View mView;

    private final DaoSession mDaoSession;

    private List<BaseModel> mDownloadImages;

    private List<BaseModel> mCollectionImages;

    private List<CollectionTag> mCollectionTags;

    ImageLibraryPresenter(ImageLibraryContract.View view, DaoSession daoSession) {
        mView = view;
        mView.setPresenter(this);
        mDaoSession = daoSession;
    }

    @Override
    public void start() {
        executeDaoTask(new Runnable() {
            @Override
            public void run() {
                loadDownload();
                loadCollection();
                loadTags();
            }
        }, new Runnable() {
            @Override
            public void run() {
                mView.showDownloads(mDownloadImages);
                mView.showCollection(mCollectionImages);
                mView.showCollectedTags(mCollectionTags);
            }
        });
    }

    @Override
    public void loadDownloadImage() {
        if (mDownloadImages != null) {
            mView.showDownloads(mDownloadImages);
            return;
        }
        executeDaoTask(new Runnable() {
            @Override
            public void run() {
                loadDownload();
            }
        }, new Runnable() {
            @Override
            public void run() {
                mView.showDownloads(mDownloadImages);
            }
        });
    }

    private void loadDownload() {
        DownloadImageDao downloadImageDao = mDaoSession.getDownloadImageDao();
        Query query = downloadImageDao.queryBuilder().orderDesc(DownloadImageDao.Properties.Date)
            .build();
        mDownloadImages = query.list();
    }

    @Override
    public void loadCollectionImage() {
        if (mCollectionImages != null) {
            mView.showCollection(mCollectionImages);
            return;
        }
        executeDaoTask(new Runnable() {
            @Override
            public void run() {
                loadCollection();
            }
        }, new Runnable() {
            @Override
            public void run() {
                mView.showCollection(mCollectionImages);
            }
        });
    }

    private void loadCollection() {
        CollectionImageDao collectionImageDao = mDaoSession.getCollectionImageDao();
        Query query = collectionImageDao.queryBuilder()
            .orderDesc(CollectionImageDao.Properties.Date).build();
        mCollectionImages = query.list();
    }

    @Override
    public void loadCollectionTags() {
        if (mCollectionTags != null) {
            mView.showCollectedTags(mCollectionTags);
            return;
        }
        executeDaoTask(new Runnable() {
            @Override
            public void run() {
                loadTags();
            }
        }, new Runnable() {
            @Override
            public void run() {
                mView.showCollectedTags(mCollectionTags);
            }
        });
    }

    private void loadTags() {
        CollectionTagDao collectionTagDao = mDaoSession.getCollectionTagDao();
        Query<CollectionTag> query = collectionTagDao.queryBuilder()
            .orderDesc(CollectionTagDao.Properties.Date).build();
        mCollectionTags = query.list();
    }

    @Override
    public void deleteDownloadImages(final List<Integer> indexes) {
        executeDaoTask(new Runnable() {
            @Override
            public void run() {
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
        }, null);
    }

    @Override
    public void deleteCollectionImages(final List<Integer> indexes) {
        executeDaoTask(new Runnable() {
            @Override
            public void run() {
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
        }, null);
    }

    @Override
    public void deleteCollectionTags(final List<Integer> indexes) {
        executeDaoTask(new Runnable() {
            @Override
            public void run() {
                CollectionTagDao collectionTagDao = mDaoSession.getCollectionTagDao();
                List<CollectionTag> deleted = new ArrayList<>(indexes.size());
                for (int index : indexes) {
                    deleted.add((mCollectionTags.get(index)));
                }
                collectionTagDao.deleteInTx(deleted);

                for (CollectionTag collectionTag : deleted) {
                    mCollectionTags.remove(collectionTag);
                }
            }
        }, null);
    }

    private void executeDaoTask(final Runnable task, final Runnable callback) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                task.run();
                emitter.onComplete();
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(new Action() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.run();
                    }
                }
            })
            .subscribe();
    }
}
