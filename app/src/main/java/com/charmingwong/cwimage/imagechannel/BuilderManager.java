package com.charmingwong.cwimage.imagechannel;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.util.ApplicationUtils;
import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Util;

/**
 * Created by CharmingWong on 2017/6/6.
 */

public class BuilderManager {

    private View parent;

    private int textColor;

    private int textSize;

    private int radius;

    private Rect imageRect;

    private BoomMenuButton mBoomMenuButton;

    public void showBoomMenuButton(boolean show) {
        if (show) {
            mBoomMenuButton.setVisibility(View.VISIBLE);
        } else {
            mBoomMenuButton.setVisibility(View.INVISIBLE);
        }
    }

    private BoomMenuButtonClickListener mBoomMenuButtonClickListener;

    public BuilderManager(View parent) {
        this.parent = parent;
        float density = ApplicationUtils.getDisplayDensity();
        textSize = (int) (20 * density);
        radius = (int) (50 * density);
        imageRect = new Rect(0, 0, radius * 2, radius * 2);
        textColor = parent.getResources().getColor(R.color.boom_menu_text);
        mBoomMenuButton = (BoomMenuButton) parent.findViewById(R.id.channel_bmb);
        mBoomMenuButton.setInFragment(true);
        mBoomMenuButton.setNormalColor(parent.getResources().getColor(R.color.colorPrimaryDark));
        mBoomMenuButton.setShowRotateEaseEnum(EaseEnum.EaseInOutCirc);
        mBoomMenuButton.setShowRotateEaseEnum(EaseEnum.EaseInOutCirc);
        mBoomMenuButton.setButtonEnum(ButtonEnum.SimpleCircle);
    }

    public void setupBoomMenu(int channel) {
        if (channel == 0) {
            setupBeautyMenu(channel);
        } else if (channel == 1) {
            setupFunMenu(channel);
        } else if (channel == 2) {
            setupWallPaperMenu(channel);
        } else if (channel == 3) {
            setupPetMenu(channel);
        } else if (channel == 4) {
            setupCarMenu(channel);
        } else if (channel == 5) {
            setupGoMenu(channel);
        } else if (channel == 6) {
            setupFoodMenu(channel);
        } else if (channel == 7) {
            setupArtMenu(channel);
        } else if (channel == 8) {
            setupPhotographMenu(channel);
        } else if (channel == 9) {
            setupDesignMenu(channel);
        } else if (channel == 10) {
            setupHomeMenu(channel);
        } else if (channel == 11) {
            setupNewsMenu(channel);
        } else if (channel == 12) {
            setupVideoMenu(channel);
        }

    }

    private void initBoomMenuButton() {
        mBoomMenuButton.clearBuilders();
        mBoomMenuButton.getCustomButtonPlacePositions().clear();
        mBoomMenuButton.getCustomPiecePlacePositions().clear();
    }

