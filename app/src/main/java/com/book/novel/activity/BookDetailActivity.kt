package com.book.novel.activity

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.BookListBean
import com.book.ireader.model.bean.HotCommentBean
import com.book.ireader.ui.base.BaseMVPActivity
import com.book.novel.presenter.contract.BookDetailContract

/**
 * Created by shixq on 2018/6/9.
 */
class BookDetailActivity : BaseMVPActivity<BookDetailContract.Presenter>(), BookDetailContract.View {
    override fun finishRefresh(bean: BookDetailBean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun finishHotComment(beans: List<HotCommentBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun finishRecommendBookList(beans: List<BookListBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindPresenter(): BookDetailContract.Presenter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun complete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun waitToBookShelf() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun errorToBookShelf() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun succeedToBookShelf() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContentId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}