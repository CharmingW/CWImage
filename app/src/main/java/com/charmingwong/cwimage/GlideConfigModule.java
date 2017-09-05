package com.charmingwong.cwimage;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by CharmingWong on 2017/5/14.
 */

public class GlideConfigModule implements GlideModule {

    //磁盘缓存大小
    private final int DISK_CACHE_SIZE;

    //内存缓存大小，选取可用内存的 1/8 作为内存缓存
    private final int MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 4);

    private final int BITMAP_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 16);

    public GlideConfigModule() {
        DISK_CACHE_SIZE = 1024 * 1024 * 500;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置磁盘缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE));
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "image_cache", DISK_CACHE_SIZE));

        builder.setMemoryCache(new LruResourceCache(MEMORY_CACHE_SIZE));
        builder.setBitmapPool(new LruBitmapPool(BITMAP_CACHE_SIZE));

        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
