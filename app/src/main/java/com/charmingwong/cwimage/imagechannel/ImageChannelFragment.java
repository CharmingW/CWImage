package com.charmingwong.cwimage.imagechannel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.charmingwong.cwimage.App;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.common.ImageDialog;
import com.charmingwong.cwimage.imagechannel.ImageChannelContract.Presenter;
import com.charmingwong.cwimage.imagedetails.ImageDetailsActivity;
import com.charmingwong.cwimage.search.SearchActivity;
import com.charmingwong.cwimage.util.ApplicationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/14.
 */

public class ImageChannelFragment extends Fragment implements ImageChannelContract.View {

    private final String[] CHANNEL_TITLES = {
        "美女",      //                  beauty
        "搞笑",      //                  funny
        "壁纸",      //                  wallpaper
        "萌宠",      //                  pet
        "汽车",      //                  car
        "旅游",      //                  go
        "美食",      //                  foot
        "艺术",      //                  art
        "摄影",      //                  photography
        "设计",      //                  design
        "家居",      //                  home
        "图说世界",  //                  video
        "图解电影"   //                  news
    };

    private final ChannelItemFragment[] CHANNEL_ITEM_FRAGMENTS = new ChannelItemFragment[CHANNEL_TITLES.length];

    private int mFragmentCount = 0;

    private static final String TAG = "ImageChannelFragment";

    private ImageChannelContract.Presenter mPresenter;

    private ChannelPagerAdapter mChannelPagerAdapter;

    private SwipeRefreshLayout mRefreshLayout;

    private BuilderManager mBuilderManager;

    private int mCurrentChannel = 0;

    private int mCurrentTag;

    @Override
    public int getCurrentChannelImageLastIndex() {
        return getChannelItemImagesLastIndex(mCurrentChannel);
    }

    public ImageChannelFragment() {
    }

    public static ImageChannelFragment newInstance() {
        return new ImageChannelFragment();
    }

