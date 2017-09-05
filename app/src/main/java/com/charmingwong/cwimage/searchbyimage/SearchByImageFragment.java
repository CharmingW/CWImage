package com.charmingwong.cwimage.searchbyimage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
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
import com.charmingwong.cwimage.util.ApplicationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/23.
 */

public class SearchByImageFragment extends Fragment implements SearchByImageContract.View {

    private SearchByImageContract.Presenter mPresenter;

    private SoImagesListAdapter mSoImagesListAdapter;

    private SwipeRefreshLayout mRefreshLayout;

    private ProgressBar mProgressBar;

    private RecyclerView mImagesList;

    private TextView mErrorView;

    private boolean mIsLoading = false;

    public SearchByImageFragment() {
    }

    public static SearchByImageFragment newInstance() {
        return new SearchByImageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSoImagesListAdapter = new SoImagesListAdapter(new ArrayList<SoImage>(1));
        Bundle args = getArguments();
        File image = (File) args.getSerializable("image");
        if (image != null) {
            mPresenter.searchSoImagesByPost(image);
        } else {
            mPresenter.searchSoImagesByUrl(args.getString("imageUrl"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search_by_image, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        mImagesList = (RecyclerView) rootView.findViewById(R.id.image_list);
        mImagesList.setAdapter(mSoImagesListAdapter);
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
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int[] lastVisibleIndexes = new int[3];
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastVisibleIndexes);
                    int size = mSoImagesListAdapter.getItemCount();
                    if ((lastVisibleIndexes[0] == size - 1
                            || lastVisibleIndexes[1] == size - 1
                            || lastVisibleIndexes[2] == size - 1) && !mIsLoading) {
                        mPresenter.appendSoImages();
                        mIsLoading = true;
                    }
                }
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshSoImages();
            }
        };
        mRefreshLayout.setOnRefreshListener(refreshListener);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_by_image, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(SearchByImageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showSoImages(List<SoImage> soImages) {
        stopIfRefreshing();
        mProgressBar.setVisibility(View.INVISIBLE);
        if (mErrorView != null) {
            mErrorView.setVisibility(View.INVISIBLE);
        }
        mSoImagesListAdapter.appendImages(soImages);
    }

    @Override
    public void showAppendedSoImages(List<SoImage> soImages) {
        mSoImagesListAdapter.appendImages(soImages);
        mIsLoading = false;
    }

    @Override
    public void showRefreshedSoImages(List<SoImage> soImages) {
        mSoImagesListAdapter.replaceData(soImages);
        stopIfRefreshing();
        if (mErrorView != null) {
            mErrorView.setVisibility(View.INVISIBLE);
        }
        mIsLoading = false;
    }

    private void stopIfRefreshing() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
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
        if (mSoImagesListAdapter.getItemCount() == 0) {
            mErrorView.setVisibility(View.VISIBLE);
        }
        mIsLoading = false;
    }

    private class SoImagesListAdapter extends RecyclerView.Adapter<SoImagesListAdapter.SearchImageViewHolder> {

        private List<SoImage> mSoImages;

        SoImagesListAdapter(List<SoImage> soImages) {
            mSoImages = soImages;
        }

        void appendImages(List<SoImage> soImages) {
            int size = getItemCount();
            mSoImages.addAll(soImages);
            notifyItemInserted(size);
        }

        void replaceData(List<SoImage> soImages) {
            mSoImages.clear();
            mSoImages.addAll(soImages);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mSoImages.size();
        }

        @Override
        public SoImagesListAdapter.SearchImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View rootView = ((Activity) parent.getContext())
                    .getLayoutInflater()
                    .inflate(R.layout.item_image_search, parent, false);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                    int maxSize = Math.min(getItemCount(), 1000);
                    int offset = getItemCount() - maxSize;
                    List<SoImage> data = new ArrayList<>(maxSize);
                    for (int i = offset; i < getItemCount(); i++) {
                        data.add(mSoImages.get(i));
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
                    final ImageDialog dialog = new ImageDialog(mSoImages.get(position));

                    dialog.show(fm, "dialog");

                    v.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL,  v.getPivotX(), v.getPivotY(), 0));

                    return true;
                }
            });

            rootView.setClipToOutline(true);
            return new SoImagesListAdapter.SearchImageViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final SoImagesListAdapter.SearchImageViewHolder holder, int position) {
            final SoImage image = mSoImages.get(position);
            float density = Resources.getSystem().getDisplayMetrics().density;
            int width = (int) (178 * density);
            int height = width
                    * image.getHeight()
                    / image.getWidth();

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
//            lp.width = width;
            lp.height = height;

            String thumbUrl = image.getThumbUrl();
            String url = image.getUrl();


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

            holder.resolution.setText(image.getWidth() + " x " + image.getHeight());
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
