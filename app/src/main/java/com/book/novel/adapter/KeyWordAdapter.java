package com.book.novel.adapter;

import com.book.ireader.ui.base.adapter.BaseListAdapter;
import com.book.ireader.ui.base.adapter.IViewHolder;
import com.book.novel.adapter.view.KeyWordHolder;

/**
 * Created by newbiechen on 17-6-2.
 */

public class KeyWordAdapter extends BaseListAdapter<String> {
    @Override
    protected IViewHolder<String> createViewHolder(int viewType) {
        return new KeyWordHolder();
    }
}
