package com.book.novel.presenter.contract

import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.ui.base.BaseContract
import com.book.ireader.widget.page.TxtChapter

/**
 * Created by newbiechen on 17-5-16.
 */

interface BookCityContract : BaseContract {
    interface View : BaseContract.BaseView {
        fun showCategory(bookChapterList: List<BookChapterBean>)

        fun finishChapter()

        fun errorChapter()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        @Throws(Exception::class)
        fun loadCategory(bookId: String)

        @Throws(Exception::class)
        fun loadChapter(bookId: String, bookChapterList: List<TxtChapter>)
    }
}
