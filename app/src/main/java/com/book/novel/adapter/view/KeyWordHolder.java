package com.book.novel.adapter.view;

import android.widget.TextView;

import com.book.ireader.ui.base.adapter.ViewHolderImpl;
import com.book.novel.R;

/**
 * Created by newbiechen on 17-6-2.
 */

public class KeyWordHolder extends ViewHolderImpl<String> {

    private TextView mTvName;

    @Override
    public void initView() {
        mTvName = findById(R.id.keyword_tv_name);
    }

    @Override
    public void onBind(String data, int pos) {
        mTvName.setText(data);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.search_item_keyword;
    }
}
