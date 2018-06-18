package com.book.novel.utils

import android.util.Log
import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.model.bean.RankTabBean
import com.book.ireader.model.bean.packages.BookCityPackage
import com.book.ireader.model.bean.packages.RankCategoryPackage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
                billBookBean.cover = "https:$cover"
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
                billBookBean.cover = "https:$cover"
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
                billBookBean.cover = "https:$cover"
                billBookBean.title = title
                billBookBean.cat = category
                billBookBean.wordCount = wordCount
                billBookBean.shortIntro = shortInstro
                billBookBean.author = author
                billBookBeans.add(billBookBean)
            }
            return billBookBeans
        }

        fun parseRankCategoryPage(json: String): RankCategoryPackage? {
            val gson = Gson()
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val map: Map<String, Any> = gson.fromJson(json, type)
            val code: Double = map.get("code") as Double
            var rankCategoryPackage: RankCategoryPackage? = null
            if (code == 0.0) {
                val data = map["data"]
                Log.e("QidianParser", "data:$data")
                val dataMap: Map<String, Any> = gson.fromJson(gson.toJson(data), type)
                val total = dataMap["total"] as Double
                val isLast: Double = dataMap["isLast"] as Double
                val pageNum: Double = dataMap["pageNum"] as Double
                val records = dataMap["records"] as List<Any>
                rankCategoryPackage = RankCategoryPackage()
                rankCategoryPackage.total = total.toInt()
                rankCategoryPackage.isLast = isLast.toInt()
                rankCategoryPackage.pageNum = pageNum.toInt()
                val billBookBeans = mutableListOf<BillBookBean>()
                for (index in records.indices) {
                    val recordMap: Map<String, Any> = gson.fromJson(gson.toJson(records[index]), type)
                    val bid = recordMap["bid"] as Double
                    Log.e("QidianParser", "bid:${bid.toInt()}")
                    val bName = recordMap["bName"] as String
                    val bAuth = recordMap["bAuth"] as String
                    val desc = recordMap["desc"] as String
                    val cat = recordMap["cat"] as String
                    val cnt = recordMap["cnt"] as String
                    val billBookBean = BillBookBean()
                    billBookBean.title = bName
                    billBookBean.author = bAuth
                    billBookBean.shortIntro = desc
                    billBookBean.cat = cat
                    billBookBean.wordCount = cnt
                    billBookBean.cover = "https://qidian.qpic.cn/qdbimg/349573/${bid.toInt()}/150"
                    billBookBeans.add(billBookBean)
                }
                rankCategoryPackage.recoders = billBookBeans
            } else {
                //失败
            }
            return rankCategoryPackage
        }

    }
}