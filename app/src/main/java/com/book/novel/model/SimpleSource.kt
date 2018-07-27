package com.book.novel.model

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 1:49
 */
class SimpleSource(val name: String, val baseUrl: String, val searchUrl: String) : Source {
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