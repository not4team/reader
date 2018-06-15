package com.book.novel.presenter

import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.novel.presenter.contract.BookShelfContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:32
 */
class BookShelfPresenter() : RxPresenter<BookShelfContract.View>(), BookShelfContract.Presenter {
    override fun deleteCollBook(collBookBean: CollBookBean) {
        BookRepository.getInstance().deleteCollBookInRx(collBookBean)
    }

    override fun refreshCollBooks(gender: String) {
        val collBooks = BookRepository
                .getInstance().getCollBooks()
        mView.show(collBooks)
    }
}