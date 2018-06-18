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
                        var cookies = cookieStore.get(HttpUrl.parse(mQidianUrl))
                        if (cookies == null) {
                            cookies = listOf()
                        }
                        return cookies
                    }
                })
                .addNetworkInterceptor { chain ->
                    val request = chain.request()
                    //添加token
                    var url = request.url().toString()
                    Log.d(TAG, "intercept: " + url)
                    if (url.contains("m.qidian.com/majax/rank")) {
                        url = url + "&_csrfToken=" + getCookie("_csrfToken")
                    }
                    val addCookieTokenRequest = request.newBuilder().url(url).build()
                    Log.d(TAG, "add cookie url: " + addCookieTokenRequest.url().toString())
                    val response = chain.proceed(addCookieTokenRequest)
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
