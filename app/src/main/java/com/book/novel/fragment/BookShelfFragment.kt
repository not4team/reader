package com.book.novel.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.ui.base.BaseMVPFragment
import com.book.ireader.widget.RefreshLayout
import com.book.novel.R
import com.book.novel.adapter.BookshelfAdapter
import com.book.novel.presenter.BookShelfPresenter
import com.book.novel.presenter.contract.BookShelfContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:31
 */
class BookShelfFragment : BaseMVPFragment<BookShelfPresenter>(), BookShelfContract.View {
    private lateinit var mRefreshLayout: RefreshLayout
    private lateinit var mRvBookShelf: RecyclerView
    private lateinit var mAdapter: BookshelfAdapter
    override fun getContentId(): Int {
        return R.layout.fragment_bookshelf
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        super.initWidget(savedInstanceState)
        mRefreshLayout = getViewById(R.id.refresh_layout)
        mRvBookShelf = getViewById(R.id.refresh_rv_content)
        mAdapter = BookshelfAdapter()
        mRvBookShelf.layoutManager = LinearLayoutManager(activity)
        mRvBookShelf.adapter = mAdapter
    }

    override fun processLogic() {
        super.processLogic()
        mPresenter.refreshCollBooks("male")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mPresenter.refreshCollBooks("male")
        }
    }

    override fun show(collBookBeans: List<CollBookBean>) {
        mRefreshLayout.showFinish()
        mAdapter.refreshItems(collBookBeans)
    }

    override fun complete() {

    }

    override fun bindPresenter(): BookShelfPresenter {
        return BookShelfPresenter()
    }

    override fun showError() {
        mRefreshLayout.showFinish()
    }
}