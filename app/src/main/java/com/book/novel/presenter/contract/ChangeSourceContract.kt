package com.book.novel.presenter.contract

import com.book.ireader.model.bean.Source
import com.book.ireader.ui.base.BaseContract

/**
 * Created with author.
 * Description:
 * Date: 2018-08-07
 * Time: 上午10:39
 */
interface ChangeSourceContract : BaseContract {
    interface View : BaseContract.BaseView {

    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun startChange(oldsource: Source, newSource: Source)
    }
}