package com.book.novel.utils

import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.model.bean.RankTabBean
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
            val content = doc.select("div.content").first()
            val hotElement = content.select("div[data-l1=4]").first()
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
            val newElement = content.select("div[data-l1=8]").first()
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
            val finishedElement = content.select("div[data-l1=9]").first()
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

        fun parseRankDefault(html: String, rank: String, gender: String): List<RankTabBean> {
            val doc = Jsoup.parse(html)
            val rankTabBeans = mutableListOf<RankTabBean>()
            val rankWrapper = doc.select("div#rankWrapper").first()
            rankWrapper.select("div.toplist-tag a").forEach {
                val tab = it.text()
                //https://m.qidian.com/rank/hotsales/male?gender=male&catId=21
                val catId = it.attr("data-value")
                val rankTabBean = RankTabBean()
                rankTabBean.tab = tab
                rankTabBean.rank = rank
                rankTabBean.gender = gender
                rankTabBean.catId = catId
                rankTabBeans.add(rankTabBean)
            }
            val billBookBeans = mutableListOf<BillBookBean>()
            rankWrapper.select("li.book-li").forEach {
                val bookLayout = it.select("a.book-layout").first()
                val cover = bookLayout.select("img.book-cover").attr("data-src")
                val title = bookLayout.select("div.book-title-x h4").text()
                val category = bookLayout.select("p.book-tags em")[0].text()
                val wordCount = bookLayout.select("p.book-tags em")[1].text()
                val shortInstro = bookLayout.select("p.book-desc").text()
                val author = bookLayout.select("div.book-meta span").text()
                val billBookBean = BillBookBean()
                billBookBean.cover = "https:" + cover
                billBookBean.title = title
                billBookBean.cat = category
                billBookBean.wordCount = wordCount
                billBookBean.shortIntro = shortInstro
                billBookBean.author = author
                billBookBeans.add(billBookBean)
            }
            if (rankTabBeans.isNotEmpty()) {
                rankTabBeans[0].billBookBeans = billBookBeans
            }
            return rankTabBeans
        }

        fun parseRankCategory(html: String): List<BillBookBean> {
            val doc = Jsoup.parse(html)
            val billBookBeans = mutableListOf<BillBookBean>()
            val rankWrapper = doc.select("div#rankWrapper").first()
            rankWrapper.select("li.book-li").forEach {
                val bookLayout = it.select("a.book-layout").first()
                val cover = bookLayout.select("img.book-cover").attr("data-src")
                val title = bookLayout.select("div.book-title-x h4").text()
                val category = bookLayout.select("p.book-tags em")[0].text()
                val wordCount = bookLayout.select("p.book-tags em")[1].text()
                val shortInstro = bookLayout.select("p.book-desc").text()
                val author = bookLayout.select("div.book-meta span").text()
                val billBookBean = BillBookBean()
                billBookBean.cover = "https:" + cover
                billBookBean.title = title
                billBookBean.cat = category
                billBookBean.wordCount = wordCount
                billBookBean.shortIntro = shortInstro
                billBookBean.author = author
                billBookBeans.add(billBookBean)
            }
            return billBookBeans
        }
    }
}