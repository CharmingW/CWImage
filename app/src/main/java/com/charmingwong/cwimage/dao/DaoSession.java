package com.charmingwong.cwimage.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.charmingwong.cwimage.search.model.SearchRecord;
import com.charmingwong.cwimage.imagelibrary.DownloadImage;
import com.charmingwong.cwimage.imagelibrary.CollectionTag;
import com.charmingwong.cwimage.imagelibrary.CollectionImage;

import com.charmingwong.cwimage.dao.SearchRecordDao;
import com.charmingwong.cwimage.dao.DownloadImageDao;
import com.charmingwong.cwimage.dao.CollectionTagDao;
import com.charmingwong.cwimage.dao.CollectionImageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig searchRecordDaoConfig;
    private final DaoConfig downloadImageDaoConfig;
    private final DaoConfig collectionTagDaoConfig;
    private final DaoConfig collectionImageDaoConfig;

    private final SearchRecordDao searchRecordDao;
    private final DownloadImageDao downloadImageDao;
    private final CollectionTagDao collectionTagDao;
    private final CollectionImageDao collectionImageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        searchRecordDaoConfig = daoConfigMap.get(SearchRecordDao.class).clone();
        searchRecordDaoConfig.initIdentityScope(type);

        downloadImageDaoConfig = daoConfigMap.get(DownloadImageDao.class).clone();
        downloadImageDaoConfig.initIdentityScope(type);

        collectionTagDaoConfig = daoConfigMap.get(CollectionTagDao.class).clone();
        collectionTagDaoConfig.initIdentityScope(type);

        collectionImageDaoConfig = daoConfigMap.get(CollectionImageDao.class).clone();
        collectionImageDaoConfig.initIdentityScope(type);

        searchRecordDao = new SearchRecordDao(searchRecordDaoConfig, this);
        downloadImageDao = new DownloadImageDao(downloadImageDaoConfig, this);
        collectionTagDao = new CollectionTagDao(collectionTagDaoConfig, this);
        collectionImageDao = new CollectionImageDao(collectionImageDaoConfig, this);

        registerDao(SearchRecord.class, searchRecordDao);
        registerDao(DownloadImage.class, downloadImageDao);
        registerDao(CollectionTag.class, collectionTagDao);
        registerDao(CollectionImage.class, collectionImageDao);
    }
    
    public void clear() {
        searchRecordDaoConfig.clearIdentityScope();
        downloadImageDaoConfig.clearIdentityScope();
        collectionTagDaoConfig.clearIdentityScope();
        collectionImageDaoConfig.clearIdentityScope();
    }

    public SearchRecordDao getSearchRecordDao() {
        return searchRecordDao;
    }

    public DownloadImageDao getDownloadImageDao() {
        return downloadImageDao;
    }

    public CollectionTagDao getCollectionTagDao() {
        return collectionTagDao;
    }

    public CollectionImageDao getCollectionImageDao() {
        return collectionImageDao;
    }

}
