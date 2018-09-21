package com.charmingwong.cwimage.wallpaperdetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.imagelibrary.ImageLibraryActivity;
import com.charmingwong.cwimage.searchbyimage.SearchByImageActivity;
import com.charmingwong.cwimage.util.ApplicationUtils;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by CharmingWong on 2017/6/8.
 */

public class WallpaperDetailsFragment extends Fragment implements WallpaperDetailsContact.View {

    private static final String TAG = "WallpaperDetailsFragment";

    private WallpaperDetailsContact.Presenter mPresenter;

    private WallPaperDetailsAdapter mWallPaperDetailsAdapter;

    private ViewPager mViewPager;

    private FloatingActionMenu mFloatingActionMenu;

    private FloatingActionButton mFloatingActionButton;

    private TextView mPageIndicator;

    private SparseArray<Wallpaper> mWallpapers;

    private int mCount;

    public WallpaperDetailsFragment() {
    }

    public static WallpaperDetailsFragment newInstance() {
        return new WallpaperDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWallPaperDetailsAdapter = new WallPaperDetailsAdapter();
        mWallpapers = new SparseArray<>(mCount);

        mPresenter.loadStartUrl(getArguments().getString("detailsUrl"));
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_image_details, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(0);

        mPageIndicator = (TextView) rootView.findViewById(R.id.page_indicator);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPageIndicator.setText((position + 1) + "/" + mCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setUpFloatingActionMenu();

        return rootView;
    }

    private void setUpFloatingActionMenu() {
        ImageView icon = new ImageView(getActivity());
        icon.setImageResource(R.drawable.ic_menu_white_24dp);
        mFloatingActionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .setTheme(FloatingActionButton.THEME_DARK)
                .build();

        ImageView download = new ImageView(getActivity());
        download.setImageResource(R.drawable.ic_file_download_white_24dp);
        SubActionButton downloadButton = new SubActionButton.Builder(getActivity())
                .setContentView(download)
                .setTheme(SubActionButton.THEME_DARKER)
                .build();
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                boolean isGranted = ApplicationUtils.isStoragePermissionGranted(getActivity());
                if (!isGranted) {
                    Snackbar.make(v, "请先开启读写权限", Snackbar.LENGTH_SHORT)
                            .setAction("前往", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ApplicationUtils.getAppDetailSettingIntent(getActivity());
                                }
                            })
                            .setActionTextColor(getActivity().getResources().getColor(R.color.colorPrimary))
                            .show();
                    return;
                }
                mPresenter.downloadImage(mWallpapers.get(mViewPager.getCurrentItem()));
                mFloatingActionMenu.close(true);
            }
        });

        ImageView collect = new ImageView(getActivity());
        collect.setImageResource(R.drawable.ic_star_white_24dp);
        SubActionButton collectButton = new SubActionButton.Builder(getActivity())
                .setContentView(collect)
                .setTheme(SubActionButton.THEME_DARKER)
                .build();
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                boolean isGranted = ApplicationUtils.isStoragePermissionGranted(getActivity());
                if (!isGranted) {
                    Snackbar.make(v, "请先开启读写权限", Snackbar.LENGTH_SHORT)
                            .setAction("前往", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ApplicationUtils.getAppDetailSettingIntent(getActivity());
                                }
                            })
                            .setActionTextColor(getActivity().getResources().getColor(R.color.colorPrimary))
                            .show();
                    return;
                }
                mPresenter.collectImage(mWallpapers.get(mViewPager.getCurrentItem()));
                mFloatingActionMenu.close(true);
            }
        });

        ImageView share = new ImageView(getActivity());
        share.setImageResource(R.drawable.ic_share_white_24dp);
        SubActionButton shareButton = new SubActionButton.Builder(getActivity())
                .setContentView(share)
                .setTheme(SubActionButton.THEME_DARKER)
                .build();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isGranted = ApplicationUtils.isStoragePermissionGranted(getActivity());
                if (!isGranted) {
                    Snackbar.make(v, "请先开启读写权限", Snackbar.LENGTH_SHORT)
                            .setAction("前往", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ApplicationUtils.getAppDetailSettingIntent(getActivity());
                                }
                            })
                            .setActionTextColor(getActivity().getResources().getColor(R.color.colorPrimary))
                            .show();
                    return;
                }
                mPresenter.prepareShareImage(mWallpapers.get(mViewPager.getCurrentItem()));
                mFloatingActionMenu.close(true);
            }
        });

        ImageView search = new ImageView(getActivity());
        search.setImageResource(R.drawable.ic_search_white_24dp);
        SubActionButton searchButton = new SubActionButton.Builder(getActivity())
                .setContentView(search)
                .setTheme(SubActionButton.THEME_DARKER)
                .build();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchByImageActivity.class);
                String url = mWallpapers.get(mViewPager.getCurrentItem()).getThumbUrl();
                intent.putExtra("imageUrl", url);
                getActivity().startActivity(intent);
                mFloatingActionMenu.close(true);
            }
        });

        TextView setWp = new TextView(getActivity());
        setWp.setText("设为壁纸");
        setWp.setGravity(Gravity.CENTER);
        setWp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        setWp.setBackground(null);
        setWp.setTextColor(Color.WHITE);
        SubActionButton setWpButton = new SubActionButton.Builder(getActivity())
                .setContentView(setWp)
                .setTheme(SubActionButton.THEME_DARKER)
                .build();
        setWpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setAsWallPager(mWallpapers.get(mViewPager.getCurrentItem()));
                mFloatingActionMenu.close(true);
            }
        });
        mFloatingActionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(downloadButton)
                .addSubActionView(collectButton)
                .addSubActionView(shareButton)
                .addSubActionView(searchButton)
                .addSubActionView(setWpButton)
                .setStartAngle(-190)
                .setEndAngle(-80)
                .attachTo(mFloatingActionButton)
                .build();

        mFloatingActionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {

            }
        });
    }

    @Override
    public void setPresenter(WallpaperDetailsContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void show(int count) {
        mCount = count;
        mPageIndicator.setText(1 + "/" + mCount);
        mViewPager.setAdapter(mWallPaperDetailsAdapter);
    }


    @Override
    public void showWallPaperDetails(int position, Wallpaper wallpaper) {
        mWallpapers.put(position, wallpaper);
        View view = mViewPager.findViewWithTag(position);
        if (view != null) {
            ImageView photoView = (ImageView) view.findViewById(R.id.photo_view);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            String url = wallpaper.getUrl();
            RequestListener<String, Bitmap> listener = new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            };
            Glide.with(getActivity())
                    .load(url)
                    .asBitmap()
                    .crossFade()
                    .listener(listener)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(photoView);
        }
    }

    @Override
    public void showDownloadResult(File file) {
        if (file != null) {
            Snackbar snackbar = Snackbar.make(mFloatingActionButton, "成功保存至 " + file.getAbsolutePath(), Snackbar.LENGTH_SHORT);
            snackbar.setAction("查看", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageLibraryActivity.class);
                    intent.putExtra("page", 0);
                    getActivity().startActivity(intent);
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        } else {
            Snackbar.make(mFloatingActionButton, "保存失败", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shareImage(Uri uri) {
        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("image/*");
            getActivity().startActivity(Intent.createChooser(intent, "分享图片到"));
        } else {
            Snackbar.make(mFloatingActionButton, "分享失败", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showWallPagerResult(boolean isSuccessful) {
        if (isSuccessful) {
            mFloatingActionMenu.close(true);
            Snackbar.make(mFloatingActionButton, "设置壁纸成功", Snackbar.LENGTH_SHORT)
                    .setAction("返回桌面", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApplicationUtils.backToHome(getActivity());
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                    .show();
        }
    }

    @Override
    public void showCollectResult() {
        Snackbar.make(mFloatingActionButton, "收藏图片成功", Snackbar.LENGTH_SHORT)
                .setAction("查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ImageLibraryActivity.class);
                        intent.putExtra("page", 1);
                        getActivity().startActivity(intent);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    private class WallPaperDetailsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.item_image_details, container, false);

            final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            final PhotoView photoView = (PhotoView) rootView.findViewById(R.id.photo_view);
            final Wallpaper wallpaper = mWallpapers.get(position);

            if (wallpaper == null) {
                mPresenter.loadWallPaperUrl(position);
            } else {
                String url = wallpaper.getUrl();
                RequestListener<String, GlideDrawable> listener = new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        rootView.setTag(true);
                        return false;
                    }
                };
                Glide.with(getActivity())
                        .load(url)
                        .listener(listener)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(photoView);
            }

            rootView.setTag(position);
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
