package com.book.novel.model

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 1:39
 */
interface Source {
    fun getSourceClass(): Class<Any>
    fun getSourceName(): String
    fun getSourceUrl(): String
}