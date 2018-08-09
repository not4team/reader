package com.book.novel.presenter

import android.util.Log
import com.book.ireader.App
import com.book.ireader.RxBus
import com.book.ireader.event.BookShelfRefreshEvent
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.bean.Source
import com.book.ireader.model.local.BookDao
import com.book.ireader.model.local.CollectDao
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.Constant
import com.book.ireader.utils.RxUtils
import com.book.ireader.utils.SharedPreUtils
import com.book.novel.presenter.contract.ChangeSourceContract
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * Created with author.
 * Description:
 * Date: 2018-08-07
 * Time: 上午10:39
 */
class ChangeSourcePresenter : RxPresenter<ChangeSourceContract.View>(), ChangeSourceContract.Presenter {
    override fun startChange(oldSource: Source, newSource: Source) {
        val disposable = Observable.create<CollBookBean> {
            updateCollectBook(it)
        }.compose(RxUtils::toSimpleSingle)
                .subscribe({ collBook ->
                    Log.e("ChangeSourcePresenter", "collBook title:" + collBook.title)
                }, { error ->
                    error.printStackTrace()
                    SharedPreUtils.getInstance().putString(Constant.SHARED_BOOK_SOURCE, oldSource.sourceBaseUrl)
                    mView.showError()
                }) {
                    Log.e("ChangeSourcePresenter", "onComplete")
                    BookDao.releaseDB()
                    mView.complete()
                    RxBus.getInstance().post(BookShelfRefreshEvent().setType(BookShelfRefreshEvent.EVENT_TYPE_UPDATE))
                }
        addDisposable(disposable)
    }

    fun updateCollectBook(parent: ObservableEmitter<CollBookBean>) {
        //获取所有书架书籍
        val collBookBeans = CollectDao.getInstance(App.getContext()).orderBooks
        Observable.fromIterable(collBookBeans).subscribe({ oldBean ->
            RemoteRepository.getInstance(App.getContext())
                    .getSearchBooks(oldBean.title)
                    .subscribe({ books ->
                        run breakTag@{
                            books.forEach continueTag@{
                                if (oldBean.title.equals(it.title) && oldBean.author.equals(it.author)) {
                                    oldBean.link = it.link
                                    oldBean.lastChapter = it.lastChapter
                                    oldBean.category = it.cat
                                    oldBean.chapterDir = it.chapterDir
                                    CollectDao.getInstance(App.getContext()).insertOrReplaceCollBook(oldBean)
                                    parent.onNext(oldBean)
                                    return@breakTag
                                }
                            }
                        }
                    }) { error ->
                        error.printStackTrace()
                    }
        }, { error ->
            parent.onError(error)
        }) {
            parent.onComplete()
        }
    }
}