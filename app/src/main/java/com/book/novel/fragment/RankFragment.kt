package com.book.novel.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import com.book.ireader.model.bean.RankTabBean
import com.book.ireader.ui.base.BaseMVPFragment
import com.lereader.novel.R
import com.book.novel.adapter.RankViewPagerAdapter
import com.book.novel.presenter.RankPresenter
import com.book.novel.presenter.contract.RankContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:42
 */
class RankFragment : BaseMVPFragment<RankPresenter>(), RankContract.View {
    private lateinit var mRankSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRankViewPager: ViewPager
    private lateinit var mRankTabLayout: TabLayout
    private lateinit var mPagerAdapter: RankViewPagerAdapter
    private lateinit var mRankTabBeans: List<RankTabBean>
    override fun getContentId(): Int {
        return R.layout.fragment_rank
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        super.initWidget(savedInstanceState)
        mRankSwipeRefreshLayout = getViewById(R.id.rank_swipe_refresh_layout)
        mRankViewPager = getViewById(R.id.rank_viewpager)
        mRankTabLayout = getViewById(R.id.rank_tablayout)
        mRankTabLayout.setupWithViewPager(mRankViewPager)
    }

    override fun processLogic() {
        super.processLogic()
        mRankSwipeRefreshLayout.isRefreshing = true
        mPresenter.load("hotsales", "male")
    }

    override fun initClick() {
        super.initClick()
        mRankSwipeRefreshLayout.setOnRefreshListener {
            val mRankCategoryFragment = mPagerAdapter.mCurrFragment
            mRankCategoryFragment.mRefreshListener = object : RankCategoryFragment.RefreshListener {
                override fun onFinish() {
                    mRankSwipeRefreshLayout.isRefreshing = false
                }

            }
            mRankCategoryFragment.refreshBooksFromNet()
        }
    }

    override fun show(rankTabBeans: List<RankTabBean>) {
        mRankTabBeans = rankTabBeans
        mRankSwipeRefreshLayout.isRefreshing = false
        mPagerAdapter = RankViewPagerAdapter(fragmentManager!!, rankTabBeans)
        mRankViewPager.adapter = mPagerAdapter
    }

    override fun complete() {

    }

    override fun bindPresenter(): RankPresenter {
        return RankPresenter()
    }

    override fun showError() {

    }
}