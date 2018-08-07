package com.book.novel.presenter

import com.book.ireader.model.bean.Source
import com.book.ireader.ui.base.RxPresenter
import com.book.novel.presenter.contract.ChangeSourceContract

/**
 * Created with author.
 * Description:
 * Date: 2018-08-07
 * Time: 上午10:39
 */
class ChangeSourcePresenter : RxPresenter<ChangeSourceContract.View>(), ChangeSourceContract.Presenter {
    override fun startChange(source: Source) {

    }

}