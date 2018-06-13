package com.book.novel.adapter

import com.book.ireader.ui.base.adapter.BaseListAdapter
import com.book.ireader.ui.base.adapter.IViewHolder
import com.book.novel.adapter.view.KeyWordHolder

/**
 * Created by newbiechen on 17-6-2.
 */

class KeyWordAdapter : BaseListAdapter<String>() {
    override fun createViewHolder(viewType: Int): IViewHolder<String> {
        return KeyWordHolder()
    }
}
