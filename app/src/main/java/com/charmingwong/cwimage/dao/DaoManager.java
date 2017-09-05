package com.charmingwong.cwimage.dao;

import android.content.Context;
import org.greenrobot.greendao.database.Database;

/**
 * Created by CharmingWong on 2017/7/21.
 */

public class DaoManager {

  private static DaoSession mDaoSession;

  public static DaoSession getInstance(Context context) {
    if (mDaoSession == null) {
      DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "images_db");
      Database db = helper.getWritableDb();
      mDaoSession = new DaoMaster(db).newSession();
    }
    return mDaoSession;
  }
}
