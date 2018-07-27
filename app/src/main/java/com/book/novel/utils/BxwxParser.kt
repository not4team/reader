package com.book.novel.utils

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.packages.SearchBookPackage

/**
 * Created with author.
 * Description:
 * Date: 2018-07-26
 * Time: 下午3:19
 */
class BxwxParser : Parser {
    override fun parseSearchResult(html: String): List<SearchBookPackage.BooksBean> {
        val list = mutableListOf<SearchBookPackage.BooksBean>()
        return list;
    }

    override fun parseBookDetail(html: String): BookDetailBean {
        val bookDetailBean = BookDetailBean()
        return bookDetailBean
    }

    override fun parseChapterInfo(html: String): ChapterInfoBean {
        val chapterInfoBean = ChapterInfoBean()
        return chapterInfoBean
    }
}