    private void setupBeautyMenu(final int channel) {
        initBoomMenuButton();
        final String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)
                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 599;
                        } else if (i == 1) {
                            tag = 595;
                        } else if (i == 2) {
                            tag = 625;
                        } else if (i == 3) {
                            tag = 600;
                        } else if (i == 4) {
                            tag = 603;
                        } else if (i == 5) {
                            tag = 596;
                        } else if (i == 6) {
                            tag = 2006;
                        } else if (i == 7) {
                            tag = 2007;
                        } else if (i == 8) {
                            tag = 598;
                        } else if (i == 9) {
                            tag = 604;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h * 0.5f;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm * 0.5f;
        float vm_1_5 = vm * 1.5f;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_1_5 + vm_1_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_1_5 + vm_1_5));
    }

    private void setupFunMenu(int channel) {
        initBoomMenuButton();

        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)
                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 1993;
                        } else if (i == 1) {
                            tag = 1983;
                        } else if (i == 2) {
                            tag = 1991;
                        } else if (i == 3) {
                            tag = 1989;
                        } else if (i == 4) {
                            tag = 1984;
                        } else if (i == 5) {
                            tag = 1990;
                        } else if (i == 6) {
                            tag = 1992;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        int offset = Util.dp2px(5);

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 + offset));


    }

    private void setupWallPaperMenu(int channel) {
        initBoomMenuButton();

        String[] names = getChannelClassify(channel);
        for (int i = 0; i < 18; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)
                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 92;
                        } else if (i == 1) {
                            tag = 93;
                        } else if (i == 2) {
                            tag = 156;
                        } else if (i == 3) {
                            tag = 157;
                        } else if (i == 4) {
                            tag = 158;
                        } else if (i == 5) {
                            tag = 159;
                        } else if (i == 6) {
                            tag = 160;
                        } else if (i == 7) {
                            tag = 161;
                        } else if (i == 8) {
                            tag = 162;
                        } else if (i == 9) {
                            tag = 163;
                        } else if (i == 10) {
                            tag = 164;
                        } else if (i == 11) {
                            tag = 165;
                        } else if (i == 12) {
                            tag = 166;
                        } else if (i == 13) {
                            tag = 167;
                        } else if (i == 14) {
                            tag = 168;
                        } else if (i == 15) {
                            tag = 169;
                        } else if (i == 16) {
                            tag = 170;
                        } else if (i == 17) {
                            tag = 171;
                        } else if (i == 18) {
                            tag = 172;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(56);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;
        float h_2_5 = h * 2.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;
        float vm_2_5 = vm * 2.5f;

        float offset = Util.dp2px(10);

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_2_5 - vm_2_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

//        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, 0));
//        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, 0));
//        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, 0));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_2_5 + vm_2_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_2_5 + vm_2_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_2_5 + vm_2_5 - offset));

//        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_1_5 + vm_1_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;
        h_2_5 = h_2_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;
        vm_2_5 = vm_2_5 / times;

        offset = offset / times;

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_2_5 - vm_2_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_2_5 + vm_2_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_2_5 + vm_2_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_2_5 + vm_2_5 - offset));


    }

    private void setupPetMenu(int channel) {
        initBoomMenuButton();

        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 234;
                        } else if (i == 1) {
                            tag = 235;
                        } else if (i == 2) {
                            tag = 236;
                        } else if (i == 3) {
                            tag = 237;
                        } else if (i == 4) {
                            tag = 238;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w / 2 - hm / 2, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w / 2 + hm / 2, +h_0_5 + vm_0_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w / 2 - hm / 2, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w / 2 + hm / 2, +h_0_5 + vm_0_5));


    }

    private void setupCarMenu(int channel) {
        initBoomMenuButton();

        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 208;
                        } else if (i == 1) {
                            tag = 209;
                        } else if (i == 2) {
                            tag = 210;
                        } else if (i == 3) {
                            tag = 211;
                        } else if (i == 4) {
                            tag = 212;
                        } else if (i == 5) {
                            tag = 213;
                        } else if (i == 6) {
                            tag = 214;
                        } else if (i == 7) {
                            tag = 215;
                        } else if (i == 8) {
                            tag = 216;
                        } else if (i == 9) {
                            tag = 217;
                        } else if (i == 10) {
                            tag = 218;
                        } else if (i == 11) {
                            tag = 219;
                        } else if (i == 12) {
                            tag = 220;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(60);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;
        float h_2_5 = h * 2.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;
        float vm_2_5 = vm * 2.5f;

        float offset = Util.dp2px(40);

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_2_5 + vm_2_5 - offset));

