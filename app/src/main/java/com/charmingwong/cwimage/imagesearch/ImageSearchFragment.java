package com.charmingwong.cwimage.imagesearch;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.charmingwong.cwimage.ImageDialog;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.imagedetails.ImageDetailsActivity;
import com.charmingwong.cwimage.imagelibrary.ImageLibraryActivity;
import com.charmingwong.cwimage.util.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/14.
 */

public class ImageSearchFragment extends Fragment implements ImageSearchContract.View {

    private static final String TAG = "ImageSearchFragment";

    private ImageSearchContract.Presenter mPresenter;

    private SwipeRefreshLayout mRefreshLayout;

    private ProgressBar mProgressBar;

    private RecyclerView mImagesList;

    private SearchImagesListAdapter mSearchImagesListAdapter;

    private FloatingActionButton fab_favor;

    private String mCurrentQuery;

    private boolean mIsLoading = false;

    private TextView mErrorView;

    private static final int P_ALL = 0;

    private static final int P_480 = 1;

    private static final int P_720 = 2;

    private static final int P_1080 = 3;

    private static final int P_2K = 4;

    private static final int MODE_COMPUTER = 5;

    private static final int MODE_PHONE = 6;

    private int mCurrentMode = P_ALL;

    private int mSize;

    private List<Integer> mPositions;

    private List<QImage> mQImages;

    //要求 public empty 的构造方法
    public ImageSearchFragment() {
    }

    public static ImageSearchFragment newInstance() {
        return new ImageSearchFragment();
    }

