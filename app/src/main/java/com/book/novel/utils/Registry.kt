package com.book.novel.utils

import com.book.ireader.model.bean.Source

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 0:24
 */
class Registry {
    private val parserRegistry = ParserRegistry()
    private val sourceRegistry = SourceRegistry()
    fun register(url: String, parser: Parser): Registry {
        parserRegistry.append(url, parser)
        return this
    }

    fun register(url: String, source: Source): Registry {
        sourceRegistry.append(url, source)
        return this
    }

    fun getParser(url: String): Parser? {
        return parserRegistry.get(url)
    }

    fun getSource(url: String): Source? {
        return sourceRegistry.get(url)
    }

    fun getAllSource(): List<SourceRegistry.Entry> {
        return sourceRegistry.sources
    }
}