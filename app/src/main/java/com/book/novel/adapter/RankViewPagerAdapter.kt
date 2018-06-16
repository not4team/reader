package com.book.novel.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.book.novel.fragment.RankCategoryFragment

/**
 * Created with author.
 * Description:
 * Date: 2018/6/17
 * Time: 1:17
 */
class RankViewPagerAdapter(fm: FragmentManager, val fragments: List<RankCategoryFragment>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}