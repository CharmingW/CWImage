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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannelPagerAdapter = new ChannelPagerAdapter(getFragmentManager());
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        for (int i = 0; i < CHANNEL_ITEM_FRAGMENTS.length; i++) {
//            ChannelItemFragment fragment = CHANNEL_ITEM_FRAGMENTS[i];
//            if (fragment != null) {
//                outState.putParcelableArrayList(CHANNEL_TITLES[i],
//                    (ArrayList<? extends Parcelable>) fragment.mChannelImageListAdapter.mChannelImages);
//            }
//        }
//    }

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
        viewPager.setAdapter(mChannelPagerAdapter);

        TabLayout tabLayout = ((Activity) container.getContext()).findViewById(R.id.channelTabs);
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
        mPresenter.start();
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
            CHANNEL_ITEM_FRAGMENTS[channel] = ChannelItemFragment
                .newInstance(channel, mChangeChannelImagesCountListener);
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

    interface ChangeChannelImagesCountListener {

        void updateChannelImagesCount();
    }

    private ChangeChannelImagesCountListener mChangeChannelImagesCountListener = new ChangeChannelImagesCountListener() {
        @Override
        public void updateChannelImagesCount() {
            mPresenter.query(mCurrentChannel, mCurrentTag);
            getChannelFragmentArgs(mCurrentChannel).putBoolean("isLoading", true);
        }
    };

    private class ChannelPagerAdapter extends FragmentPagerAdapter {

        ChannelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: ");
            ChannelItemFragment fragment = ChannelItemFragment
                .newInstance(position, mChangeChannelImagesCountListener);
            CHANNEL_ITEM_FRAGMENTS[position] = fragment;
            return fragment;
        }

        @Override
        public int getCount() {
            return CHANNEL_TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CHANNEL_TITLES[position];
        }
    }

    public static class ChannelItemFragment extends Fragment {

        private ChannelImageListAdapter mChannelImageListAdapter;

        private ChangeChannelImagesCountListener mListener;

        public static ChannelItemFragment newInstance(
            int position,
            ChangeChannelImagesCountListener listener) {
            Log.d(TAG, "newInstance: ");
            ChannelItemFragment fragment = new ChannelItemFragment();
            fragment.setListener(listener);
            Bundle args = new Bundle();
            args.putInt("num", position);
            args.putInt("image_last_index", 0);
            args.putBoolean("isLoading", false);
            fragment.setArguments(args);
            return fragment;
        }

        public ChannelItemFragment() {
        }

        public void setListener(ChangeChannelImagesCountListener listener) {
            mListener = listener;
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            Log.d(TAG, "onSaveInstanceState: ");
            outState.putBundle("args", getArguments());
        }

        @Override
        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);
            Log.d(TAG, "onViewStateRestored: ");
            if (savedInstanceState != null) {
                setArguments(savedInstanceState.getBundle("args"));
            }
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.d(TAG, "onActivityCreated: ");
            int position = getArguments() != null ? getArguments().getInt("num") : 0;
            Presenter presenter = ((ImageChannelActivity) getActivity()).getPresenter();
            presenter.query(position, initTagByPosition(position));
        }

        private int initTagByPosition(int position) {
            switch (position) {
                case 0:
                    return 599;
                case 1:
                    return 1993;
                case 2:
                    return 92;
                case 3:
                    return 234;
                case 4:
                    return 208;
                case 5:
                    return 1806;
                case 6:
                    return 309;
                case 7:
                    return 187;
                case 8:
                    return 222;
                case 9:
                    return 244;
                case 10:
                    return 592;
                case 11:
                    return 148;
                case 12:
                    return 1802;
            }
            return -1;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mChannelImageListAdapter = new ChannelImageListAdapter(getActivity());
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
            @Nullable Bundle savedInstanceState) {
            Log.d(TAG, "onCreateView: ");
            final View rootView = inflater
                .inflate(R.layout.fragment_image_channel_item, container, false);

            RecyclerView channelImageList = rootView
                .findViewById(R.id.channelImageList);
            channelImageList.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
                    @Override
                    public void smoothScrollToPosition(RecyclerView recyclerView,
                        RecyclerView.State state,
                        int position) {
                        LinearSmoothScroller scroller = new LinearSmoothScroller(
                            container.getContext()) {
                            @Override
                            protected int calculateTimeForScrolling(int dx) {

                                if (dx > 2500) {
                                    dx = 2500;
                                }
                                return super.calculateTimeForScrolling(dx);
                            }
                        };
                        scroller.setTargetPosition(0);
                        startSmoothScroll(scroller);
                    }
                });
            channelImageList.setAdapter(mChannelImageListAdapter);

            channelImageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        int[] lastVisibleIndexes = new int[3];
                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastVisibleIndexes);
                        int size = mChannelImageListAdapter.getItemCount();
                        boolean isLoading = getArguments() != null && getArguments().getBoolean("isLoading");
                        if ((lastVisibleIndexes[0] == size - 1
                            || lastVisibleIndexes[1] == size - 1
                            || lastVisibleIndexes[2] == size - 1) && !isLoading) {
                            mListener.updateChannelImagesCount();
                        }
                    }
                }
            });

            channelImageList.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                    RecyclerView.State state) {
                    int density = ApplicationUtils.getDisplayDensity();
                    outRect.set(density, density, density, density);
                }

            });

            return rootView;
        }

    }

    private static class ChannelImageListAdapter extends
        RecyclerView.Adapter<ChannelImageListAdapter.ChannelImageViewHolder> {

        private List<ChannelImage> mChannelImages;

        private Context mContext;

        ChannelImageListAdapter(Context context) {
            mContext = context;
            mChannelImages = new ArrayList<>(0);
        }

        void appendImages(List<ChannelImage> channelImages) {
            int size = getItemCount();
            mChannelImages.addAll(channelImages);
            notifyItemInserted(size);
        }

        void replaceData(List<ChannelImage> channelImages) {
            mChannelImages.clear();
            mChannelImages.addAll(channelImages);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mChannelImages.size();
        }

        @NonNull
        @Override
        public ChannelImageViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
            final View rootView = ((Activity) parent.getContext())
                .getLayoutInflater()
                .inflate(R.layout.item_image_channel, parent, false);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageDetailsActivity.class);
                    int maxSize = Math.min(getItemCount(), 1000);
                    int offset = getItemCount() - maxSize;
                    List<ChannelImage> data = new ArrayList<>(maxSize);
                    for (int i = offset; i < getItemCount(); i++) {
                        data.add(mChannelImages.get(i));
                    }
                    int position = (Integer) v.findViewById(R.id.resolution).getTag() - offset;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = (int) v.findViewById(R.id.resolution).getTag();

                    FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    final ImageDialog dialog = ImageDialog.newInstance(mChannelImages.get(position));

                    dialog.show(fm, "dialog");

                    v.onTouchEvent(
                        MotionEvent
                            .obtain(0, 0, MotionEvent.ACTION_CANCEL, v.getPivotX(), v.getPivotY(),
                                0));

                    return true;
                }
            });

            rootView.setClipToOutline(true);

            return new ChannelImageViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ChannelImageViewHolder holder, int position) {
            final ChannelImage channelImage = mChannelImages.get(position);
            float density = Resources.getSystem().getDisplayMetrics().density;
            int width = (int) (178 * density);
            int height = width
                * channelImage.getHeight()
                / channelImage.getWidth();

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image
                .getLayoutParams();
//            lp.width = width;
            lp.height = height;

            String thumbUrl = channelImage.getThumbUrl();
            String url = channelImage.getUrl();

            if (url.toLowerCase().endsWith(".gif") || thumbUrl.toLowerCase().endsWith(".gif")) {
                holder.type.setText("GIF");
                Glide.with(mContext)
                    .load(url)
                    .asBitmap()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
            } else {
                Glide.with(mContext)
                    .load(thumbUrl)
                    .asBitmap()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
            }

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.layout
                .getLayoutParams();
            layoutParams.width = width;

            holder.resolution.setText(channelImage.getWidth() + " x " + channelImage.getHeight());

            holder.resolution.setTag(position);
        }

        static class ChannelImageViewHolder extends RecyclerView.ViewHolder {

            ImageView image;

            TextView resolution;

            TextView type;

            FrameLayout layout;

            ChannelImageViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                resolution = itemView.findViewById(R.id.resolution);
                type = itemView.findViewById(R.id.type);
                layout = itemView.findViewById(R.id.tagLayout);
            }
        }
    }

}
