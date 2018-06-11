package com.book.ireader.model.remote;

import android.content.Context;

import com.book.ireader.model.bean.BillBookBean;
import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookCommentBean;
import com.book.ireader.model.bean.BookDetailBean;
import com.book.ireader.model.bean.BookHelpsBean;
import com.book.ireader.model.bean.BookListBean;
import com.book.ireader.model.bean.BookListDetailBean;
import com.book.ireader.model.bean.BookReviewBean;
import com.book.ireader.model.bean.BookTagBean;
import com.book.ireader.model.bean.ChapterInfoBean;
import com.book.ireader.model.bean.CommentBean;
import com.book.ireader.model.bean.CommentDetailBean;
import com.book.ireader.model.bean.HelpsDetailBean;
import com.book.ireader.model.bean.HotCommentBean;
import com.book.ireader.model.bean.ReviewDetailBean;
import com.book.ireader.model.bean.SortBookBean;
import com.book.ireader.model.bean.packages.BillBookPackage;
import com.book.ireader.model.bean.packages.BillboardPackage;
import com.book.ireader.model.bean.packages.BookCityPackage;
import com.book.ireader.model.bean.packages.BookSortPackage;
import com.book.ireader.model.bean.packages.BookSubSortPackage;
import com.book.ireader.model.bean.packages.InterestedBookListPackage;
import com.book.ireader.model.bean.packages.SearchBookPackage;
import com.book.ireader.utils.ManifestParser;

import java.lang.reflect.Proxy;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by newbiechen on 17-4-20.
 */

public class RemoteRepository implements IRemote {
    private static final String TAG = "RemoteRepository";

    private static volatile RemoteRepository sInstance;
    private IRemote mRemote;

    private RemoteRepository(Context mContext) throws Exception {
        List<IRemote> iRemotes = new ManifestParser(mContext).parse();
        if (iRemotes.size() == 0) {
            throw new Exception("AndroidManifext.xml not find meta-data which value is IRemoteModule");
        } else {
            mRemote = (IRemote) Proxy.newProxyInstance(RemoteRepository.class.getClassLoader(), new Class[]{IRemote.class}, new IRemoteProxy(iRemotes.get(0)));
        }
    }

    public static RemoteRepository getInstance(Context mContext) throws Exception {
        if (sInstance == null) {
            synchronized (RemoteRepository.class) {
                if (sInstance == null) {
                    sInstance = new RemoteRepository(mContext);
                }
            }
        }
        return sInstance;
    }

    @Override
    public Single<BookCityPackage> bookCity(String gender) {
        return mRemote.bookCity(gender);
    }

    @Override
    public Single<BillBookPackage> rank(String gender) {
        return mRemote.rank(gender);
    }

    public Single<List<BookChapterBean>> getBookChapters(String bookId) {
        return mRemote.getBookChapters(bookId);
    }

    /**
     * 注意这里用的是同步请求
     *
     * @param url
     * @return
     */
    public Single<ChapterInfoBean> getChapterInfo(String url) {
        return mRemote.getChapterInfo(url);
    }

    /***********************************************************************************/


    public Single<List<BookCommentBean>> getBookComment(String block, String sort, int start, int limit, String distillate) {

        return mRemote.getBookComment(block, sort, start, limit, distillate);
    }

    public Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limit, String distillate) {
        return mRemote.getBookHelps(sort, start, limit, distillate);
    }

    public Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate) {
        return mRemote.getBookReviews(sort, bookType, start, limited, distillate);
    }

    public Single<CommentDetailBean> getCommentDetail(String detailId) {
        return mRemote.getCommentDetail(detailId);
    }

    public Single<ReviewDetailBean> getReviewDetail(String detailId) {
        return mRemote.getReviewDetail(detailId);
    }

    public Single<HelpsDetailBean> getHelpsDetail(String detailId) {
        return mRemote.getHelpsDetail(detailId);
    }

    public Single<List<CommentBean>> getBestComments(String detailId) {
        return mRemote.getBestComments(detailId);
    }

    /**
     * 获取的是 综合讨论区的 评论
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailComments(String detailId, int start, int limit) {
        return mRemote.getDetailComments(detailId, start, limit);
    }

    /**
     * 获取的是 书评区和书荒区的 评论
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailBookComments(String detailId, int start, int limit) {
        return mRemote.getDetailBookComments(detailId, start, limit);
    }

    /*****************************************************************************/
    /**
     * 获取书籍的分类
     *
     * @return
     */
    public Single<BookSortPackage> getBookSortPackage() {
        return mRemote.getBookSortPackage();
    }

    /**
     * 获取书籍的子分类
     *
     * @return
     */
    public Single<BookSubSortPackage> getBookSubSortPackage() {
        return mRemote.getBookSubSortPackage();
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
    public Single<List<SortBookBean>> getSortBooks(String gender, String type, String major, String minor, int start, int limit) {
        return mRemote.getSortBooks(gender, type, major, minor, start, limit);
    }

    /*******************************************************************************/

    /**
     * 排行榜的类型
     *
     * @return
     */
    public Single<BillboardPackage> getBillboardPackage() {
        return mRemote.getBillboardPackage();
    }

    /**
     * 排行榜的书籍
     *
     * @param billId
     * @return
     */
    public Single<List<BillBookBean>> getBillBooks(String billId) {
        return mRemote.getBillBooks(billId);
    }

    /***********************************书单*************************************/

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
    public Single<List<BookListBean>> getBookLists(String duration, String sort,
                                                   int start, int limit,
                                                   String tag, String gender) {
        return mRemote.getBookLists(duration, sort, start, limit, tag, gender);
    }

    /**
     * 获取书单的标签|类型
     *
     * @return
     */
    public Single<List<BookTagBean>> getBookTags() {
        return mRemote.getBookTags();
    }

    /**
     * 获取书单的详情
     *
     * @param detailId
     * @return
     */
    public Single<BookListDetailBean> getBookListDetail(String detailId) {
        return mRemote.getBookListDetail(detailId);
    }

    /***************************************书籍详情**********************************************/
    public Single<BookDetailBean> getBookDetail(String bookId) {
        return mRemote.getBookDetail(bookId);
    }

    public Single<List<HotCommentBean>> getHotComments(String bookId) {
        return mRemote.getHotComments(bookId);
    }

    @Override
    public Single<List<InterestedBookListPackage.BookRecommendBean>> getRecommendBooks(String bookId) {
        return mRemote.getRecommendBooks(bookId);
    }

    public Single<List<BookListBean>> getRecommendBookList(String bookId, int limit) {
        return mRemote.getRecommendBookList(bookId, limit);
    }
    /********************************书籍搜索*********************************************/
    /**
     * 搜索热词
     *
     * @return
     */
    public Single<List<String>> getHotWords() {
        return mRemote.getHotWords();
    }

    /**
     * 搜索关键字
     *
     * @param query
     * @return
     */
    public Single<List<String>> getKeyWords(String query) {
        return mRemote.getKeyWords(query);
    }

    /**
     * 查询书籍
     *
     * @param query:书名|作者名
     * @return
     */
    public Single<List<SearchBookPackage.BooksBean>> getSearchBooks(String query) {
        return mRemote.getSearchBooks(query);
    }
}
