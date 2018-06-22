package com.book.novel.adapter.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.book.ireader.App
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.ui.base.adapter.ViewHolderImpl
import com.book.ireader.utils.Constant
import com.book.novel.GlideApp
import com.lereader.novel.R

/**
 * Created with author.
 * Description:
 * Date: 2018-06-15
 * Time: 下午2:53
 */
class BookshelfHolder : ViewHolderImpl<CollBookBean>() {
    private lateinit var mIvCover: ImageView
    private lateinit var mTvName: TextView
    private lateinit var mTvAuthor: TextView
    private lateinit var mTvCategory: TextView
    private lateinit var mLastChapter: TextView
    private lateinit var mUpdateTip: View

    override fun initView() {
        mIvCover = findById(R.id.bookshelf_iv_cover)
        mTvName = findById(R.id.bookshelf_tv_name)
        mTvAuthor = findById(R.id.bookshelf_tv_author)
        mTvCategory = findById(R.id.bookshelf_tv_category)
        mLastChapter = findById(R.id.bookshelf_tv_lastchapter)
        mUpdateTip = findById(R.id.bookshelf_v_red_tip)
    }

    override fun onBind(data: CollBookBean, pos: Int) {
        //显示图片
        GlideApp.with(App.getContext())
                .load(Constant.IMG_BASE_URL + data.cover)
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .into(mIvCover)

        mTvName.text = data.title

        mTvAuthor.text = context.getString(R.string.nb_search_book_author, data.author)
        mTvCategory.text = context.getString(R.string.nb_search_book_category, data.category)
        mLastChapter.text = context.getString(R.string.nb_search_book_last_chapter, data.lastChapter)
        mUpdateTip.visibility = if (data.isUpdate()) View.VISIBLE else View.GONE
    }

    override fun getItemLayoutId(): Int {
        return R.layout.fragment_bookshelf_recyclerview_item
    }

}