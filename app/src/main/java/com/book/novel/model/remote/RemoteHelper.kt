package com.book.novel.model.remote

import android.util.Log
import com.book.ireader.utils.Constant
import com.lereader.novel.BuildConfig
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
    val cookieStore = mutableMapOf<HttpUrl, List<Cookie>>()

    init {
        okHttpClient = OkHttpClient.Builder()
                .cookieJar(object : CookieJar {
                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        cookieStore.put(url, cookies)
                        if (BuildConfig.LOG_DEBUG) {
                            cookies.forEach { cookie -> Log.e(TAG, "name:" + cookie.name() + ",value:" + cookie.value()) }
                        }
                    }

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        if (url.toString().contains("https://www.sczprc.com/modules/article/search.php")) {
                            return listOf()
                        }
                        var cookies = cookieStore.get(url)
                        if (cookies == null) {
                            cookies = listOf()
                        }
                        return cookies
                    }
                })
                .addNetworkInterceptor { chain ->
                    val request = chain.request()
                    //添加token
                    var url = request.url()
                    var urlStr = url.toString()
                    if (BuildConfig.LOG_DEBUG) {
                        Log.d(TAG, "intercept: " + url)
                    }
                    if (urlStr.contains("m.qidian.com/majax/rank")) {
                        urlStr = urlStr + "&_csrfToken=" + getCookie(url, "_csrfToken")
                    }
                    val addCookieTokenRequest = request.newBuilder().url(urlStr).build()
                    if (BuildConfig.LOG_DEBUG) {
                        Log.d(TAG, "add cookie url: " + addCookieTokenRequest.url().toString())
                    }
                    val response = chain.proceed(addCookieTokenRequest)
                    response
                }.build()

        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.API_BASE_URL)
                .build()
    }

    fun getCookie(url: HttpUrl, name: String): String? {
        val cookies: List<Cookie>? = cookieStore.get(url)
        if (cookies != null) {
            for (index in cookies.indices) {
                if (cookies[index].name() == name) {
                    return cookies[index].value()
                }
            }
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
