package com.charmingwong.cwimage.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.charmingwong.cwimage.imagelibrary.DownloadImage;
import com.charmingwong.cwimage.imagelibrary.CollectionImage;
import com.charmingwong.cwimage.imagelibrary.CollectionTag;
import com.charmingwong.cwimage.search.model.SearchRecord;

import com.charmingwong.cwimage.dao.DownloadImageDao;
import com.charmingwong.cwimage.dao.CollectionImageDao;
import com.charmingwong.cwimage.dao.CollectionTagDao;
import com.charmingwong.cwimage.dao.SearchRecordDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig downloadImageDaoConfig;
    private final DaoConfig collectionImageDaoConfig;
    private final DaoConfig collectionTagDaoConfig;
    private final DaoConfig searchRecordDaoConfig;

    private final DownloadImageDao downloadImageDao;
    private final CollectionImageDao collectionImageDao;
    private final CollectionTagDao collectionTagDao;
    private final SearchRecordDao searchRecordDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        downloadImageDaoConfig = daoConfigMap.get(DownloadImageDao.class).clone();
        downloadImageDaoConfig.initIdentityScope(type);

        collectionImageDaoConfig = daoConfigMap.get(CollectionImageDao.class).clone();
        collectionImageDaoConfig.initIdentityScope(type);

        collectionTagDaoConfig = daoConfigMap.get(CollectionTagDao.class).clone();
        collectionTagDaoConfig.initIdentityScope(type);

        searchRecordDaoConfig = daoConfigMap.get(SearchRecordDao.class).clone();
        searchRecordDaoConfig.initIdentityScope(type);

        downloadImageDao = new DownloadImageDao(downloadImageDaoConfig, this);
        collectionImageDao = new CollectionImageDao(collectionImageDaoConfig, this);
        collectionTagDao = new CollectionTagDao(collectionTagDaoConfig, this);
        searchRecordDao = new SearchRecordDao(searchRecordDaoConfig, this);

        registerDao(DownloadImage.class, downloadImageDao);
        registerDao(CollectionImage.class, collectionImageDao);
        registerDao(CollectionTag.class, collectionTagDao);
        registerDao(SearchRecord.class, searchRecordDao);
    }
    
    public void clear() {
        downloadImageDaoConfig.clearIdentityScope();
        collectionImageDaoConfig.clearIdentityScope();
        collectionTagDaoConfig.clearIdentityScope();
        searchRecordDaoConfig.clearIdentityScope();
    }

    public DownloadImageDao getDownloadImageDao() {
        return downloadImageDao;
    }

    public CollectionImageDao getCollectionImageDao() {
        return collectionImageDao;
    }

    public CollectionTagDao getCollectionTagDao() {
        return collectionTagDao;
    }

    public SearchRecordDao getSearchRecordDao() {
        return searchRecordDao;
    }

}
