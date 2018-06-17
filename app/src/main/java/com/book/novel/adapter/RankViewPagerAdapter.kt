package com.book.novel.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.book.ireader.model.bean.RankTabBean
import com.book.novel.fragment.RankCategoryFragment

/**
 * Created with author.
 * Description:
 * Date: 2018/6/17
 * Time: 1:17
 */
class RankViewPagerAdapter(fm: FragmentManager, private val mRankTabBeans: List<RankTabBean>) : FragmentStatePagerAdapter(fm) {
    lateinit var mCurrFragment: RankCategoryFragment

    override fun getItem(position: Int): Fragment {
        return RankCategoryFragment.newInstance(mRankTabBeans[position].rank, mRankTabBeans[position].gender, mRankTabBeans[position].catId, mRankTabBeans[position].billBookBeans)
    }

    override fun getCount(): Int {
        return mRankTabBeans.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mRankTabBeans[position].tab
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        mCurrFragment = `object` as RankCategoryFragment
    }
}