package com.book.novel.presenter.contract

import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.ui.base.BaseContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:33
 */
interface BookShelfContract : BaseContract {
    interface View : BaseContract.BaseView {
        fun show(collBookBeans: List<CollBookBean>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun refreshCollBooks(gender: String)
        fun deleteCollBook(collBookBean: CollBookBean)
    }
}