package com.charmingwong.cwimage.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SearchContract.View) {
            new SearchPresenter((SearchContract.View) fragment, DaoManager.getInstance(getApplicationContext()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            SearchFragment fragment = new SearchFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
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
