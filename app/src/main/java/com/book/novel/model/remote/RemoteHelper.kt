package com.book.novel.model.remote

import android.util.Log
import com.book.ireader.utils.Constant
import com.lereader.novel.BuildConfig
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


/**
 * Created by newbiechen on 17-4-20.
 */

class RemoteHelper private constructor() {
    val retrofit: Retrofit
    val okHttpClient: OkHttpClient
    val cookieStore = mutableMapOf<HttpUrl, List<Cookie>>()
    val trustAllCerts = TrustAllCerts()

    init {
        val sslSocketFactory = SSLSocketFactoryCompat(trustAllCerts);
        val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                        //qidian
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384)
                .build()
        okHttpClient = OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(spec, ConnectionSpec.CLEARTEXT))
                .sslSocketFactory(sslSocketFactory, trustAllCerts)
                .hostnameVerifier(TrustAllHostnameVerifier())
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

    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
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
