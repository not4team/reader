package com.book.ireader.model.remote;

import com.book.ireader.model.bean.BillBookBean;
import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookDetailBean;
import com.book.ireader.model.bean.ChapterInfoBean;
import com.book.ireader.model.bean.RankTabBean;
import com.book.ireader.model.bean.packages.BookCityPackage;
import com.book.ireader.model.bean.packages.InterestedBookListPackage;
import com.book.ireader.model.bean.packages.RankCategoryPackage;
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

    public Single<RankCategoryPackage> rankCategoryPage(String rankName, String gender, String catId, int pageNum);

    public Single<List<BookChapterBean>> getBookChapters(String bookId);

    /**
     * 注意这里用的是同步请求
     *
     * @param url
     * @return
     */
    public Single<ChapterInfoBean> getChapterInfo(String url);

    /***************************************书籍详情**********************************************/
    public Single<BookDetailBean> getBookDetail(String bookId);

    public Single<List<InterestedBookListPackage.BookRecommendBean>> getRecommendBooks(String bookId);

    /**
     * 查询书籍
     *
     * @param query:书名|作者名
     * @return
     */
    public Single<List<BookDetailBean>> getSearchBooks(String query);
}
