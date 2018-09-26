package com.charmingwong.cwimage.imagechannel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import com.bumptech.glide.Glide;
import com.charmingwong.cwimage.App;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.base.BaseActivity;
import com.charmingwong.cwimage.imagechannel.ImageChannelContract.Presenter;
import com.charmingwong.cwimage.imagelibrary.ImageLibraryActivity;
import com.charmingwong.cwimage.search.SearchActivity;
import com.charmingwong.cwimage.searchbyimage.SearchByImageActivity;
import com.charmingwong.cwimage.settings.SettingsActivity;
import com.charmingwong.cwimage.util.ActivityUtils;
import com.charmingwong.cwimage.util.ApplicationUtils;
import com.charmingwong.cwimage.wallpaper.WallpaperActivity;
import java.io.File;

public class ImageChannelActivity extends BaseActivity
    implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ImageChannelActivity";

    private static final int TAKE_PHOTO = 1;

    private static final int PICK_IMAGE = 2;

    private static final int CROP = 3;

    private long lastTime = 0;

    private long start;

    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_channel);
        init();

        start = System.currentTimeMillis();

        ApplicationUtils.isStoragePermissionGranted(this);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        final SplashFragment splashFragment = new SplashFragment();
//        ActivityUtils
//            .addFragmentToActivity(getSupportFragmentManager(), splashFragment, R.id.welcome);

//        getWindow().getDecorView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final View contentView = getLayoutInflater()
//                    .inflate(R.layout.activity_image_channel, null);
//                ((FrameLayout) findViewById(R.id.content)).addView(contentView);
//                init();
//            }
//        }, 1000);
//
//        getWindow().getDecorView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//                Animator animator = AnimatorInflater
//                    .loadAnimator(ImageChannelActivity.this, R.animator.fragment_disappearing);
//                animator.setTarget(splashFragment.getView());
//                animator.start();
//
//                animator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//                        try {
//                            FragmentTransaction ft = ImageChannelActivity.this
//                                .getSupportFragmentManager()
//                                .beginTransaction();
//                            ft.remove(splashFragment);
//                            ft.commit();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        }, 3000);
    }

    private void init() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        toggle.setDrawerSlideAnimationEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.action_camera).setOnClickListener(this);

        ImageChannelFragment fragment = (ImageChannelFragment) getSupportFragmentManager()
            .findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = ImageChannelFragment.newInstance();
            ActivityUtils
                .addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        mPresenter = new ImageChannelPresenter(fragment);
    }

    public Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: app：" + (System.currentTimeMillis() - App.start) + "   activity：" + (
            System.currentTimeMillis() - start));
    }

    private void selectWay() {
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_title))
            .setItems(R.array.select_image_way_selection, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(TAG, "onActivityResult: " + getExternalCacheDir());
                    switch (which) {
                        case 0:
                            Intent take_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            take_photo.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(
                                    new File(getExternalCacheDir() + "/soImage", "image.jpg")));
                            startActivityForResult(take_photo, TAKE_PHOTO);
                            break;
                        case 1:
                            Intent pick_image = new Intent(Intent.ACTION_PICK);
                            pick_image.setType("image/*");
                            startActivityForResult(pick_image, PICK_IMAGE);
                            break;
                        default:
                            break;
                    }
                }
            }).create().show();
    }

    private void selectSearchWay(final int requestCode, final Intent data) {
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_title))
            .setItems(R.array.select_search_way_selection, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            if (requestCode == TAKE_PHOTO) {
                                takeToCrop();
                            } else {
                                pickToCrop(data);
                            }
                            break;
                        case 1:
                            if (requestCode == TAKE_PHOTO) {
                                Intent intent = new Intent(ImageChannelActivity.this,
                                    SearchByImageActivity.class);
                                intent
                                    .putExtra("image",
                                        new File(getExternalCacheDir() + "/soImage", "image.jpg"));
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ImageChannelActivity.this,
                                    SearchByImageActivity.class);
                                intent.putExtra("image", new File(ApplicationUtils
                                    .getFilePathFromContentUri(data.getData(),
                                        getContentResolver())));
                                startActivity(intent);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }).create().show();
    }

    private void takeToCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent
            .setDataAndType(Uri.fromFile(new File(getExternalCacheDir() + "/soImage", "image.jpg")),
                "image/*");
        configCropIntent(intent);
    }

    private void pickToCrop(Intent data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data.getData(), "image/*");
        configCropIntent(intent);
    }

    private void configCropIntent(Intent intent) {
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//                    take_photo_crop.putExtra("aspectX", 1);
//                    take_photo_crop.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//                    take_photo_crop.putExtra("outputX", 1000);
//                    take_photo_crop.putExtra("outputY", 1000);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(new File(getExternalCacheDir() + "/soImage", "crop_image.jpg")));
        intent.putExtra("return-data", false);
        startActivityForResult(intent, CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                if (requestCode == TAKE_PHOTO) {
                    selectSearchWay(TAKE_PHOTO, null);
                }
            } else {
                switch (requestCode) {
                    case PICK_IMAGE:
                        selectSearchWay(PICK_IMAGE, data);
                        break;
                    case CROP:
                        Intent intent = new Intent(this, SearchByImageActivity.class);
                        intent
                            .putExtra("image",
                                new File(getExternalCacheDir() + "/soImage", "crop_image.jpg"));
                        startActivity(intent);
                        break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer == null) {
            return;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long current = System.currentTimeMillis();
            if (current - lastTime < 2000) {
                super.onBackPressed();
            } else {
                lastTime = current;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_wallpaper) {
            Intent intent = new Intent(this, WallpaperActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_channel) {

        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_library) {
            Intent intent = new Intent(this, ImageLibraryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.action_clear_memory) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(ImageChannelActivity.this).clearDiskCache();
                }
            }).start();
        } else if (id == R.id.action_more_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_camera:
                selectWay();
                break;
            default:
                break;
        }
    }
}
