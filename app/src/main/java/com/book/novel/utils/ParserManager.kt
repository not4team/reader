package com.book.novel.utils

import com.book.novel.model.SimpleSource
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
        val bxwxSource = SimpleSource("笔下文学", "http://www.bxwx3.org", "http://www.bxwx3.org/search.aspx?bookname=")
        val biqugexswSource = SimpleSource("笔趣阁", "http://www.biqugexsw.com", "http://www.biqugexsw.com/s.php?q=")
        registry.register(bxwxSource.baseUrl, bxwxSource)
        registry.register(bxwxSource.baseUrl, BiqugexswParser())
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