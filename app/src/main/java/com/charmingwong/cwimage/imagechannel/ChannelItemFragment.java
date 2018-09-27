package com.charmingwong.cwimage.imagechannel;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.util.ApplicationUtils;


/**
 * Created by huangchaoming on 2018/9/27
 */
public class ChannelItemFragment extends Fragment {

    private static final String TAG = "ChannelItemFragment";

    private ChannelImageListAdapter mChannelImageListAdapter;

    private ChangeChannelImagesCountListener mListener;

    public static ChannelItemFragment newInstance(int position, ChangeChannelImagesCountListener listener) {
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
        ImageChannelContract.Presenter presenter = ((ImageChannelActivity) getActivity()).getPresenter();
        presenter.query(position, initTagByPosition(position));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        final View rootView = inflater.inflate(R.layout.fragment_image_channel_item, container, false);
        RecyclerView channelImageList = rootView.findViewById(R.id.channelImageList);
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
                        if (mListener != null) {
                            mListener.updateChannelImagesCount();
                        }
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

    interface ChangeChannelImagesCountListener {
        void updateChannelImagesCount();
    }
}