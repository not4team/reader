package com.book.novel.adapter.view

import android.widget.TextView

import com.book.ireader.ui.base.adapter.ViewHolderImpl
import com.book.novel.R

/**
 * Created by newbiechen on 17-6-2.
 */

class KeyWordHolder : ViewHolderImpl<String>() {

    private var mTvName: TextView? = null

    override fun initView() {
        mTvName = findById(R.id.keyword_tv_name)
    }

    override fun onBind(data: String, pos: Int) {
        mTvName!!.text = data
    }

    override fun getItemLayoutId(): Int {
        return R.layout.activity_search_item_keyword
    }
}
