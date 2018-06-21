package com.book.novel.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.model.bean.packages.BookCityPackage
import com.book.ireader.ui.base.BaseMVPFragment
import com.book.novel.GlideApp
import com.book.novel.activity.BookDetailActivity
import com.book.novel.adapter.BookCityRecyclerAdapter
import com.book.novel.adapter.recyclerview.MultiItemTypeAdapter
import com.book.novel.adapter.recyclerview.wrapper.HeaderAndFooterWrapper
import com.book.novel.presenter.BookCityPresenter
import com.book.novel.presenter.contract.BookCityContract
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.lereader.novel.R

/**
 * Created with author.
 * Description:
 * Date: 2018-06-05
 * Time: 下午4:58
 */
class BookCityFragment : BaseMVPFragment<BookCityPresenter>(), BookCityContract.View {
    val TAG = "BookCityFragment"
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: BookCityRecyclerAdapter
    private lateinit var mHeaderAndFooterWrapper: HeaderAndFooterWrapper
    private lateinit var mEmptyView: View
    private lateinit var mAdView: AdView
    private lateinit var mBannerImageView: ImageView
    private val mAdListener = object : AdListener() {
        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.e(TAG, "onAdLoaded")
            mBannerImageView.visibility = View.GONE
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            // Code to be executed when an ad request fails.
            Log.e(TAG, "onAdFailedToLoad")
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.e(TAG, "onAdOpened")
        }

        override fun onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            Log.e(TAG, "onAdLeftApplication")
        }

        override fun onAdClosed() {
            // Code to be executed when when the user is about to return
            // to the app after tapping on an ad.
            Log.e(TAG, "onAdClosed")
        }
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        super.initWidget(savedInstanceState)
        mSwipeRefreshLayout = getViewById(R.id.bookcity_swipe_refresh)
        mRecyclerView = getViewById(R.id.bookcity_recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerAdapter = BookCityRecyclerAdapter(activity!!, R.layout.fragment_bookcity_recyclerview_item)
        mHeaderAndFooterWrapper = HeaderAndFooterWrapper(mRecyclerAdapter)
        mRecyclerView.adapter = mHeaderAndFooterWrapper
        mEmptyView = getViewById(R.id.rl_empty_view)
    }

    override fun initClick() {
        super.initClick()
        mSwipeRefreshLayout.setOnRefreshListener {
            mPresenter.load("male")
        }
        mRecyclerAdapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                //有1个header
                val item = mRecyclerAdapter.datas[position - 1]
                toDetailActivity(item)
            }

        })
    }

    override fun processLogic() {
        super.processLogic()
        mSwipeRefreshLayout.isRefreshing = true
        mPresenter.load("male")
    }

    private fun toDetailActivity(bean: BillBookBean) {
        val mIntent = Intent(activity, BookDetailActivity::class.java)
        mIntent.putExtra(BookDetailActivity.BOOK_ID_INTENT_KEY, bean._id)
        mIntent.putExtra(BookDetailActivity.BOOK_TILTE_INTENT_KEY, bean.title)
        mIntent.putExtra(BookDetailActivity.BOOK_AUTHOR_INTENT_KEY, bean.author)
        startActivity(mIntent)
    }

    override fun show(bookCityPackage: BookCityPackage) {
        mSwipeRefreshLayout.isRefreshing = false
        mEmptyView.visibility = View.GONE
        val headerView = layoutInflater.inflate(R.layout.fragment_bookcity_recyclerview_header, null)
        mBannerImageView = headerView.findViewById(R.id.banner_image_view)
        mAdView = headerView.findViewById(R.id.adView)
        mAdView.adListener = mAdListener
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        val linearLayout = headerView.findViewById<LinearLayout>(R.id.bookcity_rv_header_parent)
        bookCityPackage.hotBooks.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.fragment_bookcity_recyclerview_header_item, null)
            val title = itemView.findViewById<TextView>(R.id.bookcity_rv_header_item_title)
            title.text = item.title
            val author = itemView.findViewById<TextView>(R.id.bookcity_rv_header_item_author)
            author.text = item.author
            val cover = itemView.findViewById<ImageView>(R.id.bookcity_rv_header_item_cover)
            GlideApp.with(activity!!).load(item.cover).placeholder(R.drawable.ic_book_loading).error(R.drawable.ic_book_loading).into(cover)
            itemView.setOnClickListener {
                toDetailActivity(item)
            }
            linearLayout.addView(itemView)
        }
        mHeaderAndFooterWrapper.removeLastHeaderView()
        mHeaderAndFooterWrapper.addHeaderView(headerView)
        mRecyclerAdapter.newBooks = bookCityPackage.newBooks
        mRecyclerAdapter.finishedBooks = bookCityPackage.finishedBooks
        val list = mutableListOf<BillBookBean>()
        list.addAll(bookCityPackage.newBooks)
        list.addAll(bookCityPackage.finishedBooks)
        mRecyclerAdapter.refreshItems(list)
        mHeaderAndFooterWrapper.notifyDataSetChanged()
    }

    override fun getContentId(): Int {
        return R.layout.fragment_bookcity
    }

    override fun bindPresenter(): BookCityPresenter {
        return BookCityPresenter()
    }

    override fun showError() {
        mSwipeRefreshLayout.isRefreshing = false
        mEmptyView.visibility = View.VISIBLE
    }

    override fun complete() {

    }
}