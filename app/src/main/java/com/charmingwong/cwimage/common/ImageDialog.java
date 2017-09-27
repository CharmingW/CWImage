package com.charmingwong.cwimage.common;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.base.BaseModel;
import com.charmingwong.cwimage.util.ApplicationUtils;

/**
 * Created by CharmingWong on 2017/6/16.
 */

public class ImageDialog extends DialogFragment {

    private BaseModel mImage;

    public ImageDialog() {
    }

    public static ImageDialog newInstance(BaseModel baseModel) {
        ImageDialog imageDialog = new ImageDialog();
        Bundle args = new Bundle();
        args.putSerializable("images", baseModel);
        imageDialog.setArguments(args);
        return imageDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImage = (BaseModel) getArguments().getSerializable("images");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_dialog, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = ApplicationUtils.dp2px(300);
        layoutParams.height = layoutParams.width * mImage.getHeight() / mImage.getWidth();

        Glide.with(getActivity())
                .load(mImage.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

        rootView.onTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_DOWN, rootView.getPivotX(), rootView.getPivotY(), 0));

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
