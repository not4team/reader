package com.book.ireader.model.flag;

import android.support.annotation.StringRes;

import com.book.ireader.App;
import com.book.ireader.R;

/**
 * Created by newbiechen on 17-5-1.
 */

public enum BookListType {
    HOT(R.string.nb_fragment_book_list_hot, "last-seven-days"),
    NEWEST(R.string.nb_fragment_book_list_newest, "created"),
    COLLECT(R.string.nb_fragment_book_list_collect, "collectorCount");
    private String typeName;
    private String netName;

    BookListType(@StringRes int typeName, String netName) {
        this.typeName = App.getContext().getString(typeName);
        this.netName = netName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getNetName() {
        return netName;
    }
}