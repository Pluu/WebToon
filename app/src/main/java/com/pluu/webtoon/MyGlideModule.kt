package com.pluu.webtoon

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

/**
 * My Glide Module
 * Created by PLUUSYSTEM-NEW on 2016-01-19.
 */
@GlideModule
class MyGlideModule : AppGlideModule() {

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private val DISK_CACHE_SIZE = 1024 * 1024 * 10

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(cacheSize))
                .setDiskCache(ExternalCacheDiskCacheFactory(context, "cache", DISK_CACHE_SIZE))
    }

    override fun isManifestParsingEnabled(): Boolean = false

}
