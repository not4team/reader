package com.book.novel.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.model.bean.packages.BookCityPackage
import com.book.ireader.ui.base.BaseMVPFragment
import com.book.novel.GlideApp
import com.book.novel.R
import com.book.novel.activity.BookDetailActivity
import com.book.novel.adapter.BookCityRecyclerAdapter
import com.book.novel.adapter.recyclerview.MultiItemTypeAdapter
import com.book.novel.adapter.recyclerview.wrapper.HeaderAndFooterWrapper
import com.book.novel.presenter.BookCityPresenter
import com.book.novel.presenter.contract.BookCityContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-05
 * Time: 下午4:58
 */
class BookCityFragment : BaseMVPFragment<BookCityPresenter>(), BookCityContract.View {
    val TAG = "BookCityFragment"
    lateinit var mScrollRefreshLayout: SwipeRefreshLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mRecyclerAdapter: BookCityRecyclerAdapter
    lateinit var mHeaderAndFooterWrapper: HeaderAndFooterWrapper
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        super.initWidget(savedInstanceState)
        mScrollRefreshLayout = getViewById(R.id.bookcity_swipe_refresh)
        mRecyclerView = getViewById(R.id.bookcity_recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerAdapter = BookCityRecyclerAdapter(activity!!, R.layout.fragment_bookcity_recyclerview_item)
        mHeaderAndFooterWrapper = HeaderAndFooterWrapper(mRecyclerAdapter)
        mRecyclerView.adapter = mHeaderAndFooterWrapper
    }

    override fun initClick() {
        super.initClick()
        mScrollRefreshLayout.setOnRefreshListener {
            mPresenter.load("male")
        }
        mRecyclerAdapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                //有1个header
                val item = mRecyclerAdapter.datas[position - 1]
                val mIntent = Intent(activity, BookDetailActivity::class.java)
                mIntent.putExtra(BookDetailActivity.BOOK_ID_INTENT_KEY, item._id)
                mIntent.putExtra(BookDetailActivity.BOOK_TILTE_INTENT_KEY, item.title)
                mIntent.putExtra(BookDetailActivity.BOOK_AUTHOR_INTENT_KEY, item.author.replace("作者：", ""))
                startActivity(mIntent)
            }

        })
    }

    override fun processLogic() {
        super.processLogic()
        mScrollRefreshLayout.setRefreshing(true)
        mPresenter.load("male")
    }

    override fun show(bookCityPackage: BookCityPackage) {
        mScrollRefreshLayout.setRefreshing(false)
        val headerView = layoutInflater.inflate(R.layout.fragment_bookcity_recyclerview_header, null)
        val linearLayout = headerView.findViewById<LinearLayout>(R.id.bookcity_rv_header_parent)
        bookCityPackage.hotBooks.forEach { item ->
            val itemView = layoutInflater.inflate(R.layout.fragment_bookcity_recyclerview_header_item, null)
            val title = itemView.findViewById<TextView>(R.id.bookcity_rv_header_item_title)
            title.text = item.title
            val author = itemView.findViewById<TextView>(R.id.bookcity_rv_header_item_author)
            author.text = item.author
            val cover = itemView.findViewById<ImageView>(R.id.bookcity_rv_header_item_cover)
            GlideApp.with(activity!!).load(item.cover).placeholder(R.mipmap.ic_default_portrait).error(R.mipmap.ic_default_portrait).into(cover)
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
        mScrollRefreshLayout.setRefreshing(false)
    }

    override fun complete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}