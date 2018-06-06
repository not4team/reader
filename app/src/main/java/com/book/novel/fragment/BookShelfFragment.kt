package com.book.novel.fragment

import android.widget.TextView
import com.book.ireader.ui.base.BaseMVPFragment
import com.book.novel.R
import com.book.novel.presenter.BookShelfPresenter

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:31
 */
class BookShelfFragment : BaseMVPFragment<BookShelfPresenter>() {
    override fun getContentId(): Int {
        return R.layout.fragment_bookshelf
    }

    override fun complete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindPresenter(): BookShelfPresenter {
        return BookShelfPresenter()
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}