    @Override
    public void setPresenter(@NonNull ImageChannelContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        CHANNEL_ITEM_FRAGMENTS[mFragmentCount++] = (ChannelItemFragment) childFragment;
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            mCurrentChannel = position;
            mRefreshLayout.setRefreshing(false);

            if (CHANNEL_ITEM_FRAGMENTS[position] != null) {
                final View rootView = CHANNEL_ITEM_FRAGMENTS[position].getView();
                View view = rootView.findViewById(R.id.progressBar);
                if (getChannelItemImagesLastIndex(position) == 0) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }

            App.getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBuilderManager.setupBoomMenu(position);
                }
            }, 50);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_image_channel, container, false);

        //ViewPager
        ViewPager viewPager = rootView.findViewById(R.id.channelPager);
        mChannelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager());
        mChannelPagerAdapter.setTitles(Arrays.asList(CHANNEL_TITLES));
        mChannelPagerAdapter.setChangeChannelImagesCountListener(mChangeChannelImagesCountListener);
        viewPager.setAdapter(mChannelPagerAdapter);

        TabLayout tabLayout = getActivity().findViewById(R.id.channelTabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(mPageChangeListener);
        viewPager.setOffscreenPageLimit(1);

        //下拉刷新控件
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh(mCurrentChannel, mCurrentTag);
            }
        };
        mRefreshLayout.setOnRefreshListener(refreshListener);

        //搜索条
        View search_bar = getActivity().findViewById(R.id.search_bar);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //浮动按钮
        FloatingActionButton fabTop = rootView.findViewById(R.id.fab_top);
        fabTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecyclerView) CHANNEL_ITEM_FRAGMENTS[mCurrentChannel].getView()
                    .findViewById(R.id.channelImageList)).smoothScrollToPosition(0);
                ((AppBarLayout) getActivity().findViewById(R.id.app_bar)).setExpanded(true);
            }
        });

        setupBoomMenu(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupBoomMenu(View parent) {
        mBuilderManager = new BuilderManager(parent);

        mBuilderManager
            .setBoomMenuButtonClickListener(new BuilderManager.BoomMenuButtonClickListener() {
                @Override
                public void onClick(int tag) {
                    if (tag == mCurrentTag) {
                        return;
                    }
                    resetChannelItemImagesLastIndex(mCurrentChannel);
                    mPresenter.refresh(mCurrentChannel, tag);
                    ((RecyclerView) CHANNEL_ITEM_FRAGMENTS[mCurrentChannel]
                        .getView()
                        .findViewById(R.id.channelImageList))
                        .smoothScrollToPosition(0);
                    mCurrentTag = tag;
                }
            });
        mBuilderManager.setupBoomMenu(mCurrentChannel);
        mBuilderManager.showBoomMenuButton(true);
    }

    @Override
    public void showChannelImages(int channel, List<ChannelImage> channelImages) {

        Fragment fragment = CHANNEL_ITEM_FRAGMENTS[channel];
        final View rootView = fragment.getView();
        if (fragment.getView() == null) {
            return;
        }

        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        rootView.findViewById(R.id.error_view).setVisibility(View.GONE);
        ChannelImageListAdapter adapter = getChannelImageListAdapter(channel);
        if (adapter == null) {
            return;
        }
        adapter.appendImages(channelImages);
        updateChannelItemImagesLastIndex(channel);
    }

    @Override
    public void showRefreshedChannelImages(int channel, List<ChannelImage> channelImages) {
        final View rootView = CHANNEL_ITEM_FRAGMENTS[channel].getView();
        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        rootView.findViewById(R.id.error_view).setVisibility(View.GONE);
        ChannelImageListAdapter adapter = getChannelImageListAdapter(channel);
        if (adapter == null) {
            return;
        }
        adapter.replaceData(channelImages);
        updateChannelItemImagesLastIndex(channel);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    public void showChannelImagesNull(int channel) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        resetChannelItemImagesLastIndex(channel);
        mPresenter.refresh(channel, mCurrentTag);
    }

    @Override
    public void showChannelImagesFailed(int channel) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        final View rootView = CHANNEL_ITEM_FRAGMENTS[channel].getView();
        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        if (getChannelItemImagesLastIndex(channel) == 0) {
            rootView.findViewById(R.id.error_view).setVisibility(View.VISIBLE);
        }
    }

    private ChannelImageListAdapter getChannelImageListAdapter(int channel) {
        if (CHANNEL_ITEM_FRAGMENTS[channel] == null) {
            CHANNEL_ITEM_FRAGMENTS[channel] = ChannelItemFragment.newInstance(channel, mChangeChannelImagesCountListener);
        }
        View view = CHANNEL_ITEM_FRAGMENTS[channel].getView();
        if (view == null) {
            return null;
        }
        return (ChannelImageListAdapter) ((RecyclerView) view.findViewById(R.id.channelImageList))
            .getAdapter();
    }

    private void resetChannelItemImagesLastIndex(int channel) {
        Bundle args = getChannelFragmentArgs(channel);
        args.putInt("image_last_index", 0);
    }

    private void updateChannelItemImagesLastIndex(int channel) {
        Bundle args = getChannelFragmentArgs(channel);
        int count = args.getInt("image_last_index");
        args.putInt("image_last_index", count + 40);
        args.putBoolean("isLoading", false);
    }

    private int getChannelItemImagesLastIndex(int channel) {
        Bundle args = getChannelFragmentArgs(channel);
        return args.getInt("image_last_index");
    }

    private Bundle getChannelFragmentArgs(int channel) {
        return CHANNEL_ITEM_FRAGMENTS[channel].getArguments();
    }

    private ChannelItemFragment.ChangeChannelImagesCountListener mChangeChannelImagesCountListener = new ChannelItemFragment.ChangeChannelImagesCountListener() {
        @Override
        public void updateChannelImagesCount() {
            mPresenter.query(mCurrentChannel, mCurrentTag);
            getChannelFragmentArgs(mCurrentChannel).putBoolean("isLoading", true);
        }
    };
}