    @Override
    public void setPresenter(@NonNull ImageSearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentQuery = getArguments().getString("query");

        setHasOptionsMenu(true);
        mQImages = new ArrayList<>(0);
        mPositions = new ArrayList<>(0);
        mSearchImagesListAdapter = new SearchImagesListAdapter();
        mPresenter.searchImages(mCurrentQuery);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_image_search, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        mImagesList = (RecyclerView) rootView.findViewById(R.id.image_list);
        mImagesList.setAdapter(mSearchImagesListAdapter);
        mImagesList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller scroller = new LinearSmoothScroller(getActivity()) {
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

        mImagesList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int density = ApplicationUtils.getDisplayDensity();
                outRect.set(density, density, density, density);
            }
        });

        mImagesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                loadMore();
            }
        });


        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshImages(mCurrentQuery);
            }
        };
        mRefreshLayout.setOnRefreshListener(refreshListener);

        fab_favor = (FloatingActionButton) getActivity().findViewById(R.id.fab_favor);
        fab_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.collect(mCurrentQuery);
            }
        });

        FloatingActionButton fab_top = (FloatingActionButton) getActivity().findViewById(R.id.fab_top);
        fab_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagesList.smoothScrollToPosition(0);
                ((AppBarLayout) getActivity().findViewById(R.id.app_bar)).setExpanded(true);
            }
        });

        return rootView;
    }

    private void loadMore() {
        if (mQImages.size() == 0) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = mImagesList.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastVisibleIndexes = new int[3];
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastVisibleIndexes);
            int size = mSize;
            if ((lastVisibleIndexes[0] == size - 1
                    || lastVisibleIndexes[1] == size - 1
                    || lastVisibleIndexes[2] == size - 1) && !mIsLoading) {
                mPresenter.appendImages(mCurrentQuery);
                mIsLoading = true;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_image_search, menu);

        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchItem.collapseActionView();

                mPresenter.searchImages(query);
                mImagesList.smoothScrollToPosition(0);

                mCurrentQuery = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.p_all:
                updateMode(P_ALL);
                return true;
            case R.id.p_480:
                updateMode(P_480);
                return true;
            case R.id.p_720:
                updateMode(P_720);
                return true;
            case R.id.p_1080:
                updateMode(P_1080);
                return true;
            case R.id.p_2K:
                updateMode(P_2K);
                return true;
            case R.id.action_computer:
                updateMode(MODE_COMPUTER);
                return true;
            case R.id.action_phone:
                updateMode(MODE_PHONE);
                return true;
            default:
                return false;
        }
    }

    private void updateMode(int mode) {
        if (mCurrentMode == mode) {
            return;
        }
        mCurrentMode = mode;
        mPositions.clear();
        mSize = count(mQImages);
        mSearchImagesListAdapter.notifyDataSetChanged();
    }

    private int count(List<QImage> QImages) {
        int count = 0;
        int previous = QImages == mQImages ? 0 : mQImages.size();
        if (mCurrentMode == P_ALL) {
            count = QImages.size();
            for (int i = 0; i < QImages.size(); i++) {
                mPositions.add(previous + i);
            }
        } else if (mCurrentMode == P_480) {
            for (int i = 0; i < QImages.size(); i++) {
                if (QImages.get(i).getWidth() == 480 || QImages.get(i).getHeight() == 480) {
                    count += 1;
                    mPositions.add(previous + i);
                }
            }
        } else if (mCurrentMode == P_720) {
            for (int i = 0; i < QImages.size(); i++) {
                if (QImages.get(i).getWidth() == 720 || QImages.get(i).getHeight() == 720) {
                    count += 1;
                    mPositions.add(previous + i);
                }
            }
        } else if (mCurrentMode == P_1080) {
            for (int i = 0; i < QImages.size(); i++) {
                if (QImages.get(i).getWidth() == 1080 || QImages.get(i).getHeight() == 1080) {
                    count += 1;
                    mPositions.add(previous + i);
                }
            }
        } else if (mCurrentMode == P_2K) {
            for (int i = 0; i < QImages.size(); i++) {
                if (QImages.get(i).getWidth() == 2560 || QImages.get(i).getHeight() == 2560) {
                    count += 1;
                    mPositions.add(previous + i);
                }
            }
        } else if (mCurrentMode == MODE_COMPUTER) {
            for (int i = 0; i < QImages.size(); i++) {
                if ((QImages.get(i).getHeight() == 768 && QImages.get(i).getWidth() == 1024)
                        || (QImages.get(i).getHeight() == 1024 && QImages.get(i).getWidth() == 1280)
                        || (QImages.get(i).getHeight() == 1200 && QImages.get(i).getWidth() == 1600)
                        || (QImages.get(i).getHeight() == 900 && QImages.get(i).getWidth() == 1440)
                        || (QImages.get(i).getHeight() == 1050 && QImages.get(i).getWidth() == 1680)
                        || (QImages.get(i).getHeight() == 1080 && QImages.get(i).getWidth() == 1920)
                        || (QImages.get(i).getHeight() == 1200 && QImages.get(i).getWidth() == 1920)
                        || (QImages.get(i).getHeight() == 1080 && QImages.get(i).getWidth() == 1920)
                        || (QImages.get(i).getHeight() == 1440 && QImages.get(i).getWidth() == 2560)
                        || (QImages.get(i).getHeight() == 1600 && QImages.get(i).getWidth() == 2560)
                        || (QImages.get(i).getHeight() == 2160 && QImages.get(i).getWidth() == 3840)) {
                    count += 1;
                    mPositions.add(previous + i);
                }
            }
        } else if (mCurrentMode == MODE_PHONE) {
            for (int i = 0; i < QImages.size(); i++) {
                if ((QImages.get(i).getHeight() == 320 && QImages.get(i).getWidth() == 240)
                        || (QImages.get(i).getHeight() == 480 && QImages.get(i).getWidth() == 320)
                        || (QImages.get(i).getHeight() == 640 && QImages.get(i).getWidth() == 480)
                        || (QImages.get(i).getHeight() == 800 && QImages.get(i).getWidth() == 600)
                        || (QImages.get(i).getHeight() == 800 && QImages.get(i).getWidth() == 480)
                        || (QImages.get(i).getHeight() == 854 && QImages.get(i).getWidth() == 480)
                        || (QImages.get(i).getHeight() == 960 && QImages.get(i).getWidth() == 540)
                        || (QImages.get(i).getHeight() == 1280 && QImages.get(i).getWidth() == 720)
                        || (QImages.get(i).getHeight() == 1920 && QImages.get(i).getWidth() == 1080)
                        || (QImages.get(i).getHeight() == 960 && QImages.get(i).getWidth() == 640)
                        || (QImages.get(i).getHeight() == 1136 && QImages.get(i).getWidth() == 640)
                        || (QImages.get(i).getHeight() == 1136 && QImages.get(i).getWidth() == 640)
                        || (QImages.get(i).getHeight() == 2560 && QImages.get(i).getWidth() == 1440)
                        || (QImages.get(i).getHeight() == 2560 && QImages.get(i).getWidth() == 1600)
                        || (QImages.get(i).getHeight() == 3840 && QImages.get(i).getWidth() == 2160)) {
                    count += 1;
                    mPositions.add(previous + i);
                }
            }
        }
        return count;
    }

    @Override
    public void showSearchedImages(List<QImage> QImages) {
        mSize = QImages.size();
        for (int i = 0; i < mSize; i++) {
            mPositions.add(i);
        }
        mProgressBar.setVisibility(View.INVISIBLE);

        mQImages.clear();
        mQImages.addAll(QImages);
        mSearchImagesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAppendedImages(List<QImage> QImages) {
        int previousSize = mSize;
        mSize += count(QImages);
        mQImages.addAll(QImages);
        mIsLoading = false;
        mSearchImagesListAdapter.notifyItemInserted(previousSize);
        if (mErrorView != null) {
            mErrorView.setVisibility(View.INVISIBLE);
        }
        if (!mImagesList.canScrollVertically(1)) {
            loadMore();
        }
    }

    @Override
    public void showRefreshedImages(List<QImage> QImages) {
        mQImages.clear();
        mPositions.clear();
        mSize = count(QImages);
        mQImages.addAll(QImages);
        mSearchImagesListAdapter.notifyDataSetChanged();
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.INVISIBLE);
        }
        mIsLoading = false;
    }

    @Override
    public void showImagesFailed() {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if (mErrorView == null) {
            mErrorView = new TextView(getActivity());
            CoordinatorLayout.LayoutParams lp
                    = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            mErrorView.setLayoutParams(lp);
            mErrorView.setText(R.string.error_network);
            ((ViewGroup) getView()).addView(mErrorView);
        }
        if (mSearchImagesListAdapter.getItemCount() == 0) {
            mErrorView.setVisibility(View.VISIBLE);
        }
        mIsLoading = false;
    }

    @Override
    public void showCollectingResult(boolean isSuccessful) {
        if (isSuccessful) {
            Snackbar snackbar = Snackbar.make(fab_favor, "收藏标签成功", Snackbar.LENGTH_SHORT);
            snackbar.setAction("查看", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageLibraryActivity.class);
                    intent.putExtra("page", 2);
                    getActivity().startActivity(intent);
                }
            });
            snackbar.show();
        } else {
            Snackbar.make(fab_favor, "收藏标签失败", Snackbar.LENGTH_SHORT).show();
        }
    }

    private class SearchImagesListAdapter extends RecyclerView.Adapter<SearchImagesListAdapter.SearchImageViewHolder> {

        @Override
        public int getItemCount() {
            return mSize;
        }

        @Override
        public SearchImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View rootView = ((Activity) parent.getContext())
                    .getLayoutInflater()
                    .inflate(R.layout.item_image_search, parent, false);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                    int maxSize = Math.min(mSize, 1000);
                    int offset = mSize - maxSize;
                    List<QImage> data = new ArrayList<>(maxSize);
                    for (int i = offset; i < mSize; i++) {
                        data.add(mQImages.get(mPositions.get(i)));
                    }
                    int position = (Integer) v.findViewById(R.id.resolution).getTag() - offset;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });

            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = (int) v.findViewById(R.id.resolution).getTag();

                    FragmentManager fm = (getActivity().getSupportFragmentManager());
                    final ImageDialog dialog = new ImageDialog(mQImages.get(position));

                    dialog.show(fm, "dialog");

                    v.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL,  v.getPivotX(), v.getPivotY(), 0));

                    return true;
                }
            });

            rootView.setClipToOutline(true);
            return new SearchImageViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final SearchImageViewHolder holder, int position) {
            final QImage QImage = mQImages.get(mPositions.get(position));
            float density = Resources.getSystem().getDisplayMetrics().density;
            int width = (int) (178 * density);
            int height = width
                    * QImage.getHeight()
                    / QImage.getWidth();

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
//            lp.width = width;
            lp.height = height;

            String thumbUrl = QImage.getThumbUrl();
            String url = QImage.getUrl();

            if (url.toLowerCase().endsWith(".gif") || thumbUrl.toLowerCase().endsWith(".gif")) {

                holder.type.setText("GIF");
                Glide.with(getActivity())
                        .load(url)
                        .asBitmap()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.image);
            } else {
                Glide.with(getActivity())
                        .load(thumbUrl)
                        .asBitmap()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.image);
            }

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.layout.getLayoutParams();
            layoutParams.width = width;

            holder.resolution.setText(QImage.getWidth() + " x " + QImage.getHeight());
            holder.resolution.setTag(position);
        }

        class SearchImageViewHolder extends RecyclerView.ViewHolder {

            ImageView image;

            TextView resolution;

            TextView type;

            FrameLayout layout;

            SearchImageViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);
                resolution = (TextView) itemView.findViewById(R.id.resolution);
                type = (TextView) itemView.findViewById(R.id.type);
                layout = (FrameLayout) itemView.findViewById(R.id.tagLayout);
            }
        }
    }

}
