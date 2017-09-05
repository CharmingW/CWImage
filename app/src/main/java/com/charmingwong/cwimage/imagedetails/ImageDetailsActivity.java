package com.charmingwong.cwimage.imagedetails;

import android.os.Bundle;
import com.charmingwong.cwimage.BaseActivity;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.dao.DaoManager;
import com.charmingwong.cwimage.util.ActivityUtils;

public class ImageDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        ImageDetailsFragment fragment = (ImageDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = ImageDetailsFragment.newInstance();
            fragment.setArguments(getIntent().getExtras());
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        new ImageDetailsPresenter(fragment, DaoManager.getInstance(getApplicationContext()  ));
    }
}
