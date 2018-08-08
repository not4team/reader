package com.book.novel

import android.content.Context
import android.support.multidex.MultiDex
import com.book.ireader.App
import com.book.ireader.utils.Constant
import com.book.ireader.utils.SharedPreUtils
import com.book.novel.utils.IMMLeaks
import com.book.novel.utils.ParserManager
import com.google.android.gms.ads.MobileAds

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:24
 */
class NovelApplication : App() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, "ca-app-pub-7332030505319718~5396323800")
        var source = ParserManager.getSource(SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE))
        if (source == null) {
            source = ParserManager.getDefaultSource()
            SharedPreUtils.getInstance().putString(Constant.SHARED_BOOK_SOURCE, source.sourceBaseUrl)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        IMMLeaks.fixFocusedViewLeak(this)
    }
}