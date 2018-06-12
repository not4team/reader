package com.book.novel.presenter

import android.util.Log
import com.book.ireader.App
import com.book.ireader.model.remote.RemoteRepository
import com.book.ireader.ui.base.RxPresenter
import com.book.ireader.utils.LogUtils
import com.book.ireader.utils.RxUtils
import com.book.novel.presenter.contract.SearchContract

/**
 * Created by newbiechen on 17-6-2.
 */

class SearchPresenter : RxPresenter<SearchContract.View>(), SearchContract.Presenter {
    private val TAG = "SearchPresenter"

    override fun searchHistory() {

    }

    override fun searchHotWord() {
        try {
            val disp = RemoteRepository.getInstance(App.getContext())
                    .hotWords
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            { bean -> mView.finishHistory(bean) }
                    ) { e -> LogUtils.e(e) }
            addDisposable(disp)
        } catch (e: Exception) {
            Log.e(TAG, "searchHotWord error", e)
        }

    }

    override fun searchKeyWord(query: String) {
        try {
            val disp = RemoteRepository.getInstance(App.getContext())
                    .getKeyWords(query)
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            { bean -> mView.finishKeyWords(bean) }
                    ) { e -> LogUtils.e(e) }
            addDisposable(disp)
        } catch (e: Exception) {
            Log.e(TAG, "searchKeyWord error", e)
        }

    }

    override fun searchBook(query: String) {
        try {
            val disp = RemoteRepository.getInstance(App.getContext())
                    .getSearchBooks(query)
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            { bean -> mView.finishBooks(bean) }
                    ) { e ->
                        LogUtils.e(e)
                        mView.errorBooks()
                    }
            addDisposable(disp)
        } catch (e: Exception) {
            Log.e(TAG, "searchBook error", e)
        }

    }
}
