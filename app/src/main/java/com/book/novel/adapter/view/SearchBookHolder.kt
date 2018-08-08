package com.book.novel.adapter.view

import android.widget.ImageView
import android.widget.TextView
import com.book.ireader.App
import com.book.ireader.model.bean.BookDetailBean

import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.ui.base.adapter.ViewHolderImpl
import com.book.ireader.utils.Constant
import com.book.novel.GlideApp
import com.lereader.novel.R

/**
 * Created by newbiechen on 17-6-2.
 */

class SearchBookHolder : ViewHolderImpl<BookDetailBean>() {

    private lateinit var mIvCover: ImageView
    private lateinit var mTvName: TextView
    private lateinit var mTvAuthor: TextView
    private lateinit var mTvCategory: TextView
    private lateinit var mLastChapter: TextView

    override fun initView() {
        mIvCover = findById(R.id.search_book_iv_cover)
        mTvName = findById(R.id.search_book_tv_name)
        mTvAuthor = findById(R.id.search_book_tv_author)
        mTvCategory = findById(R.id.search_book_tv_category)
        mLastChapter = findById(R.id.search_book_tv_lastchapter)
    }

    override fun onBind(data: BookDetailBean, pos: Int) {
        //显示图片
        GlideApp.with(App.getContext())
                .load(data.cover)
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_book_loading)
                .into(mIvCover)

        mTvName.text = data.title

        mTvAuthor.text = context.getString(R.string.nb_search_book_author, data.author)
        mTvCategory.text = context.getString(R.string.nb_search_book_category, data.cat)
        mLastChapter.text = context.getString(R.string.nb_search_book_last_chapter, data.lastChapter)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.activity_search_item_book
    }
}
