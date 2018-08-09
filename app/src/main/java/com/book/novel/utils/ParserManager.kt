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
        val bxwxSource = SimpleSource("bxwx3", "http://www.bxwx3.org", "http://www.bxwx3.org/search.aspx", "bookname", "get", "gbk")
        val sczprcSource = SimpleSource("sczprc", "https://www.sczprc.com", "https://www.sczprc.com/modules/article/search.php", "searchkey", "post", "gbk")
        val biqugexswSource = SimpleSource("biqugexsw", "https://www.biqugexsw.com", "https://www.biqugexsw.com/s.php?", "q", "get", "gbk")
        registry.register(bxwxSource.sourceBaseUrl, bxwxSource)
        registry.register(bxwxSource.sourceBaseUrl, BxwxParser())
        registry.register(sczprcSource.sourceBaseUrl, sczprcSource)
        registry.register(sczprcSource.sourceBaseUrl, SczprcParser())
        registry.register(biqugexswSource.sourceBaseUrl, biqugexswSource)
        registry.register(biqugexswSource.sourceBaseUrl, BiqugexswParser())
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