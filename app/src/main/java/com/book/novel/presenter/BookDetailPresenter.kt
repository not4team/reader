package com.book.novel.presenter

import com.book.ireader.App
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookRepository
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.LogUtils
import com.book.ireader.utils.MD5Utils
import com.book.novel.presenter.contract.BookDetailContract
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by newbiechen on 17-5-4.
 */

class BookDetailPresenter : RxPresenter<BookDetailContract.View>(), BookDetailContract.Presenter {
    private var bookId: String? = null

    override fun refreshBookDetail(bookId: String) {
        this.bookId = bookId
        refreshBook()
        refreshComment()
        refreshRecommend()

    }

    override fun addToBookShelf(collBookBean: CollBookBean) {
        var disposable: Disposable? = null
        try {
            disposable = RemoteRepository.getInstance(App.getContext())
                    .getBookChapters(collBookBean._id)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { d -> mView.waitToBookShelf() }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { beans ->

                                //设置 id
                                for (bean in beans) {
                                    bean.id = MD5Utils.strToMd5By16(bean.link)
                                }

                                //设置目录
                                collBookBean.bookChapters = beans
                                //存储收藏
                                BookRepository.getInstance()
                                        .saveCollBookWithAsync(collBookBean)

                                mView.succeedToBookShelf()
                            }
                    ) { e ->
                        mView.errorToBookShelf()
                        LogUtils.e(e)
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        addDisposable(disposable)
    }

    private fun refreshBook() {
        try {
            RemoteRepository
                    .getInstance(App.getContext())
                    .getBookDetail(bookId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<BookDetailBean> {
                        override fun onSubscribe(d: Disposable) {
                            addDisposable(d)
                        }

                        override fun onSuccess(value: BookDetailBean) {
                            mView.finishRefresh(value)
                            mView.complete()
                        }

                        override fun onError(e: Throwable) {
                            mView.showError()
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun refreshComment() {
        try {
            val disposable = RemoteRepository
                    .getInstance(App.getContext())
                    .getHotComments(bookId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { value -> mView.finishHotComment(value) }
            addDisposable(disposable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun refreshRecommend() {
        try {
            val disposable = RemoteRepository
                    .getInstance(App.getContext())
                    .getRecommendBookList(bookId, 3)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { value -> mView.finishRecommendBookList(value) }
            addDisposable(disposable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val TAG = "BookDetailPresenter"
    }
}
