package com.book.novel.utils

import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.model.bean.packages.BookCityPackage
import org.jsoup.Jsoup

/**
 * Created with author.
 * Description:
 * Date: 2018-06-07
 * Time: 上午11:23
 */
class QidianParser {
    companion object {
        fun parseHome(html: String): BookCityPackage? {
            val bookCityPackage = BookCityPackage();
            val doc = Jsoup.parse(html)
            val hotElement = doc.select("div[data-l1=4]").first()
            hotElement.select("li.module-slide-li").forEach { element ->
                val cover = element.select("img.module-slide-img").first().attr("data-src")
                val title = element.select("figcaption").first().text()
                val author = element.select("span.gray").first().text()
                val billBookBean = BillBookBean()
                billBookBean.title = title
                billBookBean.cover = "https:" + cover
                billBookBean.author = author
                bookCityPackage.hotBooks.add(billBookBean)
            }
            val newElement = doc.select("div[data-l1=8]").first()
            newElement.select("li.book-li").forEach { element ->
                val cover = element.select("img.book-cover").first().attr("data-src")
                val title = element.select("h4.book-title").first().text()
                val desc = element.select("p.book-desc").first().text()
                val author = element.select("span.book-author").first().text()
                val billBookBean = BillBookBean()
                billBookBean.title = title
                billBookBean.cover = "https:" + cover
                billBookBean.author = author.replace("作者：", "")
                billBookBean.shortIntro = desc
                bookCityPackage.newBooks.add(billBookBean)
            }
            val finishedElement = doc.select("div[data-l1=9]").first()
            finishedElement.select("li.book-li").forEach { element ->
                val cover = element.select("img.book-cover").first().attr("data-src")
                val title = element.select("h4.book-title").first().text()
                val desc = element.select("p.book-desc").first().text()
                val author = element.select("span.book-author").first().text()
                val billBookBean = BillBookBean()
                billBookBean.title = title
                billBookBean.cover = "https:" + cover
                billBookBean.author = author.replace("作者：", "")
                billBookBean.shortIntro = desc
                bookCityPackage.finishedBooks.add(billBookBean)
            }
            return bookCityPackage
        }
    }
}