package com.book.novel.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.ui.base.BaseMVPFragment
import com.book.ireader.widget.RefreshLayout
import com.book.novel.R
import com.book.novel.adapter.RankCategoryRecyclerAdapter
import com.book.novel.presenter.RankCategoryPresenter
import com.book.novel.presenter.contract.RankCategoryContract
import java.io.Serializable

/**
 * Created with author.
 * Description:
 * Date: 2018/6/16
 * Time: 22:32
 */
class RankCategoryFragment : BaseMVPFragment<RankCategoryPresenter>(), RankCategoryContract.View {
    private lateinit var mRefreshLayout: RefreshLayout
    private lateinit var mRvBooks: RecyclerView
    private lateinit var mAdapter: RankCategoryRecyclerAdapter
    private var mBillBookBeans: MutableList<BillBookBean>? = null
    private lateinit var mRankName: String
    private lateinit var mGender: String
    private lateinit var mCatId: String

    companion object {
        val BUNDLE_RANK_DEFAULT_BOOKS = "rank_default_books"
        val BUNDLE_RANK_NAME = "rank_name"
        val BUNDLE_RANK_GENDER = "rank_gender"
        val BUNDLE_RANK_CATID = "rank_catId"

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        fun newInstance(rankName: String, gender: String, catId: String, books: MutableList<BillBookBean>?): RankCategoryFragment {
            val f = RankCategoryFragment()
            // Supply num input as an argument.
            val args = Bundle()
            if (books != null) {
                args.putSerializable(BUNDLE_RANK_DEFAULT_BOOKS, books as Serializable)
            }
            args.putString(BUNDLE_RANK_NAME, rankName)
            args.putString(BUNDLE_RANK_GENDER, gender)
            args.putString(BUNDLE_RANK_CATID, catId)
            f.setArguments(args)
            return f
        }
    }

    override fun bindPresenter(): RankCategoryPresenter {
        return RankCategoryPresenter()
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mRankName = arguments!!.getString(BUNDLE_RANK_NAME)
        mGender = arguments!!.getString(BUNDLE_RANK_GENDER)
        mCatId = arguments!!.getString(BUNDLE_RANK_CATID)
        val books = arguments!!.getSerializable(BUNDLE_RANK_DEFAULT_BOOKS)
        if (books != null) {
            mBillBookBeans = books as MutableList<BillBookBean>
        }
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        super.initWidget(savedInstanceState)
        mRefreshLayout = getViewById(R.id.refresh_layout)
        mRvBooks = getViewById(R.id.refresh_rv_content)
        mRvBooks.layoutManager = LinearLayoutManager(activity)
        mAdapter = RankCategoryRecyclerAdapter()
        mRvBooks.adapter = mAdapter
    }

    override fun processLogic() {
        super.processLogic()
        if (mBillBookBeans == null) {
            mPresenter.load(mRankName, mGender, mCatId)
        } else {
            mRefreshLayout.showFinish()
            mAdapter.refreshItems(mBillBookBeans)
        }
    }

    override fun show(books: List<BillBookBean>) {
        mRefreshLayout.showFinish()
        mAdapter.refreshItems(books)
    }

    override fun complete() {

    }

    override fun showError() {
        mRefreshLayout.showError()
    }

    override fun getContentId(): Int {
        return R.layout.fragment_rank_category
    }

}