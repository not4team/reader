package com.book.novel.utils

import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.Source
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.utils.MD5Utils
import org.jsoup.Jsoup
import java.net.URLEncoder

/**
 * Created with author.
 * Description:
 * Date: 2018-08-06
 * Time: 下午3:56
 */
class BiqudaoParser : Parser {
    override fun parseSearchResult(source: Source, html: String): List<SearchBookPackage.BooksBean> {
        val list = mutableListOf<SearchBookPackage.BooksBean>()
        val doc = Jsoup.parse(html)
        val elements = doc.select("div#main div.novelslist2")
        val liList = elements.select("ul li")
        for (i in 1 until liList.size) {
            val category = liList[i].select("span.s1").first().text()
            val title = liList[i].select("span.s2 a").first().text()
            val id = liList[i].select("span.s2 a").attr("href")
            val lastChapter = liList[i].select("span.s3").first().text()
            val author = liList[i].select("span.s4").first().text()
            val book = SearchBookPackage.BooksBean()
            book.title = title
            book.author = author
            book.cat = category.substring(1, category.length - 1)
            book._id = source.sourceBaseUrl + id
            book.lastChapter = lastChapter
            list.add(book)
        }
        return list
    }

    override fun parseBookDetail(source: Source, html: String): BookDetailBean {
        val doc = Jsoup.parse(html)
        val elements = doc.select("div.box_con")
        //书籍信息
        val mainInfo = elements[0].select("div#maininfo")
        val info = mainInfo.select("div#info")
        val title = info.select("h1").first().text()
        val author = info.select("p").first().text()
        val updated = info.select("p")[2].text()
        val lastChapter = info.select("p")[3].select("a").text()
        val _id = info.select("p")[3].select("a").attr("href")
        val intro = mainInfo.select("div#intro").first().text()
        val cover = elements[0].select("div#sidebar div img").attr("src")
        val mBookDetailBean = BookDetailBean()
        mBookDetailBean._id = AndroidUtils.base64Encode(URLEncoder.encode(source.sourceBaseUrl + _id.substring(0, _id.lastIndexOf("/")), "utf-8"))
        mBookDetailBean.title = title
        mBookDetailBean.author = author.replace("作  者：", "")
        mBookDetailBean.lastChapter = lastChapter
        mBookDetailBean.longIntro = intro
        mBookDetailBean.cover = source.sourceBaseUrl + cover
        mBookDetailBean.cat = "暂无数据"
        mBookDetailBean.updated = updated.replace("最后更新：", "")
        //章节目录
        val chapterList = mutableListOf<BookChapterBean>()
        val chapterElement = elements[1].select("div#list dl").first().children()
        var dtCount = 0
        for (index in 0 until chapterElement.size) {
            val tagName = chapterElement[index].tagName()
            if ("dt".equals(tagName)) {
                dtCount++
            }
            if (dtCount < 2 || "dt" == tagName) {
                continue
            }
            val _title = chapterElement[index].text()
            val _link = chapterElement[index].select("a").attr("href")
            val bookChapterBean = BookChapterBean()
            bookChapterBean.title = _title
            bookChapterBean.link = source.sourceBaseUrl + _link
            bookChapterBean.id = MD5Utils.strToMd5By16(bookChapterBean.link)
            bookChapterBean.bookId = mBookDetailBean._id
            chapterList.add(bookChapterBean)
        }
        mBookDetailBean.bookChapterBeans = chapterList
        mBookDetailBean.chaptersCount = chapterList.size
        return mBookDetailBean
    }

    override fun parseChapterInfo(source: Source, html: String): ChapterInfoBean {
        val doc = Jsoup.parse(html)
        val content = doc.select("div.content_read div.box_con")
        val title = content.select("div.bookname h1").text()
        val body = content.select("#content").text()
        val chapterInfoBean = ChapterInfoBean()
        chapterInfoBean.title = title
        chapterInfoBean.body = body
        return chapterInfoBean
    }

}