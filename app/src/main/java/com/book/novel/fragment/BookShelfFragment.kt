package com.book.novel.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.TextView
import com.book.ireader.RxBus
import com.book.ireader.event.BookShelfRefreshEvent
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.ui.activity.ReadActivity
import com.book.ireader.ui.base.BaseMVPFragment
import com.book.ireader.widget.RefreshLayout
import com.book.novel.R
import com.book.novel.adapter.BookshelfAdapter
import com.book.novel.adapter.SimpleItemTouchHelperCallback
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
    private lateinit var mTvEmpty: TextView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mCollBookBeans: List<CollBookBean>
    private var isInit = true
    override fun getContentId(): Int {
        return R.layout.fragment_bookshelf
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        super.initWidget(savedInstanceState)
        mSwipeRefreshLayout = getViewById(R.id.bookshelf_swipe_refresh)
        mRefreshLayout = getViewById(R.id.refresh_layout)
        mRvBookShelf = getViewById(R.id.refresh_rv_content)
        mTvEmpty = getViewById(R.id.bookshelf_tv_empty)
        mAdapter = BookshelfAdapter()
        mRvBookShelf.layoutManager = LinearLayoutManager(activity)
        mRvBookShelf.adapter = mAdapter
        val mItemTouchHelperCallback = SimpleItemTouchHelperCallback(mAdapter)
        val mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        mItemTouchHelper.attachToRecyclerView(mRvBookShelf)
    }

    override fun processLogic() {
        super.processLogic()
        mPresenter.refreshCollBooks("male")
    }

    override fun initClick() {
        val disposable = RxBus.getInstance().toObservable(BookShelfRefreshEvent::class.java).subscribe {
            when (it.type) {
                BookShelfRefreshEvent.EVENT_TYPE_ADD -> {
                    mPresenter.refreshCollBooks("male")
                }
                BookShelfRefreshEvent.EVENT_TYPE_DELETE -> {
                    val id = it._id
                    val position = it.position
                    if (position >= 0) {
                        mAdapter.notifyItemRemoved(position)
                    } else {
                        var collBookBean: CollBookBean? = null
                        for (index in mAdapter.items.indices) {
                            if (id == mAdapter.items[index]._id) {
                                collBookBean = mAdapter.items[index]
                                break;
                            }
                        }
                        if (collBookBean != null) {
                            mAdapter.removeItem(collBookBean)
                        }
                    }
                    if (mAdapter.itemCount == 0) {
                        mTvEmpty.visibility = View.VISIBLE
                    }
                }
                BookShelfRefreshEvent.EVENT_TYPE_UPDATE -> {
                    mPresenter.refreshCollBooks("male")
                }
            }
        }
        addDisposable(disposable)

        mSwipeRefreshLayout.setOnRefreshListener {
            mPresenter.updateCollBooks(mCollBookBeans)
        }

        mAdapter.setOnItemClickListener(
                { view, pos ->
                    val collBook = mAdapter.getItem(pos)
                    ReadActivity.startActivity(context,
                            collBook, true)
                }
        )
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mPresenter.refreshCollBooks("male")
        }
    }

    override fun show(collBookBeans: List<CollBookBean>) {
        this.mCollBookBeans = collBookBeans
        mRefreshLayout.showFinish()
        mAdapter.refreshItems(collBookBeans)
        if (collBookBeans.isNotEmpty()) {
            mTvEmpty.visibility = View.GONE
        } else {
            mTvEmpty.visibility = View.VISIBLE
        }
        if (isInit) {
            isInit = false
            mPresenter.updateCollBooks(collBookBeans)
        }
    }

    override fun finishUpdate() {
        mSwipeRefreshLayout.isRefreshing = false
        mPresenter.refreshCollBooks("male")
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