package com.book.novel.utils

import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.packages.SearchBookPackage
import org.jsoup.Jsoup

/**
 * Created with author.
 * Description:
 * Date: 2018-06-12
 * Time: 下午3:52
 */
class BiqugexswParser {
    companion object {
        fun parseSearchResult(html: String): List<SearchBookPackage.BooksBean> {
            val doc = Jsoup.parse(html)
            val list = mutableListOf<SearchBookPackage.BooksBean>()
            doc.select("div.bookbox").forEach {
                val tilte = it.select("div.bookinfo h4.bookname").first().text()
                var author = it.select("div.author").first().text()
                val _id = it.select("div.bookinfo h4.bookname a").first().attr("href")
                val book = SearchBookPackage.BooksBean()
                book.title = tilte
                book.author = author.replace("作者：", "")
                book._id = _id.replace("/", "")
                list.add(book)
            }
            return list
        }

        fun parseBookDetail(html: String): BookDetailBean {
            val doc = Jsoup.parse(html)
            val bookInfo = doc.select("div.info").first()
            val cover = bookInfo.select("div.cover img").first().attr("src")
            val title = bookInfo.select("h2").first().text()
            val author = bookInfo.select("div.small span")[0].text()
            val category = bookInfo.select("div.small span")[1].text()
            val status = bookInfo.select("div.small span")[2].text()
            val wordCount = bookInfo.select("div.small span")[3].text()
            val updated = bookInfo.select("div.small span")[4].text()
            val lastChapter = bookInfo.select("div.small span")[5].text()
            val intro = bookInfo.select("div.intro").first().text()
            val mBookDetailBean = BookDetailBean()
            mBookDetailBean.cover = cover
            mBookDetailBean.title = title
            mBookDetailBean.author = author.replace("作者：", "")
            mBookDetailBean.cat = category.replace("分类：", "")
            mBookDetailBean.status = status.replace("状态：", "")
            mBookDetailBean.wordCount = wordCount.replace("字数：", "").toInt()
            mBookDetailBean.updated = updated.replace("更新时间：", "")
            mBookDetailBean.lastChapter = lastChapter.replace("最新章节：", "")
            mBookDetailBean.longIntro = intro
            val chapterList = mutableListOf<BookChapterBean>()
            val chapterElement = doc.select("div.listmain dl").first().children()
            for (index in chapterElement.size - 1 downTo 0) {
                if (chapterElement[index].text().contains("正文卷")) {
                    break
                } else {
                    val _title = chapterElement[index].text()
                    val _link = chapterElement[index].select("a").attr("href")
                    val bookChapterBean = BookChapterBean()
                    bookChapterBean.title = _title
                    bookChapterBean.link = _link
                    chapterList.add(bookChapterBean)
                }
            }
            chapterList.reverse()
            mBookDetailBean.bookChapterBeans = chapterList
            return mBookDetailBean
        }
    }
}