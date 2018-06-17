package com.book.novel.presenter

import com.book.ireader.App
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.RxUtils
import com.book.novel.presenter.contract.RankCategoryContract

/**
 * Created with author.
 * Description:
 * Date: 2018/6/16
 * Time: 22:34
 */
class RankCategoryPresenter : RxPresenter<RankCategoryContract.View>(), RankCategoryContract.Presenter {
    override fun load(rankName: String, gender: String, catId: String) {
        RemoteRepository.getInstance(App.getContext())
                .rankCategory(rankName, gender, catId)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        { bean ->
                            if (mView != null) { //viewpager 滑动后fragment可能已经被销毁
                                mView.show(bean)
                            }
                        }
                ) { e ->
                    e.printStackTrace()
                    if (mView != null) {
                        mView.showError()
                    }
                }
    }

}