package com.book.novel.presenter

import com.book.ireader.App
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookDao
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.MD5Utils
import com.book.ireader.utils.RxUtils
import com.book.novel.presenter.contract.BookDetailContract
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by newbiechen on 17-5-4.
 */

class BookDetailPresenter : RxPresenter<BookDetailContract.View>(), BookDetailContract.Presenter {
    val TAG = "BookDetailPresenter"
    private var bookId: String? = null

    override fun refreshBookDetail(bookId: String) {
        this.bookId = bookId
        refreshBook()
    }

    override fun refreshBookDetail(title: String, author: String) {
        getBookId(title, author)
    }

    override fun addToBookShelf(collBookBean: CollBookBean) {
        //设置 id
        for (bean in collBookBean.bookChapterList) {
            //相同的link只会插入一条数据
            bean.id = MD5Utils.strToMd5By16(bean.link)
        }
        //存储收藏
        BookDao.getInstance(App.getContext()).saveCollBookWithAsync(collBookBean)
        mView.succeedToBookShelf()
    }

    /**
     * [title]the book name
     * 根据书名查找，名字和作者一样视为同一本书
     */
    private fun getBookId(title: String, author: String) {
        val disposable = RemoteRepository.getInstance(App.getContext())
                .getSearchBooks(title).compose(RxUtils::toSimpleSingle)
                .subscribe({ beans ->
                    run breakTag@{
                        beans.forEach continueTag@{
                            if (title.equals(it.title) && author.equals(it.author)) {
                                this@BookDetailPresenter.bookId = it._id
                                refreshBook()
                                return@breakTag
                            }
                        }
                    }
                }) { e ->
                    e.printStackTrace()
                }
        addDisposable(disposable)
    }

    private fun refreshBook() {
        try {
            RemoteRepository
                    .getInstance(App.getContext())
                    .getBookDetail(bookId)
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(object : SingleObserver<BookDetailBean> {
                        override fun onSubscribe(d: Disposable) {
                            addDisposable(d)
                        }

                        override fun onSuccess(value: BookDetailBean) {
                            if (mView != null) {
                                mView.finishRefresh(value)
                                mView.complete()
                            }
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            if (mView != null) {
                                mView.showError()
                            }
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun refreshRecommend() {
        try {
            val disposable = RemoteRepository
                    .getInstance(App.getContext())
                    .getRecommendBooks(bookId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { value -> mView.finishRecommendBooks(value) }
            addDisposable(disposable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
