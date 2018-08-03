package com.book.novel.utils

import com.book.ireader.model.bean.Source
import com.book.novel.model.SimpleSource

/**
 * Created with author.
 * Description:
 * Date: 2018-07-25
 * Time: 下午4:31
 */
object ParserManager {
    private val registry = Registry()

    init {
        val bxwxSource = SimpleSource("bxwx3", "http://www.bxwx3.org", "http://www.bxwx3.org/search.aspx?bookname=", "gb2312")
        val biqugexswSource = SimpleSource("biqugexsw", "http://www.biqugexsw.com", "http://www.biqugexsw.com/s.php?q=", null)
        registry.register(bxwxSource.baseUrl, bxwxSource)
        registry.register(bxwxSource.baseUrl, BxwxParser())
        registry.register(biqugexswSource.baseUrl, biqugexswSource)
        registry.register(biqugexswSource.baseUrl, BiqugexswParser())
    }

    fun getAllSources(): List<SourceRegistry.Entry> {
        return registry.getAllSource()
    }

    fun getDefaultSource(): Source {
        return getAllSources()[0].source
    }

    fun getSource(url: String): Source? {
        return registry.getSource(url)
    }

    fun getParser(url: String): Parser? {
        return registry.getParser(url)
    }
}