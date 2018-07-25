package com.book.novel.utils

import com.book.novel.model.BiqugexswSource
import com.book.novel.model.Source

/**
 * Created with author.
 * Description:
 * Date: 2018-07-25
 * Time: 下午4:31
 */
object ParserManager {
    private val registry = Registry()

    init {
        registry.register(BiqugexswSource::class.java, BiqugexswParser())
    }

    fun getParser(source: Source): Parser? {
        return registry.getParser(source)
    }
}