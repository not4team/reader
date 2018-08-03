package com.book.novel.model

import com.book.ireader.model.bean.Source

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 1:49
 */
class SimpleSource(val name: String, val baseUrl: String, val searchUrl: String, var encode: String?) : Source {
    override fun getSourceEncode(): String? {
        return encode
    }

    override fun getSourceSearchUrl(): String {
        return searchUrl
    }

    override fun getSourceBaseUrl(): String {
        return baseUrl
    }

    override fun getSourceName(): String {
        return name
    }

    override fun getSourceClass(): Class<Any> {
        return javaClass
    }
}