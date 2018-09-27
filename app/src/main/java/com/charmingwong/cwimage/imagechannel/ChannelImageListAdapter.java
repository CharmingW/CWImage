package com.charmingwong.cwimage.imagechannel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.common.ImageDialog;
import com.charmingwong.cwimage.imagedetails.ImageDetailsActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huangchaoming on 2018/9/27
 */
public class ChannelImageListAdapter extends RecyclerView.Adapter<ChannelImageListAdapter.ChannelImageViewHolder> {

    private List<ChannelImage> mChannelImages;

    private Context mContext;

    ChannelImageListAdapter(Context context) {
        mContext = context;
        mChannelImages = new ArrayList<>(0);
    }

    void appendImages(List<ChannelImage> channelImages) {
        int size = getItemCount();
        mChannelImages.addAll(channelImages);
        notifyItemInserted(size);
    }

    void replaceData(List<ChannelImage> channelImages) {
        mChannelImages.clear();
        mChannelImages.addAll(channelImages);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mChannelImages.size();
    }

    @NonNull
    @Override
    public ChannelImageViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View rootView = ((Activity) parent.getContext())
                .getLayoutInflater()
                .inflate(R.layout.item_image_channel, parent, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageDetailsActivity.class);
                int maxSize = Math.min(getItemCount(), 1000);
                int offset = getItemCount() - maxSize;
                List<ChannelImage> data = new ArrayList<>(maxSize);
                for (int i = offset; i < getItemCount(); i++) {
                    data.add(mChannelImages.get(i));
                }
                int position = (Integer) v.findViewById(R.id.resolution).getTag() - offset;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int position = (int) v.findViewById(R.id.resolution).getTag();

                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                final ImageDialog dialog = ImageDialog.newInstance(mChannelImages.get(position));

                dialog.show(fm, "dialog");

                v.onTouchEvent(
                        MotionEvent
                                .obtain(0, 0, MotionEvent.ACTION_CANCEL, v.getPivotX(), v.getPivotY(),
                                        0));

                return true;
            }
        });

        rootView.setClipToOutline(true);

        return new ChannelImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelImageViewHolder holder, int position) {
        final ChannelImage channelImage = mChannelImages.get(position);
        float density = Resources.getSystem().getDisplayMetrics().density;
        int width = (int) (178 * density);
        int height = width
                * channelImage.getHeight()
                / channelImage.getWidth();

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.image
                .getLayoutParams();
//            lp.width = width;
        lp.height = height;

        String thumbUrl = channelImage.getThumbUrl();
        String url = channelImage.getUrl();

        if (url.toLowerCase().endsWith(".gif") || thumbUrl.toLowerCase().endsWith(".gif")) {
            holder.type.setText("GIF");
            Glide.with(mContext)
                    .load(url)
                    .asBitmap()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
        } else {
            Glide.with(mContext)
                    .load(thumbUrl)
                    .asBitmap()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.layout
                .getLayoutParams();
        layoutParams.width = width;

        holder.resolution.setText(channelImage.getWidth() + " x " + channelImage.getHeight());

        holder.resolution.setTag(position);
    }

    static class ChannelImageViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        TextView resolution;

        TextView type;

        FrameLayout layout;

        ChannelImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            resolution = itemView.findViewById(R.id.resolution);
            type = itemView.findViewById(R.id.type);
            layout = itemView.findViewById(R.id.tagLayout);
        }
    }
}