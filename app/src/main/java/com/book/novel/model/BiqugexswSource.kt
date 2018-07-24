package com.book.novel.model

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 1:49
 */
class BiqugexswSource(val name: String, val url: String) : Source {
    override fun getSourceName(): String {
        return name
    }

    override fun getSourceUrl(): String {
        return url
    }

    override fun getSourceClass(): Class<Any> {
        return javaClass
    }
}