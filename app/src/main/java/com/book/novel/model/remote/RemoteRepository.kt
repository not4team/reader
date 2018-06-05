package com.book.novel.model.remote

import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.model.bean.BookCommentBean
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.BookHelpsBean
import com.book.ireader.model.bean.BookListBean
import com.book.ireader.model.bean.BookListDetailBean
import com.book.ireader.model.bean.BookReviewBean
import com.book.ireader.model.bean.BookTagBean
import com.book.ireader.model.bean.ChapterInfoBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.bean.CommentBean
import com.book.ireader.model.bean.CommentDetailBean
import com.book.ireader.model.bean.HelpsDetailBean
import com.book.ireader.model.bean.HotCommentBean
import com.book.ireader.model.bean.ReviewDetailBean
import com.book.ireader.model.bean.SortBookBean
import com.book.ireader.model.bean.packages.BillboardPackage
import com.book.ireader.model.bean.packages.BookSortPackage
import com.book.ireader.model.bean.packages.BookSubSortPackage
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.model.remote.IRemote

import io.reactivex.Single
import retrofit2.Retrofit

/**
 * Created by newbiechen on 17-4-20.
 */

class RemoteRepository private constructor() : IRemote {
    private val mRetrofit: Retrofit
    private val mBookApi: BookApi

    init {
        mRetrofit = RemoteHelper.instance
                .retrofit

        mBookApi = mRetrofit.create(BookApi::class.java)
    }

    override fun getRecommendBooks(gender: String): Single<List<CollBookBean>> {
        return mBookApi.getRecommendBookPackage(gender)
                .map { bean ->
                    //                    bean.getBooks()
                    null
                }
    }

    override fun getBookChapters(bookId: String): Single<List<BookChapterBean>> {
        return mBookApi.getBookChapterPackage(bookId, "chapter")
                .map { bean -> null }
    }

    /**
     * 注意这里用的是同步请求
     *
     * @param url
     * @return
     */
    override fun getChapterInfo(url: String): Single<ChapterInfoBean> {
        return mBookApi.getChapterInfoPackage(url)
                .map { bean -> null }
    }

    /** */


    override fun getBookComment(block: String, sort: String, start: Int, limit: Int, distillate: String): Single<List<BookCommentBean>> {

        return mBookApi.getBookCommentList(block, "all", sort, "all", start.toString() + "", limit.toString() + "", distillate)
                .map { listBean -> null }
    }

    override fun getBookHelps(sort: String, start: Int, limit: Int, distillate: String): Single<List<BookHelpsBean>> {
        return mBookApi.getBookHelpList("all", sort, start.toString() + "", limit.toString() + "", distillate)
                .map { listBean -> null }
    }

    override fun getBookReviews(sort: String, bookType: String, start: Int, limited: Int, distillate: String): Single<List<BookReviewBean>> {
        return mBookApi.getBookReviewList("all", sort, bookType, start.toString() + "", limited.toString() + "", distillate)
                .map { listBean -> null }
    }

    override fun getCommentDetail(detailId: String): Single<CommentDetailBean> {
        return mBookApi.getCommentDetailPackage(detailId)
                .map { bean -> null }
    }

    override fun getReviewDetail(detailId: String): Single<ReviewDetailBean> {
        return mBookApi.getReviewDetailPacakge(detailId)
                .map { bean -> null }
    }

    override fun getHelpsDetail(detailId: String): Single<HelpsDetailBean> {
        return mBookApi.getHelpsDetailPackage(detailId)
                .map { bean -> null }
    }

    override fun getBestComments(detailId: String): Single<List<CommentBean>> {
        return mBookApi.getBestCommentPackage(detailId)
                .map { bean -> null }
    }

    /**
     * 获取的是 综合讨论区的 评论
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    override fun getDetailComments(detailId: String, start: Int, limit: Int): Single<List<CommentBean>> {
        return mBookApi.getCommentPackage(detailId, start.toString() + "", limit.toString() + "")
                .map { bean -> null }
    }

    /**
     * 获取的是 书评区和书荒区的 评论
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    override fun getDetailBookComments(detailId: String, start: Int, limit: Int): Single<List<CommentBean>> {
        return mBookApi.getBookCommentPackage(detailId, start.toString() + "", limit.toString() + "")
                .map { bean -> null }
    }

    /** */
    /**
     * 获取书籍的分类
     *
     * @return
     */
    override fun getBookSortPackage(): Single<BookSortPackage> {
        return mBookApi.bookSortPackage.map { bean -> null }
    }

