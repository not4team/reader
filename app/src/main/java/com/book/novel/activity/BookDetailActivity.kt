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
import com.book.ireader.App
import com.book.ireader.RxBus
import com.book.ireader.event.BookShelfRefreshEvent
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.bean.packages.InterestedBookListPackage
import com.book.ireader.model.local.BookDao
import com.book.ireader.model.local.CollectDao
import com.book.ireader.ui.activity.ReadActivity
import com.book.ireader.ui.base.BaseMVPActivity
import com.book.novel.GlideApp
import com.book.novel.adapter.BookDetailRecyclerAdapter
import com.book.novel.adapter.recyclerview.MultiItemTypeAdapter
import com.book.novel.presenter.BookDetailPresenter
import com.book.novel.presenter.contract.BookDetailContract
import com.book.novel.utils.AndroidUtils
import com.lereader.novel.R

/**
 * Created by author on 2018/6/9.
 */
class BookDetailActivity : BaseMVPActivity<BookDetailContract.Presenter>(), BookDetailContract.View, View.OnClickListener {
    private var mBookId: String? = null
    private var mBookLink: String? = null
    private var mTitle: String? = null
    private var mAuthor: String? = null
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
        const val BOOK_LINK_INTENT_KEY = "BOOK_LINK_INTENT_KEY"
        const val REQUEST_READ = 0x1
        const val RESULT_IS_COLLECTED = "RESULT_IS_COLLECTED"
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
        mRvChapters.layoutManager = LinearLayoutManager(this)
        mAdapter = BookDetailRecyclerAdapter(this, R.layout.activity_bookdetail_chapters_item)
        mRvChapters.adapter = mAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mBookId = intent.getStringExtra(BOOK_ID_INTENT_KEY)
        mBookLink = intent.getStringExtra(BOOK_LINK_INTENT_KEY)
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
        supportActionBar!!.title = resources.getString(R.string.bookdetail_title)
    }

    override fun initClick() {
        super.initClick()
        mSwipeRefreshLayout.setOnRefreshListener {
            mSwipeRefreshLayout.isRefreshing = true
            if (mBookId != null) {
                mPresenter.refreshBookDetail(mBookId!!)
            } else {
                mPresenter.refreshBookDetail(mTitle!!, mAuthor!!)
            }
        }
        mBtnAddBookshelf.setOnClickListener(this)
        mBtnStartRead.setOnClickListener(this)
        mTvReverseOrder.setOnClickListener(this)
        mAdapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return true;
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                var chapterPosition = position
                if (mTvReverseOrder.text.equals(getString(R.string.positive_order))) {
                    chapterPosition = mAdapter.itemCount - position - 1
                }
                startActivityForResult(Intent(this@BookDetailActivity, ReadActivity::class.java)
                        .putExtra(ReadActivity.EXTRA_IS_COLLECTED, isCollected)
                        .putExtra(ReadActivity.EXTRA_COLL_BOOK, mCollBookBean)
                        .putExtra(ReadActivity.EXTRA_SKIP_POSITION, chapterPosition), REQUEST_READ)
            }

        })
    }

    override fun processLogic() {
        super.processLogic()
        mSwipeRefreshLayout.isRefreshing = true
        mContent.visibility = View.INVISIBLE
        if (mBookId != null) {
            mPresenter.refreshBookDetail(mBookLink!!)
        } else {
            mPresenter.refreshBookDetail(mTitle!!, mAuthor!!)
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
            R.id.bookdetail_bt_add_bookshelf -> {
                //点击存储
                if (isCollected) {
                    //放弃点击
                    CollectDao.getInstance(App.getContext())
                            .deleteCollBookInRx(mCollBookBean).subscribe()
                    mBtnAddBookshelf.text = resources.getString(R.string.nb_book_detail_chase_update)
                    isCollected = false
                    RxBus.getInstance().post(BookShelfRefreshEvent().setId(mCollBookBean!!._id).setType(BookShelfRefreshEvent.EVENT_TYPE_DELETE))
                } else {
                    mPresenter.addToBookShelf(mCollBookBean!!)
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
        GlideApp
                .with(this)
                .load(AndroidUtils.generateGlideUrl(bean.cover))
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_book_loading)
                .into(mIvCover)
        mTvTitle.text = bean.title
        mTvAuthor.text = resources.getString(R.string.bookdetail_author, bean.author)
        mTvCategory.text = bean.cat
        mTvWordCount.text = resources.getString(R.string.bookdetail_wordcount, bean.wordCount)
        mTvUpdated.text = resources.getString(R.string.bookdetail_updated, bean.updated)
        mTvLastCapture.text = resources.getString(R.string.bookdetail_last_chapter, bean.lastChapter)
        mTvLongInstro.text = bean.longIntro

        //判断是否收藏
        mCollBookBean = CollectDao.getInstance(App.getContext()).getCollBook(bean._id)
        if (mCollBookBean != null) {
            isCollected = true
            mBtnAddBookshelf.text = resources.getString(R.string.nb_book_detail_give_up)
            mBtnStartRead.text = resources.getString(R.string.continue_read)
            if (bean.bookChapterBeans.size > mCollBookBean!!.bookChapterList.size) {
                mCollBookBean!!.setIsUpdate(true)
                mCollBookBean!!.lastChapter = bean.lastChapter
                mCollBookBean!!.bookChapterList = bean.bookChapterBeans
                CollectDao.getInstance(App.getContext()).insertOrReplaceCollBook(mCollBookBean)
                BookDao.getInstance(App.getContext()).saveBookChaptersWithAsync(mCollBookBean!!.bookChapterList)
                RxBus.getInstance().post(BookShelfRefreshEvent().setId(mCollBookBean!!._id).setType(BookShelfRefreshEvent.EVENT_TYPE_UPDATE))
            }
        } else {
            mCollBookBean = bean.collBookBean
            mCollBookBean!!.bookChapterList = bean.bookChapterBeans
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
        mBtnAddBookshelf.text = resources.getString(R.string.nb_book_detail_give_up)
        isCollected = true
        RxBus.getInstance().post(BookShelfRefreshEvent().setId(mCollBookBean!!._id).setType(BookShelfRefreshEvent.EVENT_TYPE_ADD))
    }

    override fun getContentId(): Int {
        return R.layout.activity_book_detail
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //如果进入阅读页面收藏了，页面结束的时候，就需要返回改变收藏按钮
        if (requestCode == REQUEST_READ) {
            if (data == null) {
                return
            }
            isCollected = data.getBooleanExtra(RESULT_IS_COLLECTED, false)
            if (isCollected) {
                mBtnAddBookshelf.text = resources.getString(R.string.nb_book_detail_give_up)
                mBtnStartRead.text = resources.getString(R.string.continue_read)
            }
        }
    }
}