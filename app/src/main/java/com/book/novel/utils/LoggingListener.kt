package com.book.novel.utils

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*

/**
 * Created with author.
 * Description:
 * Date: 2018-08-03
 * Time: 下午3:47
 */
class LoggingListener<R> : RequestListener<R> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<R>?, isFirstResource: Boolean): Boolean {
        android.util.Log.d("GLIDE", String.format(Locale.ROOT,
                "onLoadFailed(%s, %s, %s, %s)", e, model, target, isFirstResource), e)
        return false
    }

    override fun onResourceReady(resource: R, model: Any?, target: Target<R>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        android.util.Log.d("GLIDE", String.format(Locale.ROOT, "onResourceReady(%s, %s, %s, %s, %s)", resource, model, target, dataSource, isFirstResource))
        return false
    }
}