package com.charmingwong.cwimage.imagedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.charmingwong.cwimage.base.BaseActivity;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.dao.DaoManager;
import com.charmingwong.cwimage.util.ActivityUtils;

public class ImageDetailsActivity extends BaseActivity {

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ImageDetailsContract.View) {
            new ImageDetailsPresenter((ImageDetailsContract.View) fragment, DaoManager.getInstance(getApplicationContext()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        ImageDetailsFragment fragment;
        if (savedInstanceState == null) {
            fragment = ImageDetailsFragment.newInstance();
            fragment.setArguments(getIntent().getExtras());
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }
}
