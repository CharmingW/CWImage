package com.charmingwong.cwimage.imagelibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.charmingwong.cwimage.base.BaseModel;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.imagedetails.ImageDetailsActivity;
import com.charmingwong.cwimage.imagesearch.ImageSearchActivity;
import com.charmingwong.cwimage.util.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/27.
 */

public class ImageLibraryFragment extends Fragment implements ImageLibraryContract.View, ImageLibraryActivity.ActivityActionListener {

    private static final String[] LIBRARY_TYPES = {"下载图片", "收藏图片", "收藏标签"};

    private static final int TYPE_DOWNLOAD = 0;

    private static final int TYPE_COLLECTION = 1;

    private static final int TYPE_TAG = 2;

    private static final String TAG = "ImageLibraryFragment";

    private ViewPager mViewPager;

    private FloatingActionButton mFabDelete;

    private ImageLibraryAdapter mImageLibraryAdapter;

    private ImageLibraryContract.Presenter mPresenter;

    private List<BaseModel> mDownloadImages;

    private List<BaseModel> mCollectionImages;

    private List<CollectionTag> mCollectionTags;

    private boolean mCanGoBack = false;

    public ImageLibraryFragment() {

    }

    public static ImageLibraryFragment newInstance() {
        return new ImageLibraryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ImageLibraryActivity) getActivity()).setActivityActionListener(this);
        mImageLibraryAdapter = new ImageLibraryAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_image_library, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.libraryPager);
        mViewPager.setAdapter(mImageLibraryAdapter);
        mViewPager.setOffscreenPageLimit(2);
        int current = getArguments().getInt("page", 0);
        mViewPager.setCurrentItem(current);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isDeleting()) {
                    mFabDelete.setVisibility(View.VISIBLE);
                } else {
                    mFabDelete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        mFabDelete = (FloatingActionButton) getActivity().findViewById(R.id.fab_delete);
        mFabDelete.setVisibility(View.GONE);
        mFabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Integer> indexes = new ArrayList<>();

                int current = mViewPager.getCurrentItem();
                RecyclerView list = ((RecyclerView) mViewPager.findViewWithTag(current).findViewById(R.id.dataList));
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) list.getLayoutManager();
                for (int i = 0; i < layoutManager.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) layoutManager.getChildAt(i).findViewById(R.id.checkbox);
                    if (checkBox.isChecked()) {
                        indexes.add(i);
                    }
                    checkBox.setChecked(false);
                    checkBox.setVisibility(View.INVISIBLE);
                }

                mFabDelete.setVisibility(View.INVISIBLE);
                list.setTag(R.id.dataList, false);

                if (current == TYPE_TAG) {
                    TagsListAdapter adapter = (TagsListAdapter) list.getAdapter();
                    for (int i = 0; i < adapter.mCheckeds.size(); i++) {
                        if (adapter.mCheckeds.get(i)) {
                            indexes.add(i);
                        }
                        adapter.mCheckeds.set(i, false);
                    }
                } else {
                    ImagesListAdapter adapter = (ImagesListAdapter) list.getAdapter();
                    for (int i = 0; i < adapter.mCheckeds.size(); i++) {
                        if (adapter.mCheckeds.get(i)) {
                            indexes.add(i);
                        }
                        adapter.mCheckeds.set(i, false);
                    }
                }

                if (indexes.size() == 0 || mCanGoBack) {
                    mCanGoBack = false;
                    return;
                }

                if (current == TYPE_DOWNLOAD) {
                    mPresenter.deleteDownloadImages(indexes);
                    mPresenter.loadDownloadImage();
                } else if (current == TYPE_COLLECTION) {
                    mPresenter.deleteCollectionImages(indexes);
                    mPresenter.loadCollectionImage();
                } else {
                    mPresenter.deleteCollectionTags(indexes);
                    mPresenter.loadCollectionTags();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ImageLibraryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getInitialData(List<BaseModel> l1, List<BaseModel> l2, List<CollectionTag> l3) {
        mDownloadImages = l1;
        mCollectionImages = l2;
        mCollectionTags = l3;

    }

    @Override
    public void showDownloads(List<BaseModel> baseModels) {
        ImagesListAdapter adapter = (ImagesListAdapter) (((RecyclerView) mViewPager.findViewWithTag(mViewPager.getCurrentItem()).findViewById(R.id.dataList)).getAdapter());
        adapter.setBaseModels(baseModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showCollection(List<BaseModel> baseModels) {
        ImagesListAdapter adapter = (ImagesListAdapter) (((RecyclerView) mViewPager.findViewWithTag(mViewPager.getCurrentItem()).findViewById(R.id.dataList)).getAdapter());
        adapter.setBaseModels(baseModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showCollectedTags(List<CollectionTag> tags) {
        TagsListAdapter adapter = (TagsListAdapter) (((RecyclerView) mViewPager.findViewWithTag(mViewPager.getCurrentItem()).findViewById(R.id.dataList)).getAdapter());
        adapter.setCollectionTags(tags);
        adapter.notifyDataSetChanged();
    }

    private class ImageLibraryAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final View rootView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_library_pager, null);
            final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.dataList);
            recyclerView.setTag(R.id.dataList, false);

            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    if (recyclerView.getChildCount() <= 2) {
                        return;
                    }

                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    int[] f = new int[2];
                    layoutManager.findFirstVisibleItemPositions(f);
                    int[] l = new int[2];
                    layoutManager.findLastVisibleItemPositions(l);


                    CheckBox view1 = (CheckBox) layoutManager.findViewByPosition(f[0]).findViewById(R.id.checkbox);
                    if (view1 != null) {
                        if (isDeleting()) {
                            view1.setVisibility(View.VISIBLE);
                        } else {
                            view1.setVisibility(View.INVISIBLE);
                            view1.setChecked(false);
                        }
                    }
                    CheckBox view2 = (CheckBox) layoutManager.findViewByPosition(f[1]).findViewById(R.id.checkbox);
                    if (view1 != null) {
                        if (isDeleting()) {
                            view2.setVisibility(View.VISIBLE);
                        } else {
                            view2.setVisibility(View.INVISIBLE);
                            view1.setChecked(false);
                        }
                    }
                    CheckBox view3 = (CheckBox) layoutManager.findViewByPosition(l[0]).findViewById(R.id.checkbox);
                    if (view1 != null) {
                        if (isDeleting()) {
                            view3.setVisibility(View.VISIBLE);
                        } else {
                            view3.setVisibility(View.INVISIBLE);
                            view1.setChecked(false);
                        }
                    }
                    CheckBox view4 = (CheckBox) layoutManager.findViewByPosition(l[1]).findViewById(R.id.checkbox);
                    if (view1 != null) {
                        if (isDeleting()) {
                            view4.setVisibility(View.VISIBLE);
                        } else {
                            view4.setVisibility(View.INVISIBLE);
                            view1.setChecked(false);
                        }
                    }
                }
            });

            if (position == TYPE_TAG) {
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        int density = ApplicationUtils.getDisplayDensity();
                        outRect.set(4 * density, 4 * density, 4 * density, 4 * density);
                    }
                });
            } else {
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        int density = ApplicationUtils.getDisplayDensity();
                        outRect.set(density, density, density, density);
                    }
                });
            }

            if (position == TYPE_DOWNLOAD || position == TYPE_COLLECTION) {
                ImagesListAdapter adapter;
                if (position == TYPE_DOWNLOAD) {
                    adapter = new ImagesListAdapter(mDownloadImages);
                } else {
                    adapter = new ImagesListAdapter(mCollectionImages);
                }
                recyclerView.setAdapter(adapter);
            } else {
                TagsListAdapter adapter = new TagsListAdapter(mCollectionTags);
                recyclerView.setAdapter(adapter);
            }

            rootView.setTag(position);
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return LIBRARY_TYPES[position];
        }
    }

    private class ImagesListAdapter extends RecyclerView.Adapter<ImagesListAdapter.ImagesViewHolder> {

        private List<BaseModel> mBaseModels;

        private List<Boolean> mCheckeds;

        public ImagesListAdapter(List<BaseModel> baseModels) {
            mBaseModels = baseModels;
            initCheckeds();
        }

        public void setBaseModels(List<BaseModel> baseModels) {
            mBaseModels = baseModels;
            initCheckeds();
        }

        private void initCheckeds() {
            mCheckeds = new ArrayList<>(mBaseModels.size());
            for (int i = 0; i < mBaseModels.size(); i++) {
                mCheckeds.add(false);
            }
        }

        @Override
        public int getItemCount() {
            if (mBaseModels != null) {
                return mBaseModels.size();
            } else {
                return 0;
            }
        }

        @Override
        public ImagesViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View rootView;
            rootView = ((Activity) parent.getContext())
                    .getLayoutInflater()
                    .inflate(R.layout.item_image_library_image, parent, false);

            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    boolean isDeleting = isDeleting();
                    if (!isDeleting) {
                        mFabDelete.setVisibility(View.VISIBLE);
                        parent.setTag(R.id.dataList, true);
                        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
                        for (int i = 0; i < layoutManager.getChildCount(); i++) {
                            CheckBox box = (CheckBox) layoutManager.getChildAt(i).findViewById(R.id.checkbox);
                            box.setVisibility(View.VISIBLE);
                            box.setChecked(false);
                        }
                        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
                        checkBox.setChecked(true);
                        int position = (int) checkBox.getTag(R.id.checkbox);
                        mCheckeds.set(position, true);
                    }
                    return true;
                }
            });

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = ((CheckBox) v.findViewById(R.id.checkbox));
                    int position = (int) checkBox.getTag(R.id.checkbox);
                    if (isDeleting()) {
                        checkBox.setChecked(!checkBox.isChecked());
                        mCheckeds.set(position, checkBox.isChecked());
                    } else {
                        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) mBaseModels);
                        bundle.putInt("position", position);
                        bundle.putInt("from", ImageLibraryActivity.FROM_TAG);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                    }
                }
            });

            rootView.setClipToOutline(true);
            return new ImagesViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final ImagesViewHolder holder, final int position) {

            BaseModel image = mBaseModels.get(position);

            float density = Resources.getSystem().getDisplayMetrics().density;
            int width = (int) (178 * density);
            int height = width
                    * image.getHeight()
                    / image.getWidth();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) holder.image.getLayoutParams();
