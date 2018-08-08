package com.book.novel.utils

import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.Source

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 0:46
 */
interface Parser {
    fun parseSearchResult(source: Source, html: String): List<BookDetailBean>
    fun parseBookDetail(source: Source, html: String): BookDetailBean
    fun parseBookChapter(source: Source, html: String): List<BookChapterBean>
    fun parseChapterInfo(source: Source, html: String): ChapterInfoBean
}