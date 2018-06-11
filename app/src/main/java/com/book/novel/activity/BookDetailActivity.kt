package com.book.novel.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.packages.InterestedBookListPackage
import com.book.ireader.ui.base.BaseMVPActivity
import com.book.novel.GlideApp
import com.book.novel.R
import com.book.novel.adapter.BookDetailRecyclerAdapter
import com.book.novel.presenter.BookDetailPresenter
import com.book.novel.presenter.contract.BookDetailContract

/**
 * Created by author on 2018/6/9.
 */
class BookDetailActivity : BaseMVPActivity<BookDetailContract.Presenter>(), BookDetailContract.View {
    var mId: String? = null
    lateinit var mTitle: String
    lateinit var mAuthor: String
    lateinit var mIvCover: ImageView
    lateinit var mTvTitle: TextView
    lateinit var mTvAuthor: TextView
    lateinit var mTvCategory: TextView
    lateinit var mTvWordCount: TextView
    lateinit var mTvUpdated: TextView
    lateinit var mBtnAddBookshelf: Button
    lateinit var mBtnStartRead: Button
    lateinit var mTvLastCapture: TextView
    lateinit var mRvInterestedBooks: RecyclerView
    lateinit var mAdapter: BookDetailRecyclerAdapter

    companion object {
        val BOOK_TILTE_INTENT_KEY = "BOOK_TILTE_INTENT_KEY"
        val BOOK_AUTHOR_INTENT_KEY = "BOOK_AUTHOR_INTENT_KEY"
        val BOOK_ID_INTENT_KEY = "BOOK_ID_INTENT_KEY"
    }

    override fun initWidget() {
        super.initWidget()
        mIvCover = findViewById(R.id.bookdetail_iv_cover)
        mTvTitle = findViewById(R.id.bookdetail_tv_title)
        mTvAuthor = findViewById(R.id.bookdetail_tv_author)
        mTvCategory = findViewById(R.id.bookdetail_tv_category)
        mTvWordCount = findViewById(R.id.bookdetail_tv_wordcount)
        mTvUpdated = findViewById(R.id.bookdetail_tv_lasttime)
        mBtnAddBookshelf = findViewById(R.id.bookdetail_bt_add_bookshelf)
        mBtnStartRead = findViewById(R.id.bookdetail_bt_start_read)
        mTvLastCapture = findViewById(R.id.bookdetail_tv_lastcapture)
        mRvInterestedBooks = findViewById(R.id.bookdetail_rv_interested_books)
        mRvInterestedBooks.layoutManager = LinearLayoutManager(this)
        mAdapter = BookDetailRecyclerAdapter(this, R.layout.activity_bookdetail_recommend_books_item)
        mRvInterestedBooks.adapter = mAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mId = intent.getStringExtra(BOOK_ID_INTENT_KEY)
        mTitle = intent.getStringExtra(BOOK_TILTE_INTENT_KEY)
        mAuthor = intent.getStringExtra(BOOK_AUTHOR_INTENT_KEY)
    }

    override fun initClick() {
        super.initClick()
        mBtnStartRead.setOnClickListener {

        }
    }

    override fun processLogic() {
        super.processLogic()
        mPresenter.refreshBookDetail(mTitle, mAuthor)
    }

    /**
     * 你可能感兴趣的书籍
     */
    override fun finishRecommendBooks(beans: List<InterestedBookListPackage.BookRecommendBean>) {
        mAdapter.refreshItems(beans)
    }

    override fun finishRefresh(bean: BookDetailBean) {
        GlideApp.with(this).load(bean.cover).placeholder(R.mipmap.ic_default_portrait).error(R.mipmap.ic_default_portrait).into(mIvCover)
        mTvTitle.text = bean.title
        mTvAuthor.text = bean.author
        mTvCategory.text = bean.cat
        mTvWordCount.text = bean.wordCount.toString()
        mTvUpdated.text = bean.updated
        mTvLastCapture.text = bean.lastChapter
    }

    override fun bindPresenter(): BookDetailContract.Presenter {
        return BookDetailPresenter()
    }

    override fun showError() {

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
        return R.layout.activity_book_detail
    }

}