//            lp.width = width;
            lp.height = height;

            String url = image.getUrl();

            Glide.with(getActivity())
                    .load(url)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);

            holder.resolution.setText(image.getWidth() + " x " + image.getHeight());
            if (isDeleting()) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(mCheckeds.get(position));
            }
            holder.checkBox.setTag(R.id.checkbox, position);
        }

        class ImagesViewHolder extends RecyclerView.ViewHolder {

            ImageView image;

            TextView resolution;

            CheckBox checkBox;

            ImagesViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);
                resolution = (TextView) itemView.findViewById(R.id.resolution);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            }
        }

    }

    private class TagsListAdapter extends RecyclerView.Adapter<TagsListAdapter.TagsViewHolder> {

        private List<CollectionTag> mCollectionTags;

        private List<Boolean> mCheckeds;

        public TagsListAdapter(List<CollectionTag> collectionTags) {
            mCollectionTags = collectionTags;
            initCheckeds();
        }

        private void initCheckeds() {
            mCheckeds = new ArrayList<>(mCollectionTags.size());
            for (int i = 0; i < mCollectionTags.size(); i++) {
                mCheckeds.add(false);
            }
        }

        public void setCollectionTags(List<CollectionTag> collectionTags) {
            mCollectionTags = collectionTags;
            initCheckeds();
        }

        @Override
        public int getItemCount() {
            if (mCollectionTags != null) {
                return mCollectionTags.size();
            } else {
                return 0;
            }
        }

        @Override
        public TagsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            final View rootView;
            rootView = getActivity()
                    .getLayoutInflater()
                    .inflate(R.layout.item_image_library_tag, parent, false);

            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    boolean isDeleting = isDeleting();
                    if (!isDeleting) {
                        mFabDelete.setVisibility(View.VISIBLE);
                        parent.setTag(R.id.dataList, true);
                        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
                        for (int i = 0; i < layoutManager.getChildCount(); i++) {
                            CheckBox box = (CheckBox) layoutManager.getChildAt(i).findViewById(R.id.checkbox);
                            box.setChecked(false);
                            box.setVisibility(View.VISIBLE);
                        }
                        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
                        checkBox.setChecked(true);
                        int position = (int) checkBox.getTag(R.id.checkbox);
                        mCheckeds.set(position, true);
                    }
                    return true;
                }
            });

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isDeleting = isDeleting();
                    if (isDeleting) {
                        CheckBox checkBox = ((CheckBox) v.findViewById(R.id.checkbox));
                        checkBox.setChecked(!checkBox.isChecked());
                        int position = (int) checkBox.getTag(R.id.checkbox);
                        mCheckeds.set(position, checkBox.isChecked());
                    } else {
                        Intent intent = new Intent(getActivity(), ImageSearchActivity.class);
                        intent.putExtra("query", ((String) rootView.findViewById(R.id.tag).getTag(R.id.tag)));
                        startActivity(intent);
                    }
                }
            });
            return new TagsViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final TagsViewHolder holder, final int position) {
            String tag = mCollectionTags.get(position).getTag();
            holder.tag.setText(tag);
            holder.tag.setTag(R.id.tag, tag);

            if (isDeleting()) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(mCheckeds.get(position));
            }

            holder.checkBox.setTag(R.id.checkbox, position);
        }

        class TagsViewHolder extends RecyclerView.ViewHolder {

            TextView tag;

            CheckBox checkBox;

            TagsViewHolder(View itemView) {
                super(itemView);
                tag = (TextView) itemView.findViewById(R.id.tag);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            }
        }
    }

    @Override
    public boolean canGoBack() {
        if (isDeleting()) {
            mCanGoBack = true;
            mFabDelete.callOnClick();
            return true;
        }
        return false;
    }

    private boolean isDeleting() {
        return (boolean) mViewPager.findViewWithTag(mViewPager.getCurrentItem()).findViewById(R.id.dataList).getTag(R.id.dataList);
    }
}
