package com.book.novel.adapter;

import com.book.ireader.model.bean.packages.SearchBookPackage;
import com.book.ireader.ui.base.adapter.BaseListAdapter;
import com.book.ireader.ui.base.adapter.IViewHolder;
import com.book.novel.adapter.view.SearchBookHolder;

/**
 * Created by newbiechen on 17-6-2.
 */

public class SearchBookAdapter extends BaseListAdapter<SearchBookPackage.BooksBean> {
    @Override
    protected IViewHolder<SearchBookPackage.BooksBean> createViewHolder(int viewType) {
        return new SearchBookHolder();
    }
}
