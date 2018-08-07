package com.book.novel.utils

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.Source
import com.book.ireader.model.bean.packages.SearchBookPackage

/**
 * Created with author.
 * Description:
 * Date: 2018/8/8
 * Time: 0:03
 */
class SczprcParser:Parser {
    override fun parseSearchResult(source: Source, html: String): List<SearchBookPackage.BooksBean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parseBookDetail(source: Source, html: String): BookDetailBean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parseChapterInfo(source: Source, html: String): ChapterInfoBean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}