package com.pluu.webtoon

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

/**
 * My Glide Module
 * Created by PLUUSYSTEM-NEW on 2016-01-19.
 */
@GlideModule
class MyGlideModule : AppGlideModule() {

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = (maxMemory / 8).toLong()
    private val diskCacheSize = (1024 * 1024 * 10).toLong()

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(cacheSize))
            .setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, "cache", diskCacheSize))
    }

    override fun isManifestParsingEnabled(): Boolean = false

}
