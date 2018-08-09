package com.book.novel.utils

import com.book.ireader.App
import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.Source
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.utils.MD5Utils
import org.jsoup.Jsoup

/**
 * Created with author.
 * Description:
 * Date: 2018/8/8
 * Time: 0:03
 */
class SczprcParser : Parser {
    override fun parseSearchResult(source: Source, html: String): List<BookDetailBean> {
        val doc = Jsoup.parse(html)
        val list = mutableListOf<BookDetailBean>()
        val tbody = doc.select("div#conn table.grid tbody")
        if (tbody != null && tbody.size > 0) {
            val trs = tbody.select("tr")
            for (i in 1 until trs.size) {
                val tds = trs[i].select("td")
                val title = tds[0].text()
                val link = tds[0].select("a").attr("href")
                val lastChapter = tds[1].text()
                val author = tds[2].text()
                val wordCount = tds[3].text()
                val updated = tds[4].text()
                val bookDetailBean = BookDetailBean()
                bookDetailBean.title = title
                bookDetailBean.link = link
                bookDetailBean.lastChapter = lastChapter
                bookDetailBean.author = author
                bookDetailBean.wordCount = wordCount
                bookDetailBean.updated = updated
                list.add(bookDetailBean)
            }
        } else {
            list.add(parseBookDetail(source, html))
        }
        return list
    }

    override fun parseBookDetail(source: Source, html: String): BookDetailBean {
        val doc = Jsoup.parse(html)
        val conn = doc.select("div#conn")
        val info = conn.select("div.bookcover-hd")
        val bookTitle = info.select("div.booktitle")
        val title = bookTitle.select("div.name h1").text()
        val category = bookTitle.select("p a")[0].text()
        val author = bookTitle.select("p a")[1].text()
        val bookcover = conn.select("div.bookcover-bd")
        val cover = bookcover.select("div.bookcover-l div.bigpic img").attr("src")
        val longIntro = bookcover.select("div.bookcover-r div.book-intro").text()
        val chapterDir = bookcover.select("div.bookcover-r div.bookbtn-box a.catalogbtn").attr("href")
        val link = doc.select("div.footer-bar div.infos p")[0].select("a").attr("href")
        val bookDetailBean = BookDetailBean()
        bookDetailBean.title = title
        bookDetailBean.cat = category
        bookDetailBean.author = author
        bookDetailBean._id = AndroidUtils.base64Encode(bookDetailBean.title + "," + bookDetailBean.author)
        bookDetailBean.cover = cover
        bookDetailBean.longIntro = longIntro
        bookDetailBean.chapterDir = chapterDir
        bookDetailBean.link = link
        //获取章节目录
        RemoteRepository
                .getInstance(App.getContext()).getBookChapters(bookDetailBean.chapterDir).subscribe({ chapters ->
                    bookDetailBean.bookChapterBeans = chapters
                    bookDetailBean.lastChapter = chapters.last().title
                }) { e ->
                    e.printStackTrace()
                }
        return bookDetailBean
    }

    override fun parseBookChapter(source: Source, html: String): List<BookChapterBean> {
        val doc = Jsoup.parse(html)
        val list = mutableListOf<BookChapterBean>()
        val readbg = doc.select("div.readbg")
        val readlocation = readbg.select("div.readlocation")
        val author = readlocation.select("div.bookrel a").first().text()
        val chapterCon = readbg.select("div.chapter-con")
        val bookTitle = chapterCon.select("div.chapter-bd div.volume-intro h2 a").text()
        val lis = chapterCon.select("div.chapter-bd ul li")
        val bookId = AndroidUtils.base64Encode(bookTitle + "," + author)
        for (i in lis.indices) {
            val title = lis[i].select("a").text()
            val link = lis[i].select("a").attr("href")
            val bookChapterBean = BookChapterBean()
            bookChapterBean.title = title
            bookChapterBean.link = source.sourceBaseUrl + link
            bookChapterBean.id = MD5Utils.strToMd5By16(bookChapterBean.link)
            bookChapterBean.bookId = bookId
            list.add(bookChapterBean)
        }
        return list
    }

    override fun parseChapterInfo(source: Source, html: String): ChapterInfoBean {
        val doc = Jsoup.parse(html)
        val content = doc.select("div.readbg")
        val title = content.select("h1.article-title").text()
        val body = content.select("div.article-con").text()
        val chapterInfoBean = ChapterInfoBean()
        chapterInfoBean.title = title
        chapterInfoBean.body = body
        return chapterInfoBean
    }
}