package com.book.novel.model.remote

import android.util.Log
import com.book.ireader.model.bean.*
import com.book.ireader.model.bean.packages.BookCityPackage
import com.book.ireader.model.bean.packages.InterestedBookListPackage
import com.book.ireader.model.bean.packages.RankCategoryPackage
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.model.remote.IRemote
import com.book.ireader.utils.Constant
import com.book.ireader.utils.SharedPreUtils
import com.book.novel.utils.AndroidUtils
import com.book.novel.utils.GsonParser
import com.book.novel.utils.ParserManager
import com.book.novel.utils.QidianParser
import com.lereader.novel.BuildConfig
import io.reactivex.Single
import retrofit2.Retrofit
import java.net.URLDecoder
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

    override fun getBookChapters(bookId: String): Single<List<BookChapterBean>> {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(TAG, "getBookChapters bookId:" + bookId)
        }
        val url = URLDecoder.decode(AndroidUtils.base64Decode(bookId), "utf-8")
        return mBookApi.getBookChapterPackage(url)
                .map { bean ->
                    val url = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
                    val source = ParserManager.getSource(url) ?: ParserManager.getDefaultSource()
                    val parser = ParserManager.getParser(source.getSourceBaseUrl())
                    parser?.parseBookDetail(bean.string())?.bookChapterBeans
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
                    val parser = ParserManager.getParser(source.getSourceBaseUrl())
                    parser?.parseChapterInfo(bean.string())
                }
    }

    /***************************************书籍详情 */
    override fun getBookDetail(bookId: String): Single<BookDetailBean> {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(TAG, "getBookDetail bookId:" + bookId)
        }
        return mBookApi.getBookDetail(bookId).map { bean ->
            val url = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
            val source = ParserManager.getSource(url) ?: ParserManager.getDefaultSource()
            val parser = ParserManager.getParser(source.getSourceBaseUrl())
            parser?.parseBookDetail(bean.string())
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
    override fun getSearchBooks(query: String): Single<List<SearchBookPackage.BooksBean>> {
        val url = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
        val source = ParserManager.getSource(url) ?: ParserManager.getDefaultSource()
        val encode = source.getSourceEncode()
        var encodeQuery = query
        if (encode != null) {
            encodeQuery = URLEncoder.encode(query, encode)
        }
        Log.e("RemoteHelper", "encodeQuery:" + encodeQuery)
        return mBookApi.getSearchBookPackage(source.getSourceSearchUrl() + encodeQuery)
                .map { bean ->
                    Log.e("RemoteHelper", "search success")
                    val parser = ParserManager.getParser(source.getSourceBaseUrl())
                    parser?.parseSearchResult(bean.string())
                }
    }
}
