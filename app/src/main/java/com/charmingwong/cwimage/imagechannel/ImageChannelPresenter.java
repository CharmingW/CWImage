package com.charmingwong.cwimage.imagechannel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.api.BaseApi;
import com.charmingwong.cwimage.imagesearch.api.QHApi;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by CharmingWong on 2017/5/14.
 */

public class ImageChannelPresenter implements ImageChannelContract.Presenter {

    private static final String TAG = "ImageChannelPresenter";

    private final ChannelApi mApi;

    private final QHApi mWelcomeApi;

    private static final int QUERY = 1;

    private static final int REFRESH = 2;

    private ImageChannelContract.View mView;

    public ImageChannelPresenter(@NonNull final ImageChannelContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mApi = ChannelApi.get();
        ChannelApi.QueryListener queryListener = new ChannelApi.QueryListener() {
            @Override
            public void onSearchCompleted(int requestCode, String channel, List<ChannelImage> channelImages) {
                if (requestCode == QUERY) {
                    mView.showChannelImages(ChannelStringToChannelNum(channel), channelImages);
                } else {
                    mView.showRefreshedChannelImages(ChannelStringToChannelNum(channel), channelImages);
                }
            }

            @Override
            public void onSearchFailed(int requestCode, int errorCode, String channel) {
                if (errorCode == ChannelApi.ERROR_JSON) {
                    if (requestCode == REFRESH) {
                        mView.showChannelImagesNull(ChannelStringToChannelNum(channel));
                    }
                } else {
                    mView.showChannelImagesFailed(ChannelStringToChannelNum(channel));
                }
            }
        };
        mApi.registerSearchListener(queryListener);

        mWelcomeApi = QHApi.newInstance();
        mWelcomeApi.setSearchFlag(1);
        mWelcomeApi.registerSearchListener(new BaseApi.QueryListener() {
            @Override
            public void onSearchCompleted(int requestCode, final List<QImage> QImages) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Activity activity = ((ImageChannelFragment) mView).getActivity();
                        if (activity == null) {
                            return;
                        }
                        try {
                            final File file = Glide.with(activity.getApplicationContext())
                                    .load(QImages.get(new Random().nextInt(QImages.size())).getUrl())
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                            if (file == null) {
                                return;
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sharedPreferences
                                            = (activity.getApplicationContext().getSharedPreferences("welcome_image_pref", Context.MODE_PRIVATE));
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("url", file.getAbsolutePath());
                                    editor.apply();
                                }
                            });
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onSearchFailed(int requestCode, int errorCode) {
                Log.i(TAG, "onSearchFailed: ");
            }
        });
    }

    @Override
    public void start() {
        mApi.query(QUERY, ChannelNumToChannelChannelString(0), 0, 0);
    }

    @Override
    public void query(int channel, int tag) {
        mApi.query(QUERY, ChannelNumToChannelChannelString(channel), tag, mView.getCurrentChannelImageLastIndex());
    }

    @Override
    public void refresh(int channel, int tag) {
        mApi.query(REFRESH, ChannelNumToChannelChannelString(channel), tag, mView.getCurrentChannelImageLastIndex());
    }

    @Override
    public void downloadWelcomeImage() {
        String[] types = ((ImageChannelFragment) mView).getResources().getStringArray(R.array.welcome_image_type);
        String type = types[new Random().nextInt(types.length)];
        mWelcomeApi.search(0, type, 0);
    }

    private String ChannelNumToChannelChannelString(int channelNum) {
        switch (channelNum) {
            case 0:
                return "beauty";
            case 1:
                return "funny";
            case 2:
                return "wallpaper";
            case 3:
                return "pet";
            case 4:
                return "car";
            case 5:
                return "go";
            case 6:
                return "food";
            case 7:
                return "art";
            case 8:
                return "photography";
            case 9:
                return "design";
            case 10:
                return "home";
            case 11:
                return "news";
            case 12:
                return "video";
            default:
                return "beauty";
        }
    }

    private int ChannelStringToChannelNum(String channelString) {
        switch (channelString) {
            case "beauty":
                return 0;
            case "funny":
                return 1;
            case "wallpaper":
                return 2;
            case "pet":
                return 3;
            case "car":
                return 4;
            case "go":
                return 5;
            case "food":
                return 6;
            case "art":
                return 7;
            case "photography":
                return 8;
            case "design":
                return 9;
            case "home":
                return 10;
            case "news":
                return 11;
            case "video":
                return 12;
            default:
                return 0;

        }
    }
}
