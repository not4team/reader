package com.book.novel

import android.content.Context
import android.support.multidex.MultiDex
import com.book.ireader.App
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
        MobileAds.initialize(this, "ca-app-pub-7332030505319718~5396323800");
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}