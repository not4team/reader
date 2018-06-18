package com.book.novel.adapter

import android.content.Intent
import android.view.View
import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.ui.base.adapter.BaseListAdapter
import com.book.ireader.ui.base.adapter.IViewHolder
import com.book.novel.activity.BookDetailActivity
import com.book.novel.adapter.view.RankCategoryHolder

/**
 * Created with author.
 * Description:
 * Date: 2018/6/16
 * Time: 23:15
 */
class RankCategoryRecyclerAdapter : BaseListAdapter<BillBookBean>() {
    override fun createViewHolder(viewType: Int): IViewHolder<BillBookBean> {
        return RankCategoryHolder()
    }

    override fun refreshItems(list: MutableList<BillBookBean>) {
        mList.clear()
        mList.addAll(list)
    }

    override fun onItemClick(v: View, pos: Int) {
        super.onItemClick(v, pos)
        val mIntent = Intent(v.context, BookDetailActivity::class.java)
        mIntent.putExtra(BookDetailActivity.BOOK_ID_INTENT_KEY, mList[pos]._id)
        mIntent.putExtra(BookDetailActivity.BOOK_TILTE_INTENT_KEY, mList[pos].title)
        mIntent.putExtra(BookDetailActivity.BOOK_AUTHOR_INTENT_KEY, mList[pos].author)
        v.context.startActivity(mIntent)
    }
}