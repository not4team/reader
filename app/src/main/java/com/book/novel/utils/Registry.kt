package com.book.novel.utils

import com.book.novel.model.Source

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 0:24
 */
class Registry {
    private val parserRegistry: ParserRegistry = ParserRegistry()
    fun <source> register(sourceClass: Class<source>, parser: Parser): Registry {
        parserRegistry.append(sourceClass, parser)
        return this
    }

    fun getParser(source: Source): Parser? {
        return parserRegistry.get(source.getSourceClass())
    }
}