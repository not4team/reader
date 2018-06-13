package com.book.novel.adapter

import android.content.Context
import android.view.View
import com.book.ireader.model.bean.BillBookBean
import com.book.novel.GlideApp
import com.book.novel.R
import com.book.novel.adapter.recyclerview.CommonAdapter
import com.book.novel.adapter.recyclerview.base.ViewHolder

/**
 * Created with author.
 * Description:
 * Date: 2018-06-07
 * Time: 下午4:55
 */
class BookCityRecyclerAdapter(context: Context, layoutId: Int) : CommonAdapter<BillBookBean>(context, layoutId) {
    val TAG = "BookCityRecyclerAdapter"
    lateinit var newBooks: MutableList<BillBookBean>
    lateinit var finishedBooks: MutableList<BillBookBean>
    override fun convert(holder: ViewHolder, item: BillBookBean, position: Int) {
        if (position == 1) {
            holder.getView<View>(R.id.bookcity_rv_item_head).visibility = View.VISIBLE
            holder.setText(R.id.bookcity_rv_item_head_text, "新书抢先")
        } else if (position == newBooks.size + 1) {
            holder.getView<View>(R.id.bookcity_rv_item_head).visibility = View.VISIBLE
            holder.setText(R.id.bookcity_rv_item_head_text, "畅销完本")
        } else {
            holder.getView<View>(R.id.bookcity_rv_item_head).visibility = View.GONE
        }
        holder.setText(R.id.bookcity_rv_item_title, item.title)
        holder.setText(R.id.bookcity_rv_item_desc, item.shortIntro)
        holder.setText(R.id.bookcity_rv_item_author, item.author)
        GlideApp.with(mContext).load(item.cover).placeholder(R.mipmap.ic_default_portrait).error(R.mipmap.ic_default_portrait).into(holder.getView(R.id.bookcity_rv_item_cover))
    }

}