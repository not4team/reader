package com.book.novel.presenter.contract

import com.book.ireader.model.bean.RankTabBean
import com.book.ireader.ui.base.BaseContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:39
 */
interface RankContract : BaseContract {
    interface View : BaseContract.BaseView {
        fun show(rankTabBeans: List<RankTabBean>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun load(rankName: String, gender: String)
    }
}