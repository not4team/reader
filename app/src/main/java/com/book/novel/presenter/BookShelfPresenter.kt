package com.book.novel.presenter

import android.util.Log
import com.book.ireader.App
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.CollectDao
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.RxUtils
import com.book.novel.presenter.contract.BookShelfContract
import com.lereader.novel.BuildConfig
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:32
 */
class BookShelfPresenter : RxPresenter<BookShelfContract.View>(), BookShelfContract.Presenter {
    override fun updateCollBooks(collBookBeans: List<CollBookBean>) {
        Observable.create<CollBookBean> {
            updateCollectBook(collBookBeans, it)
        }.compose(RxUtils::toSimpleSingle)
                .subscribe({ collBook ->
                    if(BuildConfig.DEBUG) {
                        Log.e("BookShelfPresenter", "collBook title:" + collBook.title)
                    }
                    mView.finishUpdate(collBook)
                }, { error ->
                    error.printStackTrace()
                    mView?.showError()
                }) {
                    if(BuildConfig.DEBUG) {
                        Log.e("BookShelfPresenter", "onComplete")
                    }
                    mView?.complete()
                }
    }

    fun updateCollectBook(collBookBeans: List<CollBookBean>, parent: ObservableEmitter<CollBookBean>) {
        Observable.fromIterable(collBookBeans).subscribe({ oldBean ->
            RemoteRepository.getInstance(App.getContext())
                    .getBookDetail(oldBean.link)
                    .subscribe({ newBean ->
                        val newCollBookBean = newBean.collBookBean
                        if (oldBean.isUpdate() || oldBean.lastChapter != newCollBookBean.lastChapter) {
                            newCollBookBean.setUpdate(true)
                        } else {
                            newCollBookBean.setUpdate(false)
                        }
                        newCollBookBean.lastRead = oldBean.lastRead
                        newCollBookBean.bookOrder = oldBean.bookOrder
                        CollectDao.getInstance(App.getContext()).insertOrReplaceCollBook(newCollBookBean)
                        parent.onNext(newCollBookBean)
                    }) { e ->
                        e.printStackTrace()
                    }
        }, { error ->
            parent.onError(error)
        }) {
            parent.onComplete()
        }
    }

    override fun deleteCollBook(collBookBean: CollBookBean) {
        CollectDao.getInstance(App.getContext()).deleteCollBookInRx(collBookBean).subscribe()
    }

    override fun refreshCollBooks(gender: String) {
        Single.create<List<CollBookBean>> {
            val collBooks = CollectDao.getInstance(App.getContext()).orderBooks
            it.onSuccess(collBooks)
        }.compose(RxUtils::toSimpleSingle)
                .subscribe({ collBooks ->
                    mView.show(collBooks)
                }) { e ->
                    e.printStackTrace()
                    mView.showError()
                }
    }
}