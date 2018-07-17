package com.book.ireader.model.remote;

import android.content.Context;

import com.book.ireader.model.bean.BillBookBean;
import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookDetailBean;
import com.book.ireader.model.bean.ChapterInfoBean;
import com.book.ireader.model.bean.RankTabBean;
import com.book.ireader.model.bean.packages.BookCityPackage;
import com.book.ireader.model.bean.packages.InterestedBookListPackage;
import com.book.ireader.model.bean.packages.RankCategoryPackage;
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
    public Single<List<RankTabBean>> rank(String rankName, String gender) {
        return mRemote.rank(rankName, gender);
    }

    @Override
    public Single<List<BillBookBean>> rankCategory(String rankName, String gender, String catId) {
        return mRemote.rankCategory(rankName, gender, catId);
    }

    @Override
    public Single<RankCategoryPackage> rankCategoryPage(String rankName, String gender, String catId, int pageNum) {
        return mRemote.rankCategoryPage(rankName, gender, catId, pageNum);
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

    /***************************************书籍详情**********************************************/
    public Single<BookDetailBean> getBookDetail(String bookId) {
        return mRemote.getBookDetail(bookId);
    }

    @Override
    public Single<List<InterestedBookListPackage.BookRecommendBean>> getRecommendBooks(String bookId) {
        return mRemote.getRecommendBooks(bookId);
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