    /**
     * 获取书籍的子分类
     *
     * @return
     */
    override fun getBookSubSortPackage(): Single<BookSubSortPackage> {
        return mBookApi.bookSubSortPackage.map { bean -> null }
    }

    /**
     * 根据分类获取书籍列表
     *
     * @param gender
     * @param type
     * @param major
     * @param minor
     * @param start
     * @param limit
     * @return
     */
    override fun getSortBooks(gender: String, type: String, major: String, minor: String, start: Int, limit: Int): Single<List<SortBookBean>> {
        return mBookApi.getSortBookPackage(gender, type, major, minor, start, limit)
                .map { bean -> null }
    }

    /** */

    /**
     * 排行榜的类型
     *
     * @return
     */
    override fun getBillboardPackage(): Single<BillboardPackage> {
        return mBookApi.billboardPackage.map { bean -> null }
    }

    /**
     * 排行榜的书籍
     *
     * @param billId
     * @return
     */
    override fun getBillBooks(billId: String): Single<List<BillBookBean>> {
        return mBookApi.getBillBookPackage(billId)
                .map { bean -> null }
    }

    /***********************************书单 */

    /**
     * 获取书单列表
     *
     * @param duration
     * @param sort
     * @param start
     * @param limit
     * @param tag
     * @param gender
     * @return
     */
    override fun getBookLists(duration: String, sort: String,
                              start: Int, limit: Int,
                              tag: String, gender: String): Single<List<BookListBean>> {
        return mBookApi.getBookListPackage(duration, sort, start.toString() + "", limit.toString() + "", tag, gender)
                .map { bean -> null }
    }

    /**
     * 获取书单的标签|类型
     *
     * @return
     */
    override fun getBookTags(): Single<List<BookTagBean>> {
        return mBookApi.bookTagPackage
                .map { bean -> null }
    }

    /**
     * 获取书单的详情
     *
     * @param detailId
     * @return
     */
    override fun getBookListDetail(detailId: String): Single<BookListDetailBean> {
        return mBookApi.getBookListDetailPackage(detailId)
                .map { bean -> null }
    }

    /***************************************书籍详情 */
    override fun getBookDetail(bookId: String): Single<BookDetailBean> {
        return mBookApi.getBookDetail(bookId).map { bean -> null }
    }

    override fun getHotComments(bookId: String): Single<List<HotCommentBean>> {
        return mBookApi.getHotCommnentPackage(bookId)
                .map { bean -> null }
    }

    override fun getRecommendBookList(bookId: String, limit: Int): Single<List<BookListBean>> {
        return mBookApi.getRecommendBookListPackage(bookId, limit.toString() + "")
                .map { bean -> null }
    }
    /********************************书籍搜索 */
    /**
     * 搜索热词
     *
     * @return
     */
    override fun getHotWords(): Single<List<String>> {
        return mBookApi.hotWordPackage
                .map { bean -> null }
    }

    /**
     * 搜索关键字
     *
     * @param query
     * @return
     */
    override fun getKeyWords(query: String): Single<List<String>> {
        return mBookApi.getKeyWordPacakge(query)
                .map { bean -> null }

    }

    /**
     * 查询书籍
     *
     * @param query:书名|作者名
     * @return
     */
    override fun getSearchBooks(query: String): Single<List<SearchBookPackage.BooksBean>> {
        return mBookApi.getSearchBookPackage(query)
                .map { bean -> null }
    }

    companion object {
        private val TAG = "RemoteRepository"

        private var sInstance: RemoteRepository? = null

        val instance: RemoteRepository
            get() {
                if (sInstance == null) {
                    synchronized(RemoteHelper::class.java) {
                        if (sInstance == null) {
                            sInstance = RemoteRepository()
                        }
                    }
                }
                return sInstance!!
            }
    }
}
