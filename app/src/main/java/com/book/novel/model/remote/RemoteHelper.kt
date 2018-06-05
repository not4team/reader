package com.book.novel.model.remote

import android.util.Log
import com.book.ireader.utils.Constant
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Created by newbiechen on 17-4-20.
 */

class RemoteHelper private constructor() {
    val retrofit: Retrofit
    val okHttpClient: OkHttpClient
    val mQidianUrl = "https://m.qidian.com"
    val cookieStore = mutableMapOf<HttpUrl, List<Cookie>>()

    init {
        okHttpClient = OkHttpClient.Builder()
                .cookieJar(object : CookieJar {
                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        cookieStore.put(HttpUrl.parse(mQidianUrl)!!, cookies)
                        cookies.forEach { cookie -> Log.e(TAG, "name:" + cookie.name() + ",value:" + cookie.value()) }
                    }

                    override fun loadForRequest(url: HttpUrl): List<Cookie>? {
                        return cookieStore.get(HttpUrl.parse(mQidianUrl))
                    }
                })
                .addNetworkInterceptor { chain ->
                    val request = chain.request()

                    //在这里获取到request后就可以做任何事情了
                    val response = chain.proceed(request)
                    Log.d(TAG, "intercept: " + request.url().toString())
                    response
                }.build()

        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.API_BASE_URL)
                .build()
    }

    fun getCookie(name: String): String? {
        val cookies: List<Cookie>? = cookieStore.get(HttpUrl.parse(mQidianUrl)!!)
        if (cookies != null) {
            cookies.forEach { cookie -> if (cookie.name() === name) return cookie.value() }
        }
        return null
    }

    companion object {
        private val TAG = "RemoteHelper"
        private var sInstance: RemoteHelper? = null

        val instance: RemoteHelper
            get() {
                if (sInstance == null) {
                    synchronized(RemoteHelper::class.java) {
                        if (sInstance == null) {
                            sInstance = RemoteHelper()
                        }
                    }
                }
                return sInstance!!
            }
    }
}
