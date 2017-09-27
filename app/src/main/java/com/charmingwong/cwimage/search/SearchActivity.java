package com.charmingwong.cwimage.search;

import android.os.Bundle;
import com.charmingwong.cwimage.base.BaseActivity;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.dao.DaoManager;
import com.charmingwong.cwimage.util.ActivityUtils;

public class SearchActivity extends BaseActivity {

    public void setActivityActionListener(ActivityActionListener listener) {
        mListener = listener;
    }

    private ActivityActionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = new SearchFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        new SearchPresenter(fragment, DaoManager.getInstance(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        boolean canGoBack = mListener.canGoBack();
        if (!canGoBack) {
            super.onBackPressed();
        }

    }


    interface ActivityActionListener {
        boolean canGoBack();
    }
}
