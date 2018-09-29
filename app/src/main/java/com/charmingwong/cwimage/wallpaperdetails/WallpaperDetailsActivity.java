package com.charmingwong.cwimage.wallpaperdetails;

import android.os.Bundle;

import com.charmingwong.cwimage.base.BaseActivity;
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
        if (bundle == null) {
            return;
        }
        WallpaperDetailsFragment fragment;
        if (savedInstanceState == null) {
            fragment = WallpaperDetailsFragment.newInstance();
            fragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        } else {
             fragment = (WallpaperDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
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
