package com.book.novel.adapter

import android.content.Intent
import android.view.View
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.ui.base.adapter.BaseListAdapter
import com.book.ireader.ui.base.adapter.IViewHolder
import com.book.novel.activity.BookDetailActivity
import com.book.novel.adapter.view.BookshelfHolder

/**
 * Created with author.
 * Description:
 * Date: 2018-06-15
 * Time: 下午2:51
 */
class BookshelfAdapter : BaseListAdapter<CollBookBean>() {
    override fun createViewHolder(viewType: Int): IViewHolder<CollBookBean> {
        return BookshelfHolder()
    }

    override fun onItemClick(v: View, pos: Int) {
        super.onItemClick(v, pos)
        val mIntent = Intent(v.context, BookDetailActivity::class.java)
        mIntent.putExtra(BookDetailActivity.BOOK_ID_INTENT_KEY, mList.get(pos)._id)
        mIntent.putExtra(BookDetailActivity.BOOK_TILTE_INTENT_KEY, mList.get(pos).title)
        mIntent.putExtra(BookDetailActivity.BOOK_AUTHOR_INTENT_KEY, mList.get(pos).author)
        v.context.startActivity(mIntent)
    }
}