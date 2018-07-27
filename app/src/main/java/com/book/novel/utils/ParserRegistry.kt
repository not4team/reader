package com.book.novel.utils

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 0:41
 */
class ParserRegistry {
    private val parsers = mutableListOf<Entry>()

    @Synchronized
    fun append(url: String, parser: Parser) {
        parsers.add(Entry(url, parser))
    }

    @Synchronized
    fun prepend(url: String, parser: Parser) {
        parsers.add(0, Entry(url, parser))
    }

    @Synchronized
    fun get(url: String): Parser? {
        for (index in parsers.indices) {
            val entry: Entry = parsers[index]
            if (parsers[index].equals(url)) {
                return entry.parser
            }
        }
        return null;
    }

    private inner class Entry(val url: String, val parser: Parser) {
        override fun equals(other: Any?): Boolean {
            if (other is String) {
                return url.equals(other)
            } else {
                return super.equals(other)
            }
        }
    }
}