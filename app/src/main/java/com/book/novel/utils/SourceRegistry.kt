package com.book.novel.utils

import com.book.novel.model.Source

/**
 * Created with author.
 * Description:
 * Date: 2018-07-26
 * Time: 下午2:37
 */
class SourceRegistry {
    val sources = mutableListOf<Entry>()

    @Synchronized
    fun append(url: String, source: Source) {
        sources.add(Entry(url, source))
    }

    @Synchronized
    fun prepend(url: String, source: Source) {
        sources.add(0, Entry(url, source))
    }

    @Synchronized
    fun get(url: String): Source? {
        for (i in sources.indices) {
            if (sources[i].equals(url)) {
                return sources[i].source
            }
        }
        return null;
    }

    inner class Entry(val url: String, val source: Source) {
        override fun equals(other: Any?): Boolean {
            if (other is String) {
                return url.equals(other)
            } else {
                return super.equals(other)
            }
        }
    }
}