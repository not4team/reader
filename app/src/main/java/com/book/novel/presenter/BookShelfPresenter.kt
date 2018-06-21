package com.book.novel.presenter

import com.book.ireader.App
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookRepository
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.RxUtils
import com.book.novel.presenter.contract.BookShelfContract
import io.reactivex.Single

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:32
 */
class BookShelfPresenter() : RxPresenter<BookShelfContract.View>(), BookShelfContract.Presenter {
    override fun updateCollBooks(collBookBeans: List<CollBookBean>) {
        Single.create<List<CollBookBean>> {
            val newCollBookBeans = mutableListOf<CollBookBean>()
            collBookBeans.forEach { oldBean ->
                RemoteRepository.getInstance(App.getContext())
                        .getBookDetail(oldBean._id)
                        .subscribe({ newBean ->
                            val newCollBookBean = newBean.collBookBean
                            if (oldBean.isUpdate() || oldBean.lastChapter != newCollBookBean.lastChapter) {
                                newCollBookBean.setUpdate(true)
                            } else {
                                newCollBookBean.setUpdate(false)
                            }
                            newCollBookBean.lastRead = oldBean.lastRead
                            newCollBookBean.bookOrder = oldBean.bookOrder
                            newCollBookBeans.add(newCollBookBean)
                        }) { e ->
                            e.printStackTrace()
                        }
            }
            BookRepository.getInstance().saveCollBooks(newCollBookBeans)
            it.onSuccess(newCollBookBeans)
        }.compose(RxUtils::toSimpleSingle)
                .subscribe({ collBookBeans ->
                    if (mView != null) {
                        mView.finishUpdate()
                    }
                }) { e ->
                    e.printStackTrace()
                    if (mView != null) {
                        mView.showError()
                    }
                }
    }

    override fun deleteCollBook(collBookBean: CollBookBean) {
        BookRepository.getInstance().deleteCollBookInRx(collBookBean).subscribe()
    }

    override fun refreshCollBooks(gender: String) {
        Single.create<List<CollBookBean>> {
            val collBooks = BookRepository.getInstance().orderBooks
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