package com.book.novel.presenter.contract

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.bean.packages.InterestedBookListPackage
import com.book.ireader.ui.base.BaseContract

/**
 * Created by newbiechen on 17-5-4.
 */

interface BookDetailContract {
    interface View : BaseContract.BaseView {
        fun finishRefresh(bean: BookDetailBean?)

        fun finishRecommendBooks(beans: List<InterestedBookListPackage.BookRecommendBean>)

        fun waitToBookShelf()

        fun errorToBookShelf()

        fun succeedToBookShelf()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun refreshBookDetail(bookId: String)

        fun refreshBookDetail(title: String, author: String)

        //添加到书架上
        fun addToBookShelf(collBook: CollBookBean)
    }
}
