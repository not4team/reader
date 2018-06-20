package com.book.novel.adapter

import android.util.Log
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookRepository
import com.book.ireader.ui.base.adapter.BaseListAdapter
import com.book.ireader.ui.base.adapter.IViewHolder
import com.book.ireader.utils.RxUtils
import com.book.novel.adapter.view.BookshelfHolder
import io.reactivex.Single
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

        Single.create<Unit> {
            collBook1.update()
            collBook2.update()
            it.onSuccess(Unit)
        }.compose(RxUtils::toSimpleSingle).subscribe { success ->
            Log.e("BookshelfAdapter", "swapOrder success")
        }
    }

    override fun onItemDissmiss(position: Int) {
        BookRepository.getInstance().deleteCollBookInRx(mList[position]).subscribe()
        //移除数据
        mList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun createViewHolder(viewType: Int): IViewHolder<CollBookBean> {
        return BookshelfHolder()
    }
}