package com.book.novel.model.remote

import android.util.Log
import com.book.ireader.model.bean.*
import com.book.ireader.model.bean.packages.BookCityPackage
import com.book.ireader.model.bean.packages.InterestedBookListPackage
import com.book.ireader.model.bean.packages.RankCategoryPackage
import com.book.ireader.model.remote.IRemote
import com.book.ireader.utils.Constant
import com.book.ireader.utils.SharedPreUtils
import com.book.novel.utils.GsonParser
import com.book.novel.utils.ParserManager
import com.book.novel.utils.QidianParser
import com.lereader.novel.BuildConfig
import io.reactivex.Single
import retrofit2.Retrofit
import java.net.URLEncoder


/**
 * Created by newbiechen on 17-4-20.
 */

class RemoteImpl : IRemote {
    private val TAG = "RemoteImpl"
    private val mRetrofit: Retrofit
    private val mBookApi: BookApi

    init {
        mRetrofit = RemoteHelper.instance.retrofit
        mBookApi = mRetrofit.create(BookApi::class.java)
    }

    override fun bookCity(gender: String): Single<BookCityPackage> {
        return mBookApi.bookCity(gender).map { bean ->
            QidianParser.parseHome(bean.string())
        }
    }

    override fun rank(rankName: String, gender: String): Single<List<RankTabBean>> {
        return mBookApi.rank(rankName, gender).map { bean ->
            QidianParser.parseRankDefault(bean.string(), rankName, gender)
        }
    }

    override fun rankCategory(rankName: String, gender: String, catId: String): Single<List<BillBookBean>> {
        return mBookApi.rankCategory(rankName, gender, gender, catId).map { bean ->
            QidianParser.parseRankCategory(bean.string())
        }
    }

    override fun rankCategoryPage(rankName: String, gender: String, catId: String, pageNum: Int): Single<RankCategoryPackage> {
        return mBookApi.rankCategoryPage(rankName, gender, catId, pageNum).map { bean ->
            QidianParser.parseRankCategoryPage(bean.string(), gender)
        }
    }

    override fun getBookChapters(bookLink: String): Single<List<BookChapterBean>> {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(TAG, "getBookChapters bookId:" + bookLink)
        }
        return mBookApi.getBookChapterPackage(bookLink)
                .map { bean ->
                    val url = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
                    val source = ParserManager.getSource(url) ?: ParserManager.getDefaultSource()
                    val html = String(bean.bytes(), charset(source.sourceEncode))
                    val parser = ParserManager.getParser(source.getSourceBaseUrl())
                    parser?.parseBookChapter(source, html)
                }
    }

    /**
     * 注意这里用的是同步请求
     *
     * @param url
     * @return
     */
    override fun getChapterInfo(url: String): Single<ChapterInfoBean> {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(TAG, "getChapterInfo url:" + url)
        }
        return mBookApi.getChapterInfoPackage(url)
                .map { bean ->
                    val url = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
                    val source = ParserManager.getSource(url) ?: ParserManager.getDefaultSource()
                    val html = String(bean.bytes(), charset(source.sourceEncode))
                    val parser = ParserManager.getParser(source.getSourceBaseUrl())
                    parser?.parseChapterInfo(source, html)
                }
    }

    /***************************************书籍详情 */
    override fun getBookDetail(link: String): Single<BookDetailBean> {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(TAG, "getBookDetail bookId:" + link)
        }
        return mBookApi.getBookDetail(link).map { bean ->
            val url = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
            val source = ParserManager.getSource(url) ?: ParserManager.getDefaultSource()
            val html = String(bean.bytes(), charset(source.sourceEncode))
            val parser = ParserManager.getParser(source.getSourceBaseUrl())
            parser?.parseBookDetail(source, html)
        }
    }

    override fun getRecommendBooks(bookId: String): Single<MutableList<InterestedBookListPackage.BookRecommendBean>> {
        return mBookApi.getBookDetailRecommend(bookId).map { bean ->
            GsonParser.jsonConvert(bean.string(), InterestedBookListPackage::class.java).books
        }
    }

    /**
     * 查询书籍
     *
     * @param query:书名|作者名
     * @return
     */
    override fun getSearchBooks(query: String): Single<List<BookDetailBean>> {
        val url = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
        val source = ParserManager.getSource(url) ?: ParserManager.getDefaultSource()
        val encode = source.getSourceEncode()
        var encodeQuery = query
        if (encode != null) {
            encodeQuery = URLEncoder.encode(query, encode)
        }
        val param = mutableMapOf<String, String>()
        param.put(source.sourceSearchParam, encodeQuery)
        var searchUrl = source.sourceSearchUrl + "?" + source.sourceSearchParam + "=" + encodeQuery
        if (source.sourceSearchMethod == "get") {
            return mBookApi.getSearchBookPackage(searchUrl)
                    .map { bean ->
                        if (BuildConfig.LOG_DEBUG) {
                            Log.e("RemoteHelper", "search success get")
                        }
                        val parser = ParserManager.getParser(source.getSourceBaseUrl())
                        parser?.parseSearchResult(source, bean.string())
                    }
        } else {
            return mBookApi.getSearchBookPackagePost(source.getSourceSearchUrl(), param)
                    .map { bean ->
                        if (BuildConfig.LOG_DEBUG) {
                            Log.e("RemoteHelper", "search success post")
                        }
                        val html = String(bean.bytes(), charset(encode))
                        val parser = ParserManager.getParser(source.getSourceBaseUrl())
                        parser?.parseSearchResult(source, html)
                    }
        }
    }
}
