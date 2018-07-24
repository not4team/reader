package com.book.novel.utils

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 0:41
 */
class ParserRegistry {
    private val parsers = mutableListOf<Entry<*>>()

    @Synchronized
    fun <Z> append(sourceClass: Class<Z>, parser: Parser) {
        parsers.add(Entry(sourceClass, parser))
    }

    @Synchronized
    fun <Z> prepend(sourceClass: Class<Z>, parser: Parser) {
        parsers.add(0, Entry(sourceClass, parser))
    }

    @Synchronized
    fun <Z> get(sourceClass: Class<Z>): Parser? {
        for (index in parsers.indices) {
            val entry: Entry<Z> = parsers[index] as Entry<Z>
            if (entry.handles(sourceClass)) {
                return entry.parser
            }
        }
        return null;
    }

    private inner class Entry<T>(val sourceClass: Class<T>, val parser: Parser) {
        fun handles(sourceClass: Class<T>): Boolean {
            return this.sourceClass.isAssignableFrom(sourceClass)
        }
    }
}