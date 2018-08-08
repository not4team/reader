package com.book.novel.presenter.contract

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.ui.base.BaseContract

/**
 * Created by newbiechen on 17-6-2.
 */

interface SearchContract : BaseContract {

    interface View : BaseContract.BaseView {
        fun finishHistory(history: List<String>)

        fun finishKeyWords(keyWords: List<String>)

        fun finishBooks(books: List<BookDetailBean>)

        fun errorBooks()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun searchHistory()

        //搜索书籍
        fun searchBook(query: String)
    }
}
