package com.book.novel.provider

import android.app.SearchManager
import android.content.SearchRecentSuggestionsProvider
import android.database.Cursor
import android.database.CursorWrapper
import android.net.Uri
import com.lereader.novel.R


/**
 * Created with author.
 * Description:
 * Date: 2018/6/17
 * Time: 22:57
 */
class MySuggestionProvider : SearchRecentSuggestionsProvider {
    val mIconUri: String = "android.resource://com.book.novel/" + R.drawable.ic_search_history

    companion object {
        const val AUTHORITY = "com.book.novel.MySuggestionProvider"
        const val MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }

    constructor() {
        setupSuggestions(AUTHORITY, MODE)
    }

    internal inner class Wrapper(c: Cursor) : CursorWrapper(c) {

        override fun getString(columnIndex: Int): String {
            return if (columnIndex != -1 && columnIndex == getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1)) mIconUri else super.getString(columnIndex)

        }
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        return Wrapper(super.query(uri, projection, selection,
                selectionArgs, sortOrder))
    }
}