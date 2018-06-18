package com.book.novel.presenter

import com.book.ireader.App
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.RxUtils
import com.book.novel.presenter.contract.BookCityContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-05
 * Time: 下午5:00
 */
class BookCityPresenter() : RxPresenter<BookCityContract.View>(), BookCityContract.Presenter {
    override fun load(gender: String) {
        val disposable = RemoteRepository.getInstance(App.getContext())
                .bookCity(gender)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        { bean ->
                            if (bean.isEmpty) {
                                mView.showError()
                            } else {
                                mView.show(bean)
                            }
                        }
                ) { e ->
                    e.printStackTrace()
                    mView.showError()
                }
        addDisposable(disposable)
    }

}