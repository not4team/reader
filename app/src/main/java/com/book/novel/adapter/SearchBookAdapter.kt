package com.book.novel.adapter

import com.book.ireader.model.bean.BookDetailBean
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.ui.base.adapter.BaseListAdapter
import com.book.ireader.ui.base.adapter.IViewHolder
import com.book.novel.adapter.view.SearchBookHolder

/**
 * Created by newbiechen on 17-6-2.
 */

class SearchBookAdapter : BaseListAdapter<BookDetailBean>() {
    override fun createViewHolder(viewType: Int): IViewHolder<BookDetailBean> {
        return SearchBookHolder()
    }
}
