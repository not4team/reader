package com.book.novel.presenter

import com.book.ireader.ui.base.RxPresenter
import com.book.novel.presenter.contract.RankContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:40
 */
class RankPresenter() : RxPresenter<RankContract.View>(), RankContract.Presenter {
    override fun load(gender: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}