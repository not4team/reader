package com.book.novel.presenter.contract

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.BookListBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.bean.HotCommentBean
import com.book.ireader.ui.base.BaseContract

/**
 * Created by newbiechen on 17-5-4.
 */

interface BookDetailContract {
    interface View : BaseContract.BaseView {
        fun finishRefresh(bean: BookDetailBean)

        fun finishHotComment(beans: List<HotCommentBean>)

        fun finishRecommendBookList(beans: List<BookListBean>)

        fun waitToBookShelf()

        fun errorToBookShelf()

        fun succeedToBookShelf()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun refreshBookDetail(bookId: String)

        //添加到书架上
        fun addToBookShelf(collBook: CollBookBean)
    }
}
