package com.book.novel.model

import com.book.ireader.model.bean.Source

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 1:49
 */
class SimpleSource() : Source {
    private lateinit var name: String
    private lateinit var baseUrl: String
    private lateinit var searchUrl: String
    private lateinit var searchParam: String
    private lateinit var searchMethod: String
    private var encode: String? = null

    constructor(name: String, baseUrl: String, searchUrl: String, searchParam: String, searchMethod: String, encode: String?) : this() {
        this.name = name
        this.baseUrl = baseUrl
        this.searchUrl = searchUrl
        this.searchParam = searchParam
        this.searchMethod = searchMethod
        this.encode = encode
    }

    override fun getSourceEncode(): String? {
        return encode
    }

    override fun getSourceSearchUrl(): String {
        return searchUrl
    }

    override fun getSourceSearchParam(): String {
        return searchParam
    }

    override fun getSourceSearchMethod(): String {
        return searchMethod
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