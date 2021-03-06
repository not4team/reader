package com.book.novel.presenter.contract

import com.book.ireader.model.bean.packages.BookCityPackage
import com.book.ireader.ui.base.BaseContract

/**
 * Created by newbiechen on 17-5-16.
 */

interface BookCityContract : BaseContract {
    interface View : BaseContract.BaseView {
        fun show(bookCityPackage: BookCityPackage)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun load(gender: String)
    }
}
