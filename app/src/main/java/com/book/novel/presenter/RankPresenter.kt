package com.book.novel.presenter

import com.book.ireader.App
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.RxUtils
import com.book.novel.presenter.contract.RankContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:40
 */
class RankPresenter() : RxPresenter<RankContract.View>(), RankContract.Presenter {
    override fun load(rankName: String, gender: String) {
        val disposable = RemoteRepository.getInstance(App.getContext())
                .rank(rankName, gender)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        { bean ->
                            mView.show(bean)
                        }
                ) { e ->
                    e.printStackTrace()
                    mView.showError()
                }
        addDisposable(disposable)
    }
}