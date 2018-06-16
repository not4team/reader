package com.book.novel.adapter.view

import android.widget.ImageView
import android.widget.TextView
import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.ui.base.adapter.ViewHolderImpl
import com.book.novel.GlideApp
import com.book.novel.R

/**
 * Created with author.
 * Description:
 * Date: 2018/6/16
 * Time: 23:17
 */
class RankCategoryHolder : ViewHolderImpl<BillBookBean>() {
    private lateinit var mIvCover: ImageView
    private lateinit var mTvName: TextView
    private lateinit var mTvAuthor: TextView
    private lateinit var mTvCategory: TextView
    private lateinit var mTvShortInstro: TextView
    override fun initView() {
        mIvCover = findById(R.id.rank_category_rv_item_cover)
        mTvName = findById(R.id.rank_category_rv_item_title)
        mTvAuthor = findById(R.id.rank_category_rv_item_author)
        mTvCategory = findById(R.id.rank_category_rv_item_cat)
        mTvShortInstro = findById(R.id.rank_category_rv_item_desc)
    }

    override fun onBind(data: BillBookBean, pos: Int) {
        //显示图片
        GlideApp.with(context)
                .load(data.cover)
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .into(mIvCover!!)

        mTvName!!.text = data.title
        mTvAuthor!!.text = data.author
        mTvCategory!!.text = context.getString(R.string.rank_category_item_cat_wordcount, data.cat, data.wordCount)
        mTvShortInstro.text = context.getString(R.string.nb_search_book_last_chapter, data.shortIntro)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.fragment_rank_category_recyclerview_item
    }

}