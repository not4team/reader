package com.book.novel.adapter.view

import android.widget.ImageView
import android.widget.TextView

import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.ui.base.adapter.ViewHolderImpl
import com.book.ireader.utils.Constant
import com.book.novel.GlideApp
import com.book.novel.R

/**
 * Created by newbiechen on 17-6-2.
 */

class SearchBookHolder : ViewHolderImpl<SearchBookPackage.BooksBean>() {

    private lateinit var mIvCover: ImageView
    private lateinit var mTvName: TextView
    private lateinit var mTvBrief: TextView
    private lateinit var mLastChapter: TextView

    override fun initView() {
        mIvCover = findById(R.id.search_book_iv_cover)
        mTvName = findById(R.id.search_book_tv_name)
        mTvBrief = findById(R.id.search_book_tv_brief)
        mLastChapter = findById(R.id.search_book_tv_lastchapter)
    }

    override fun onBind(data: SearchBookPackage.BooksBean, pos: Int) {
        //显示图片
        GlideApp.with(context)
                .load(Constant.IMG_BASE_URL + data.cover)
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .into(mIvCover!!)

        mTvName!!.text = data.title

        mTvBrief!!.text = context.getString(R.string.nb_search_book_brief, data.author, data.cat)
        mLastChapter.text = context.getString(R.string.nb_search_book_last_chapter, data.lastChapter)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.activity_search_item_book
    }
}
