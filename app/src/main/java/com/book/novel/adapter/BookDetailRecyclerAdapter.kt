package com.book.novel.adapter

import android.content.Context
import com.book.ireader.model.bean.BookChapterBean
import com.book.ireader.model.bean.packages.InterestedBookListPackage.BookRecommendBean
import com.book.ireader.utils.Constant
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
class BookDetailRecyclerAdapter(context: Context, layoutId: Int) : CommonAdapter<BookChapterBean>(context, layoutId) {
    override fun convert(holder: ViewHolder, item: BookChapterBean, position: Int) {
        holder.setText(R.id.bookdetail_chapters_Item_tv_name, item.title)
    }

}