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
        val biqugexswSource = SimpleSource("来源1", "https://www.biqugexsw.com", "https://www.biqugexsw.com/s.php", "q", "get", "gbk")
        val bxwxSource = SimpleSource("来源2", "http://www.bxwx3.org", "http://www.bxwx3.org/search.aspx", "bookname", "get", "gbk")
        val sczprcSource = SimpleSource("来源3", "https://www.sczprc.com", "https://www.sczprc.com/modules/article/search.php", "searchkey", "post", "gbk")
        registry.register(biqugexswSource.sourceBaseUrl, biqugexswSource)
        registry.register(biqugexswSource.sourceBaseUrl, BiqugexswParser())
        registry.register(bxwxSource.sourceBaseUrl, bxwxSource)
        registry.register(bxwxSource.sourceBaseUrl, BxwxParser())
        registry.register(sczprcSource.sourceBaseUrl, sczprcSource)
        registry.register(sczprcSource.sourceBaseUrl, SczprcParser())
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