package com.book.novel.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.bean.packages.InterestedBookListPackage
import com.book.ireader.model.local.BookRepository
import com.book.ireader.ui.activity.ReadActivity
import com.book.ireader.ui.base.BaseMVPActivity
import com.book.ireader.utils.Constant
import com.book.novel.GlideApp
import com.book.novel.R
import com.book.novel.adapter.BookDetailRecyclerAdapter
import com.book.novel.presenter.BookDetailPresenter
import com.book.novel.presenter.contract.BookDetailContract

/**
 * Created by author on 2018/6/9.
 */
class BookDetailActivity : BaseMVPActivity<BookDetailContract.Presenter>(), BookDetailContract.View, View.OnClickListener {
    private var mBookId: String? = null
    private lateinit var mTitle: String
    private lateinit var mAuthor: String
    private var mCollBookBean: CollBookBean? = null
    private var isCollected: Boolean = false
    private lateinit var mIvCover: ImageView
    private lateinit var mTvTitle: TextView
    private lateinit var mTvAuthor: TextView
    private lateinit var mTvCategory: TextView
    private lateinit var mTvWordCount: TextView
    private lateinit var mTvUpdated: TextView
    private lateinit var mTvLongInstro: TextView
    private lateinit var mBtnAddBookshelf: Button
    private lateinit var mBtnStartRead: Button
    private lateinit var mTvLastCapture: TextView
    private lateinit var mTvReverseOrder: TextView
    private lateinit var mRvChapters: RecyclerView
    private lateinit var mAdapter: BookDetailRecyclerAdapter
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mEmptyView: View
    private lateinit var mContent: View

    companion object {
        const val BOOK_TILTE_INTENT_KEY = "BOOK_TILTE_INTENT_KEY"
        const val BOOK_AUTHOR_INTENT_KEY = "BOOK_AUTHOR_INTENT_KEY"
        const val BOOK_ID_INTENT_KEY = "BOOK_ID_INTENT_KEY"
        const val REQUEST_READ = 0x1
    }