//        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_1_5 + vm_1_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;
        h_2_5 = h_2_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;
        vm_2_5 = vm_2_5 / times;

        offset = offset / times;

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_2_5 + vm_2_5 - offset));

    }

    private void setupGoMenu(int channel) {
        initBoomMenuButton();
        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 1806;
                        } else if (i == 1) {
                            tag = 396;
                        } else if (i == 2) {
                            tag = 397;
                        } else if (i == 3) {
                            tag = 398;
                        } else if (i == 4) {
                            tag = 399;
                        } else if (i == 5) {
                            tag = 400;
                        } else if (i == 6) {
                            tag = 401;
                        } else if (i == 7) {
                            tag = 402;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w / 2 - hm / 2, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w / 2 + hm / 2, +h_0_5 + vm_0_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        int offset = Util.dp2px(5);

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w / 2 - hm / 2, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w / 2 + hm / 2, +h_0_5 + vm_0_5 + offset));

    }

    private void setupFoodMenu(int channel) {
        initBoomMenuButton();

        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 309;
                        } else if (i == 1) {
                            tag = 310;
                        } else if (i == 2) {
                            tag = 311;
                        } else if (i == 3) {
                            tag = 312;
                        } else if (i == 4) {
                            tag = 313;
                        } else if (i == 5) {
                            tag = 314;
                        } else if (i == 6) {
                            tag = 315;
                        } else if (i == 7) {
                            tag = 316;
                        } else if (i == 8) {
                            tag = 317;
                        } else if (i == 9) {
                            tag = 318;
                        } else if (i == 10) {
                            tag = 319;
                        } else if (i == 11) {
                            tag = 320;
                        } else if (i == 12) {
                            tag = 321;
                        } else if (i == 13) {
                            tag = 322;
                        } else if (i == 14) {
                            tag = 323;
                        } else if (i == 15) {
                            tag = 324;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(56);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;
        float h_2_5 = h * 2.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;
        float vm_2_5 = vm * 2.5f;

        float offset = Util.dp2px(10);

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_2_5 - vm_2_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_2_5 + vm_2_5 - offset));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;
        h_2_5 = h_2_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;
        vm_2_5 = vm_2_5 / times;

        offset = offset / times;

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_2_5 - vm_2_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_2_5 - vm_2_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_2_5 + vm_2_5 - offset));


    }

    private void setupArtMenu(int channel) {
        initBoomMenuButton();
        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 187;
                        } else if (i == 1) {
                            tag = 204;
                        } else if (i == 2) {
                            tag = 1781;
                        } else if (i == 3) {
                            tag = 191;
                        } else if (i == 4) {
                            tag = 192;
                        } else if (i == 5) {
                            tag = 193;
                        } else if (i == 6) {
                            tag = 194;
                        } else if (i == 7) {
                            tag = 196;
                        } else if (i == 8) {
                            tag = 195;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        float offset = Util.dp2px(10);

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 + offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 + offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 + offset));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        offset = offset * 5 / times;

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 + offset));


    }

    private void setupPhotographMenu(int channel) {
        initBoomMenuButton();
        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 222;
                        } else if (i == 1) {
                            tag = 223;
                        } else if (i == 2) {
                            tag = 224;
                        } else if (i == 3) {
                            tag = 225;
                        } else if (i == 4) {
                            tag = 226;
                        } else if (i == 5) {
                            tag = 227;
                        } else if (i == 6) {
                            tag = 228;
                        } else if (i == 7) {
                            tag = 229;
                        } else if (i == 8) {
                            tag = 230;
                        } else if (i == 9) {
                            tag = 231;
                        } else if (i == 10) {
                            tag = 232;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w / 2 - hm / 2, +h_1_5 + vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w / 2 + hm / 2, +h_1_5 + vm_1_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w / 2 - hm / 2, +h_1_5 + vm_1_5));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w / 2 + hm / 2, +h_1_5 + vm_1_5));
    }

    private void setupDesignMenu(int channel) {
        initBoomMenuButton();
        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 244;
                        } else if (i == 1) {
                            tag = 243;
                        } else if (i == 2) {
                            tag = 244;
                        } else if (i == 3) {
                            tag = 245;
                        } else if (i == 4) {
                            tag = 246;
                        } else if (i == 5) {
                            tag = 1773;
                        } else if (i == 6) {
                            tag = 1774;
                        } else if (i == 7) {
                            tag = 1775;
                        } else if (i == 8) {
                            tag = 1776;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        int offset = Util.dp2px(5);

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 + offset));
    }

    private void setupHomeMenu(int channel) {
        initBoomMenuButton();
        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 592;
                        } else if (i == 1) {
                            tag = 255;
                        } else if (i == 2) {
                            tag = 256;
                        } else if (i == 3) {
                            tag = 258;
                        } else if (i == 4) {
                            tag = 259;
                        } else if (i == 5) {
                            tag = 260;
                        } else if (i == 6) {
                            tag = 261;
                        } else if (i == 7) {
                            tag = 264;
                        } else if (i == 8) {
                            tag = 265;
                        } else if (i == 9) {
                            tag = 274;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_1_5 + vm_1_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_1_5 + vm_1_5));

    }

    private void setupNewsMenu(int channel) {
        initBoomMenuButton();
        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 148;
                        } else if (i == 1) {
                            tag = 577;
                        } else if (i == 2) {
                            tag = 149;
                        } else if (i == 3) {
                            tag = 150;
                        } else if (i == 4) {
                            tag = 154;
                        } else if (i == 5) {
                            tag = 153;
                        } else if (i == 6) {
                            tag = 151;
                        } else if (i == 7) {
                            tag = 620;
                        } else if (i == 8) {
                            tag = 155;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(80);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));

        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        mBoomMenuButton.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;

        int offset = Util.dp2px(5);
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 + offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 + offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 + offset));
    }

    private void setupVideoMenu(int channel) {
        initBoomMenuButton();

        String[] names = getChannelClassify(channel);
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            SimpleCircleButton.Builder builder = new SimpleCircleButton
                .Builder()
                .imageRect(imageRect)

                .normalImageDrawable(new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setTextAlign(Paint.Align.LEFT);
                        paint.setTextSize(textSize);
                        paint.setColor(textColor);
                        Rect bounds = new Rect();
                        paint.getTextBounds(name, 0, name.length(), bounds);
                        canvas.drawText(name, radius - bounds.width() / 2,
                            radius + bounds.height() / 2, paint);
                    }

                    @Override
                    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.RGB_565;
                    }
                })
                .shadowEffect(true)
                .rippleEffect(true)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int i) {
                        int tag = 0;
                        if (i == 0) {
                            tag = 1802;
                        } else if (i == 1) {
                            tag = 1792;
                        } else if (i == 2) {
                            tag = 1788;
                        } else if (i == 3) {
                            tag = 1835;
                        } else if (i == 4) {
                            tag = 1836;
                        } else if (i == 5) {
                            tag = 1784;
                        } else if (i == 6) {
                            tag = 1837;
                        } else if (i == 7) {
                            tag = 1787;
                        } else if (i == 8) {
                            tag = 1790;
                        } else if (i == 9) {
                            tag = 1791;
                        } else if (i == 10) {
                            tag = 1786;
                        } else if (i == 11) {
                            tag = 1793;
                        } else if (i == 12) {
                            tag = 1838;
                        }
                        mBoomMenuButtonClickListener.onClick(tag);
                    }
                });
            mBoomMenuButton.addBuilder(builder);
        }

        float w = Util.dp2px(80);
        float h = Util.dp2px(60);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;
        float h_2_5 = h * 2.5f;

        float hm = mBoomMenuButton.getButtonHorizontalMargin();
        float vm = mBoomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;
        float vm_2_5 = vm * 2.5f;

        float offset = Util.dp2px(40);

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomButtonPlacePositions()
            .add(new PointF(0, +h_2_5 + vm_2_5 - offset));

        float times = 10;

        w = w / times;
        h = h / times;
        h_0_5 = h_0_5 / times;
        h_1_5 = h_1_5 / times;
        h_2_5 = h_2_5 / times;

        hm = hm / times;
        vm = vm / times;
        vm_0_5 = vm_0_5 / times;
        vm_1_5 = vm_1_5 / times;
        vm_2_5 = vm_2_5 / times;

        offset = offset / times;

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_1_5 - vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_1_5 - vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, -h_0_5 - vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, -h_0_5 - vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_0_5 + vm_0_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_0_5 + vm_0_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(-w - hm, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_1_5 + vm_1_5 - offset));
        mBoomMenuButton.getCustomPiecePlacePositions()
            .add(new PointF(+w + hm, +h_1_5 + vm_1_5 - offset));

        mBoomMenuButton.getCustomPiecePlacePositions().add(new PointF(0, +h_2_5 + vm_2_5 - offset));
    }

    private String[] getChannelClassify(int channel) {
        Resources resources = parent.getResources();
        if (channel == 0) {
            return resources.getStringArray(R.array.beauty_channel);
        } else if (channel == 1) {
            return resources.getStringArray(R.array.fun_channel);
        } else if (channel == 2) {
            return resources.getStringArray(R.array.wallpaper_channel);
        } else if (channel == 3) {
            return resources.getStringArray(R.array.pet_channel);
        } else if (channel == 4) {
            return resources.getStringArray(R.array.car_channel);
        } else if (channel == 5) {
            return resources.getStringArray(R.array.go_channel);
        } else if (channel == 6) {
            return resources.getStringArray(R.array.foot_channel);
        } else if (channel == 7) {
            return resources.getStringArray(R.array.art_channel);
        } else if (channel == 8) {
            return resources.getStringArray(R.array.photography_channel);
        } else if (channel == 9) {
            return resources.getStringArray(R.array.design_channel);
        } else if (channel == 10) {
            return resources.getStringArray(R.array.home_channel);
        } else if (channel == 11) {
            return resources.getStringArray(R.array.news_channel);
        } else if (channel == 12) {
            return resources.getStringArray(R.array.video_channel);
        } else {
            return null;
        }
    }

    public void setBoomMenuButtonClickListener(BoomMenuButtonClickListener listener) {
        mBoomMenuButtonClickListener = listener;
    }

    interface BoomMenuButtonClickListener {

        void onClick(int tag);
    }

}
