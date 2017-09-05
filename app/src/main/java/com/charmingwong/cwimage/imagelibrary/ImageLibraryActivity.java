package com.charmingwong.cwimage.imagelibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.dao.DaoManager;
import com.charmingwong.cwimage.util.ActivityUtils;

public class ImageLibraryActivity extends AppCompatActivity {

    public static int FROM_TAG = 0;

    public void setActivityActionListener(ActivityActionListener listener) {
        mListener = listener;
    }

    private ActivityActionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        ImageView imageView = (ImageView) findViewById(R.id.bar_image);
        Glide.with(this)
                .load(R.drawable.ic_bar_1)
                .into(imageView);


        ImageLibraryFragment fragment = (ImageLibraryFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = ImageLibraryFragment.newInstance();
            Bundle args = new Bundle();
            args.putInt("page", getIntent().getIntExtra("page", 0));
            fragment.setArguments(args);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        new ImageLibraryPresenter(fragment, DaoManager.getInstance(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        if (!mListener.canGoBack()) {
            super.onBackPressed();
        }
    }

    interface ActivityActionListener {
        boolean canGoBack();
    }
}
