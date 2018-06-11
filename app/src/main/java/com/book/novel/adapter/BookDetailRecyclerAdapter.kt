package com.book.novel.adapter

import android.content.Context
import com.book.ireader.model.bean.packages.InterestedBookListPackage.BookRecommendBean
import com.book.novel.GlideApp
import com.book.novel.R
import com.book.novel.adapter.recyclerview.CommonAdapter
import com.book.novel.adapter.recyclerview.base.ViewHolder

/**
 * Created with author.
 * Description:
 * Date: 2018-06-11
 * Time: 下午1:55
 */
class BookDetailRecyclerAdapter(context: Context, layoutId: Int) : CommonAdapter<BookRecommendBean>(context, layoutId) {
    override fun convert(holder: ViewHolder, item: BookRecommendBean, position: Int) {
        holder.setText(R.id.bookdetail_rv_item_title, item.title)
        holder.setText(R.id.bookdetail_rv_item_author, item.author)
        GlideApp.with(mContext).load(item.cover).placeholder(R.mipmap.ic_default_portrait).error(R.mipmap.ic_default_portrait).into(holder.getView(R.id.bookdetail_rv_item_cover))
    }

}