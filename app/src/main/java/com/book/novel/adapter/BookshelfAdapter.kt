package com.book.novel.adapter

import android.content.Intent
import android.view.View
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookRepository
import com.book.ireader.ui.base.adapter.BaseListAdapter
import com.book.ireader.ui.base.adapter.IViewHolder
import com.book.novel.activity.BookDetailActivity
import com.book.novel.adapter.view.BookshelfHolder
import java.util.*

/**
 * Created with author.
 * Description:
 * Date: 2018-06-15
 * Time: 下午2:51
 */
class BookshelfAdapter : BaseListAdapter<CollBookBean>(), ItemTouchHelperAdapter {
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        //交换位置
        Collections.swap(mList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        swapOrder(mList[fromPosition], mList[toPosition])
    }

    private fun swapOrder(collBook1: CollBookBean, collBook2: CollBookBean) {
        val order = collBook1.bookOrder
        collBook1.bookOrder = collBook2.bookOrder
        collBook2.bookOrder = order
        collBook1.update()
        collBook2.update()
    }

    override fun onItemDissmiss(position: Int) {
        //移除数据
        mList.removeAt(position)
        notifyItemRemoved(position)
        BookRepository.getInstance().deleteCollBookInRx(mList[position]).subscribe()
    }

    override fun createViewHolder(viewType: Int): IViewHolder<CollBookBean> {
        return BookshelfHolder()
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