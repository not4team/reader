package com.book.novel.utils

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.packages.SearchBookPackage

/**
 * Created with author.
 * Description:
 * Date: 2018/7/25
 * Time: 0:46
 */
interface Parser {
    fun parseSearchResult(html: String): List<SearchBookPackage.BooksBean>
    fun parseBookDetail(html: String): BookDetailBean
    fun parseChapterInfo(html: String): ChapterInfoBean
}