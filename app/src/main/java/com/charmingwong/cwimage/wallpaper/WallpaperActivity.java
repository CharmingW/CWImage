package com.charmingwong.cwimage.wallpaper;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.charmingwong.cwimage.BaseActivity;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.util.ActivityUtils;

public class WallpaperActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        ImageView imageView= (ImageView) findViewById(R.id.bar_image);
        Glide.with(this)
                .load(R.drawable.ic_bar_2)
                .into(imageView);

        WallpaperFragment fragment = (WallpaperFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = new WallpaperFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        new WallpaperPresenter(fragment, WallpaperApi.newInstance(), DesktopApi.newInstance());
    }
}
