package com.charmingwong.cwimage.wallpaperdetails;

import android.os.Bundle;
import com.charmingwong.cwimage.BaseActivity;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.dao.DaoManager;
import com.charmingwong.cwimage.util.ActivityUtils;

/**
 * Created by CharmingWong on 2017/6/8.
 */

public class WallpaperDetailsActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_details);

    Bundle bundle = getIntent().getExtras();
    WallpaperDetailsFragment fragment = (WallpaperDetailsFragment) getSupportFragmentManager()
        .findFragmentById(R.id.contentFrame);
    if (fragment == null) {
      fragment = WallpaperDetailsFragment.newInstance();
      fragment.setArguments(bundle);
      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
    }
    new WallpaperDetailsPresenter(
        fragment,
        DaoManager.getInstance(getApplicationContext()),
        WallpaperDetailsApi.newInstance(bundle.getInt("type")),
        WallpaperDetailsStartApi.newInstance(bundle.getString("size"), bundle.getInt("type")),
        bundle.getInt("type")
    );
  }

}
