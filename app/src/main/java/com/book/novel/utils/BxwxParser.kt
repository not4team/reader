package com.book.novel.utils

import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.utils.MD5Utils
import org.jsoup.Jsoup
import java.net.URLEncoder

/**
 * Created with author.
 * Description:
 * Date: 2018-07-26
 * Time: 下午3:19
 */
class BxwxParser : Parser {
    override fun parseSearchResult(html: String): List<SearchBookPackage.BooksBean> {
        val doc = Jsoup.parse(html)
        val list = mutableListOf<SearchBookPackage.BooksBean>()
        val elements = doc.select("#newscontent div ul").first()
        elements.select("li").forEach {
            val spans = it.select("span")
            val category = spans[0].text()
            val title = spans[1].text()
            val id = spans[1].select("a").attr("href")
            val lastChapter = spans[2].text()
            val author = spans[3].text()
            val book = SearchBookPackage.BooksBean()
            book.title = title
            book.author = author
            book.cat = category.substring(1, category.length - 1)
            book._id = id
            book.lastChapter = lastChapter
            list.add(book)
        }
        return list;
    }

    override fun parseBookDetail(html: String): BookDetailBean {
        val doc = Jsoup.parse(html)
        val elements = doc.select("div.box_con")
        //书籍信息
        val mainInfo = elements[1].select("div#maininfo")
        val info = mainInfo.select("div#info")
        val title = info.select("h1").first().text()
        val author = info.select("div div").first().text()
        val lastChapter = info.select("div div")[3].select("span a").text()
        val _id = info.select("div div")[3].select("span a").attr("href")
        val intro = mainInfo.select("div#intro p").first().text()
        val cover = elements[1].select("div#sidebar div img").attr("src")
        val mBookDetailBean = BookDetailBean()
        mBookDetailBean._id = AndroidUtils.base64Encode(URLEncoder.encode(_id.substring(0, _id.lastIndexOf("/")), "utf-8"))
        mBookDetailBean.title = title
        mBookDetailBean.author = author.replace("作 者：", "")
        mBookDetailBean.lastChapter = lastChapter
        mBookDetailBean.longIntro = intro
        mBookDetailBean.cover = cover
        mBookDetailBean.cat = "暂无数据"
        mBookDetailBean.updated = "暂无数据"
        //章节目录
        val chapterList = mutableListOf<BookChapterBean>()
        elements[3].select("#list dl dd").forEach {
            val title = it.select("a").text()
            val link = it.select("a").attr("href")
            val bookChapterBean = BookChapterBean()
            bookChapterBean.title = title
            bookChapterBean.link = link
            bookChapterBean.id = MD5Utils.strToMd5By16(bookChapterBean.link)
            bookChapterBean.bookId = mBookDetailBean._id
            chapterList.add(bookChapterBean)
        }
        mBookDetailBean.bookChapterBeans = chapterList
        mBookDetailBean.chaptersCount = chapterList.size
        return mBookDetailBean
    }

    override fun parseChapterInfo(html: String): ChapterInfoBean {
        val doc = Jsoup.parse(html)
        val content = doc.select("div.content_read")
        val title = content.select("div.bookname h1").text()
        val body = content.select("#zjneirong").text()
        val chapterInfoBean = ChapterInfoBean()
        chapterInfoBean.title = title
        chapterInfoBean.body = body
        return chapterInfoBean
    }
}