    override fun initWidget() {
        super.initWidget()
        mSwipeRefreshLayout = findViewById(R.id.bookdetail_swipe_refresh)
        mEmptyView = findViewById(R.id.rl_empty_view)
        mContent = findViewById(R.id.bookdetail_cl_content)
        mIvCover = findViewById(R.id.bookdetail_iv_cover)
        mTvTitle = findViewById(R.id.bookdetail_tv_title)
        mTvAuthor = findViewById(R.id.bookdetail_tv_author)
        mTvCategory = findViewById(R.id.bookdetail_tv_category)
        mTvWordCount = findViewById(R.id.bookdetail_tv_wordcount)
        mTvUpdated = findViewById(R.id.bookdetail_tv_lasttime)
        mTvLongInstro = findViewById(R.id.bookdetail_tv_longintro)
        mBtnAddBookshelf = findViewById(R.id.bookdetail_bt_add_bookshelf)
        mBtnStartRead = findViewById(R.id.bookdetail_bt_start_read)
        mTvLastCapture = findViewById(R.id.bookdetail_tv_lastcapture)
        mTvReverseOrder = findViewById(R.id.bookdetail_tv_reverse_order)
        mRvChapters = findViewById(R.id.bookdetail_rv_interested_books)
        mRvChapters.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                //禁止滚动，防止与scrollview冲突
                return true
            }
        }
        mAdapter = BookDetailRecyclerAdapter(this, R.layout.activity_bookdetail_chapters_item)
        mRvChapters.adapter = mAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mBookId = intent.getStringExtra(BOOK_ID_INTENT_KEY)
        mTitle = intent.getStringExtra(BOOK_TILTE_INTENT_KEY)
        mAuthor = intent.getStringExtra(BOOK_AUTHOR_INTENT_KEY)
    }

    override fun initToolbar() {
        mToolbar = findViewById(R.id.bookdetail_toolbar)
        if (mToolbar != null) {
            supportActionBar(mToolbar)
            setUpToolbar(mToolbar)
        }
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        super.setUpToolbar(toolbar)
        supportActionBar!!.title = "书籍详情"
    }

    override fun initClick() {
        super.initClick()
        mSwipeRefreshLayout.setOnRefreshListener {
            mSwipeRefreshLayout.isRefreshing = true
            if (mBookId != null) {
                mPresenter.refreshBookDetail(mBookId!!)
            } else {
                mPresenter.refreshBookDetail(mTitle, mAuthor)
            }
        }
        mBtnStartRead.setOnClickListener(this)
        mTvReverseOrder.setOnClickListener(this)
    }

    override fun processLogic() {
        super.processLogic()
        mSwipeRefreshLayout.isRefreshing = true
        mContent.visibility = View.INVISIBLE
        if (mBookId != null) {
            mPresenter.refreshBookDetail(mBookId!!)
        } else {
            mPresenter.refreshBookDetail(mTitle, mAuthor)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bookdetail_bt_start_read -> {
                startActivityForResult(Intent(this, ReadActivity::class.java)
                        .putExtra(ReadActivity.EXTRA_IS_COLLECTED, isCollected)
                        .putExtra(ReadActivity.EXTRA_COLL_BOOK, mCollBookBean), REQUEST_READ)
            }
            R.id.bookdetail_tv_reverse_order -> {
                mAdapter.datas.reverse()
                mAdapter.notifyDataSetChanged()
                if (mTvReverseOrder.text.equals(getString(R.string.reverse_order))) {
                    mTvReverseOrder.text = getString(R.string.positive_order)
                } else {
                    mTvReverseOrder.text = getString(R.string.reverse_order)
                }
            }
        }
    }

    /**
     * 你可能感兴趣的书籍
     */
    override fun finishRecommendBooks(beans: List<InterestedBookListPackage.BookRecommendBean>) {

    }

    override fun finishRefresh(bean: BookDetailBean) {
        mSwipeRefreshLayout.isRefreshing = false
        mEmptyView.visibility = View.GONE
        mContent.visibility = View.VISIBLE
        GlideApp.with(this).load(Constant.IMG_BASE_URL + bean.cover).placeholder(R.drawable.ic_book_loading).error(R.drawable.ic_book_loading).into(mIvCover)
        mTvTitle.text = bean.title
        mTvAuthor.text = bean.author
        mTvCategory.text = bean.cat
        mTvWordCount.text = bean.wordCount.toString()
        mTvUpdated.text = bean.updated
        mTvLastCapture.text = bean.lastChapter
        mTvLongInstro.text = bean.longIntro

//        BookRepository.getInstance().saveBookChaptersWithAsync(bean.bookChapterBeans)

        //判断是否收藏
        mCollBookBean = BookRepository.getInstance().getCollBook(bean._id)
        if (mCollBookBean != null) {
            isCollected = true
            mBtnAddBookshelf.text = resources.getString(R.string.nb_book_detail_give_up)
            //修改背景
//            val drawable = resources.getDrawable(R.drawable.shape_common_gray_corner)
//            mTvChase.setBackground(drawable)
            //设置图片
//            mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null, null, null)
            mBtnStartRead.text = "继续阅读"
        } else {
            mCollBookBean = bean.collBookBean
        }
        mAdapter.refreshItems(bean.bookChapterBeans)
    }

    override fun bindPresenter(): BookDetailContract.Presenter {
        return BookDetailPresenter()
    }

    override fun showError() {
        mSwipeRefreshLayout.isRefreshing = false
        mEmptyView.visibility = View.VISIBLE
    }

    override fun complete() {

    }

    override fun waitToBookShelf() {

    }

    override fun errorToBookShelf() {

    }

    override fun succeedToBookShelf() {

    }

    override fun getContentId(): Int {
        return R.layout.activity_book_detail
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}