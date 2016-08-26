package ru.ilyamodder.vkfeed;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;

/**
 * Created by ilya on 26.08.16.
 */

public class GlideModule implements com.bumptech.glide.module.GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(10 * 1024 * 1024))
                .setDiskCache(new InternalCacheDiskCacheFactory(context));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
