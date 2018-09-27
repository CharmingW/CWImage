package com.charmingwong.cwimage.imagechannel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huangchaoming on 2018/9/27
 */

public class ChannelPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "ChannelPagerAdapter";

    private List<String> mTitles;
    private ChannelItemFragment.ChangeChannelImagesCountListener mChangeChannelImagesCountListener;

    public void setTitles(List<String> titles) {
        mTitles = titles;
        if (mTitles == null) {
            mTitles = new ArrayList<>();
        }
    }

    public void setChangeChannelImagesCountListener(ChannelItemFragment.ChangeChannelImagesCountListener changeChannelImagesCountListener) {
        mChangeChannelImagesCountListener = changeChannelImagesCountListener;
    }

    ChannelPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: ");
        ChannelItemFragment fragment = ChannelItemFragment.newInstance(position, mChangeChannelImagesCountListener);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}