package com.book.ireader.model.remote;

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
import com.book.ireader.model.bean.RankTabBean;
import com.book.ireader.model.bean.ReviewDetailBean;
import com.book.ireader.model.bean.SortBookBean;
import com.book.ireader.model.bean.packages.BillboardPackage;
import com.book.ireader.model.bean.packages.BookCityPackage;
import com.book.ireader.model.bean.packages.BookSortPackage;
import com.book.ireader.model.bean.packages.BookSubSortPackage;
import com.book.ireader.model.bean.packages.InterestedBookListPackage;
import com.book.ireader.model.bean.packages.SearchBookPackage;

import java.util.List;

import io.reactivex.Single;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-05
 * Time: 下午2:37
 */
public interface IRemote {

    public Single<BookCityPackage> bookCity(String gender);

    public Single<List<RankTabBean>> rank(String rankName, String gender);

    public Single<List<BillBookBean>> rankCategory(String rankName, String gender, String catId);

    public Single<List<BookChapterBean>> getBookChapters(String bookId);

    /**
     * 注意这里用的是同步请求
     *
     * @param url
     * @return
     */
    public Single<ChapterInfoBean> getChapterInfo(String url);

    public Single<List<BookCommentBean>> getBookComment(String block, String sort, int start, int limit, String distillate);

    public Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limit, String distillate);

    public Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate);

    public Single<CommentDetailBean> getCommentDetail(String detailId);

    public Single<ReviewDetailBean> getReviewDetail(String detailId);

    public Single<HelpsDetailBean> getHelpsDetail(String detailId);

    public Single<List<CommentBean>> getBestComments(String detailId);

    /**
     * 获取的是 综合讨论区的 评论
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailComments(String detailId, int start, int limit);

    /**
     * 获取的是 书评区和书荒区的 评论
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailBookComments(String detailId, int start, int limit);

    /*****************************************************************************/
    /**
     * 获取书籍的分类
     *
     * @return
     */
    public Single<BookSortPackage> getBookSortPackage();

    /**
     * 获取书籍的子分类
     *
     * @return
     */
    public Single<BookSubSortPackage> getBookSubSortPackage();

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
    public Single<List<SortBookBean>> getSortBooks(String gender, String type, String major, String minor, int start, int limit);

    /*******************************************************************************/

    /**
     * 排行榜的类型
     *
     * @return
     */
    public Single<BillboardPackage> getBillboardPackage();

    /**
     * 排行榜的书籍
     *
     * @param billId
     * @return
     */
    public Single<List<BillBookBean>> getBillBooks(String billId);

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
                                                   String tag, String gender);

    /**
     * 获取书单的标签|类型
     *
     * @return
     */
    public Single<List<BookTagBean>> getBookTags();

    /**
     * 获取书单的详情
     *
     * @param detailId
     * @return
     */
    public Single<BookListDetailBean> getBookListDetail(String detailId);

    /***************************************书籍详情**********************************************/
    public Single<BookDetailBean> getBookDetail(String bookId);

    public Single<List<HotCommentBean>> getHotComments(String bookId);

    public Single<List<InterestedBookListPackage.BookRecommendBean>> getRecommendBooks(String bookId);

    public Single<List<BookListBean>> getRecommendBookList(String bookId, int limit);
    /********************************书籍搜索*********************************************/
    /**
     * 搜索热词
     *
     * @return
     */
    public Single<List<String>> getHotWords();

    /**
     * 搜索关键字
     *
     * @param query
     * @return
     */
    public Single<List<String>> getKeyWords(String query);

    /**
     * 查询书籍
     *
     * @param query:书名|作者名
     * @return
     */
    public Single<List<SearchBookPackage.BooksBean>> getSearchBooks(String query);
}
