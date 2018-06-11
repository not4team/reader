package com.book.novel.presenter

import android.util.Log
import com.book.ireader.App
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookRepository
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.LogUtils
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
        refreshRecommend()
    }

    override fun refreshBookDetail(title: String, author: String) {
        getBookId(title, author)
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

    /**
     * [title]the book name
     * 根据书名查找，名字和作者一样视为同一本书
     */
    private fun getBookId(title: String, author: String) {
        RemoteRepository.getInstance(App.getContext())
                .getSearchBooks(title).compose(RxUtils::toSimpleSingle)
                .subscribe({ beans ->
                    run breakTag@{
                        beans.forEach continueTag@{
                            Log.e(TAG, "title:" + title + ",author:" + author + " it.title:" + it.title + "it.author" + it.author)
                            if (title.equals(it.title) && author.equals(it.author)) {
                                this@BookDetailPresenter.bookId = it._id
                                refreshBook()
                                refreshRecommend()
                                return@breakTag
                            }
                        }
                    }
                }) { e ->
                    e.printStackTrace()
                }
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
                            mView.finishRefresh(value)
                            mView.complete()
                        }

                        override fun onError(e: Throwable) {
                            mView.showError()
                            e.printStackTrace()
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

    companion object {
        private val TAG = "BookDetailPresenter"
    }
}
