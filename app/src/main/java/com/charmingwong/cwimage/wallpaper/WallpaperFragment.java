package com.charmingwong.cwimage.wallpaper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.util.ApplicationUtils;
import com.charmingwong.cwimage.wallpaper.adapter.WallPaperTypesAdapter;
import com.charmingwong.cwimage.wallpaperdetails.WallpaperDetailsActivity;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CharmingWong on 2017/6/4.
 */

public class WallpaperFragment extends Fragment implements WallpaperContract.View {

    private static final String TAG = "WallpaperFragment";

    private WallpaperContract.Presenter mPresenter;

    private DropDownMenu mDropDownMenu;

    private RecyclerView mWallPaperList;

    private ProgressBar mProgressBar;

    private WallPaperListAdapter mWallPaperListAdapter;

    private List<WallpaperCover> mWallpaperCovers;

    private String tag1, tag2, tag3, tag4;

    private boolean mIsLast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWallpaperCovers = new ArrayList<>(0);
        mWallPaperListAdapter = new WallPaperListAdapter();

        initTag();
    }

    private void initTag() {
        tag1 = "全部";
        tag2 = "";
        tag3 = "最新";
        tag4 = "全部";
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.queryWallpaperCovers(tag1, tag2, tag3, tag4);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wallpaper, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_top);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWallPaperList.smoothScrollToPosition(0);
                ((AppBarLayout) getActivity().findViewById(R.id.app_bar)).setExpanded(true);
            }
        });

        mDropDownMenu = (DropDownMenu) rootView.findViewById(R.id.dropDownMenu);

        mWallPaperList = new RecyclerView(getActivity());
        mWallPaperList.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWallPaperList.setAdapter(mWallPaperListAdapter);
        mWallPaperList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
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


        mWallPaperList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (!mWallPaperList.canScrollVertically(1) && !mIsLast) {
                    mPresenter.appendWallpaperCovers();
                }
            }
        });

        mWallPaperList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int density = ApplicationUtils.getDisplayDensity();
                outRect.set(density, density, density, density);
            }

        });

        setupDropDownMenu();

        return rootView;
    }

    private void setupDropDownMenu() {

        final String[] menuItems = getActivity().getResources().getStringArray(R.array.wallpaper_primary__category);

        GridView typeList = new GridView(getActivity());
        typeList.setBackgroundColor(Color.LTGRAY);
        typeList.setNumColumns(4);
        final String[] types = getActivity().getResources().getStringArray(R.array.wallpaper_secondary_category_types);
        final WallPaperTypesAdapter typesAdapter = new WallPaperTypesAdapter(getActivity(), Arrays.asList(types));
        typeList.setAdapter(typesAdapter);
        typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tag1.equals(typesAdapter.getItem(position))) {
                    mDropDownMenu.closeMenu();
                    return;
                }
                tag1 = typesAdapter.getItem(position);
                typesAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? menuItems[0] : typesAdapter.getItem(position));
                mDropDownMenu.closeMenu();
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.queryWallpaperCovers(tag1, tag2, tag3, tag4);
                mWallPaperList.smoothScrollToPosition(0);
                mIsLast = false;
            }
        });


        final GridView sizeList = new GridView(getActivity());
        sizeList.setBackgroundColor(Color.LTGRAY);
        sizeList.setNumColumns(3);
        final String[] sizes = getActivity().getResources().getStringArray(R.array.wallpaper_secondary_category_phone_size);
        final WallPaperTypesAdapter sizeAdapter = new WallPaperTypesAdapter(getActivity(), Arrays.asList(sizes));
        sizeList.setAdapter(sizeAdapter);
        sizeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tag2.equals(sizeAdapter.getItem(position))) {
                    mDropDownMenu.closeMenu();
                    return;
                }
                if (position == 0) {
                    tag2 = "";
                } else {
                    tag2 = sizeAdapter.getItem(position);
                }
                sizeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? menuItems[1] : sizeAdapter.getItem(position));
                mDropDownMenu.closeMenu();
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.queryWallpaperCovers(tag1, tag2, tag3, tag4);
                mWallPaperList.smoothScrollToPosition(0);
                mIsLast = false;

            }
        });

        ListView orderList = new ListView(getActivity());
        orderList.setBackgroundColor(Color.LTGRAY);
        String[] orders = getActivity().getResources().getStringArray(R.array.wallpaper_secondary_category_order);
        final WallPaperTypesAdapter orderAdapter = new WallPaperTypesAdapter(getActivity(), Arrays.asList(orders));
        orderList.setAdapter(orderAdapter);
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tag3.equals(orderAdapter.getItem(position))) {
                    mDropDownMenu.closeMenu();
                    return;
                }
                tag3 = orderAdapter.getItem(position);
                orderAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? menuItems[2] : orderAdapter.getItem(position));
                mDropDownMenu.closeMenu();
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.queryWallpaperCovers(tag1, tag2, tag3, tag4);
                mWallPaperList.smoothScrollToPosition(0);
                mIsLast = false;

            }
        });

        ListView orientationList = new ListView(getActivity());
        orientationList.setBackgroundColor(Color.LTGRAY);
        final String[] orientations = getActivity().getResources().getStringArray(R.array.wallpaper_secondary_category_landscape);
        final WallPaperTypesAdapter orientationAdapter = new WallPaperTypesAdapter(getActivity(), Arrays.asList(orientations));
        orientationList.setAdapter(orientationAdapter);
        orientationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (tag4.equals(orientationAdapter.getItem(position))) {
                    mDropDownMenu.closeMenu();
                    return;
                }

                if (position == orientations.length - 1) {
                    String[] sizes = getActivity().getResources().getStringArray(R.array.wallpaper_secondary_category_desktop_size);
                    sizeAdapter.setTypes(Arrays.asList(sizes));
                    sizeAdapter.setCheckItem(0);
                    tag2 = "";
                } else {
                    if (tag4.equals(orientationAdapter.getItem(orientations.length - 1))) {
                        sizeAdapter.setTypes(Arrays.asList(sizes));
                        sizeAdapter.setCheckItem(0);
                        tag2 = "";
                    }
                }

                tag4 = orientationAdapter.getItem(position);
                orientationAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? menuItems[3] : orientationAdapter.getItem(position));
                mDropDownMenu.closeMenu();
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.queryWallpaperCovers(tag1, tag2, tag3, tag4);
                mWallPaperList.smoothScrollToPosition(0);
                mIsLast = false;
            }
        });

        List<View> menuViews = new ArrayList<>(4);
        menuViews.add(typeList);
        menuViews.add(sizeList);
        menuViews.add(orderList);
        menuViews.add(orientationList);

        mDropDownMenu.setDropDownMenu(Arrays.asList(menuItems), menuViews, mWallPaperList);
    }

    @Override
    public void setPresenter(WallpaperContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showQueryWallpaperCovers(List<WallpaperCover> wallpaperCovers) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mWallpaperCovers = wallpaperCovers;
        mWallPaperListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAppendedWallpaperCovers(List<WallpaperCover> wallpaperCovers) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (wallpaperCovers.size() == 0) {
            return;
        }
        int lastSize = mWallpaperCovers.size();
        if (!wallpaperCovers.get(wallpaperCovers.size() - 1).equals(mWallpaperCovers.get(mWallpaperCovers.size() - 1))) {
            mWallpaperCovers.addAll(wallpaperCovers);
            mWallPaperListAdapter.notifyItemInserted(lastSize);
        } else {
            mIsLast = true;
        }

    }

    private class WallPaperListAdapter extends RecyclerView.Adapter<WallPaperListAdapter.WallPaperViewHolder> {

        @Override
        public int getItemCount() {
            return mWallpaperCovers.size();
        }

        @Override
        public WallPaperListAdapter.WallPaperViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            final View rootView = ((Activity) parent.getContext())
                    .getLayoutInflater()
                    .inflate(R.layout.item_wallpaper_cover, parent, false);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WallpaperDetailsActivity.class);
                    Bundle bundle = new Bundle();

                    int position = (int) v.findViewById(R.id.cover).getTag(R.id.cover);
                    WallpaperCover wallpaperCover = mWallpaperCovers.get(position);

                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(wallpaperCover.getTitle());
                    if (matcher.find()) {
                        bundle.putInt("count", Integer.parseInt(matcher.group()));
                    }
                    bundle.putString("detailsUrl", wallpaperCover.getDetailsUrl());
                    if ("全部".equals(tag2)) {
                        bundle.putString("size", "");
                    } else {
                        bundle.putString("size", tag2);
                    }
                    if (!"电脑壁纸".equals(tag4)) {
                        bundle.putInt("type", 0);
                    } else {
                        bundle.putInt("type", 1);
                    }
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });

            rootView.setClipToOutline(true);
            return new WallPaperListAdapter.WallPaperViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final WallPaperListAdapter.WallPaperViewHolder holder, int position) {

            WallpaperCover cover = mWallpaperCovers.get(position);

            float density = Resources.getSystem().getDisplayMetrics().density;
            int width = (int) (178 * density);
            int height = width
                    * cover.getHeight()
                    / cover.getWidth();

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.cover.getLayoutParams();
//            lp.width = width;
            lp.height = height;

            Glide.with(getActivity())
                    .load(cover.getImageUrl())
                    .asBitmap()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.cover);

            holder.title.setText(cover.getTitle());

            holder.cover.setTag(R.id.cover, position);
        }

        class WallPaperViewHolder extends RecyclerView.ViewHolder {

            ImageView cover;

            TextView title;

            WallPaperViewHolder(View itemView) {
                super(itemView);
                cover = (ImageView) itemView.findViewById(R.id.cover);
                title = (TextView) itemView.findViewById(R.id.title);
            }
        }
    }


}
