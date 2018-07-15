package com.book.novel.adapter

import android.util.Log
import com.book.ireader.App
import com.book.ireader.RxBus
import com.book.ireader.event.BookShelfRefreshEvent
import com.book.ireader.model.bean.CollBookBean
import com.book.ireader.model.local.BookDao
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
    val TAG = "BookshelfAdapter"
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        swapOrder(mList[fromPosition], mList[toPosition])
        //交换位置
        Collections.swap(mList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    private fun swapOrder(collBook1: CollBookBean, collBook2: CollBookBean) {
        Log.e(TAG, "swap pre collBook1.bookOrder : " + collBook1.bookOrder + ", collBook2.bookOrder : " + collBook2.bookOrder)
        val order = collBook1.bookOrder
        collBook1.bookOrder = collBook2.bookOrder
        collBook2.bookOrder = order

        Single.create<Unit> {
            Log.e(TAG, "swap after collBook1.bookOrder : " + collBook1.bookOrder + ", collBook2.bookOrder : " + collBook2.bookOrder)
            BookDao.getInstance(App.getContext()).swapCollBookOrder(collBook1, collBook2)
            it.onSuccess(Unit)
        }.compose(RxUtils::toSimpleSingle).subscribe { success ->
            Log.e("BookshelfAdapter", "swapOrder success")
        }
    }

    override fun onItemDissmiss(position: Int) {
        BookDao.getInstance(App.getContext()).deleteCollBookInRx(mList[position]).subscribe()
        //移除数据
        val _id = mList[position]._id
        mList.removeAt(position)
        RxBus.getInstance().post(BookShelfRefreshEvent().setId(_id).setType(BookShelfRefreshEvent.EVENT_TYPE_DELETE))
    }

    override fun createViewHolder(viewType: Int): IViewHolder<CollBookBean> {
        return BookshelfHolder()
    }

}