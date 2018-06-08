package com.book.novel.adapter.recyclerview;

import android.content.Context;

import com.book.novel.adapter.recyclerview.base.ItemViewDelegate;
import com.book.novel.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(final Context context, final int layoutId) {
        super(context);
        mContext = context;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);


}
