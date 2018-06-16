package com.book.novel.presenter.contract

import com.book.ireader.model.bean.BillBookBean
import com.book.ireader.ui.base.BaseContract

/**
 * Created with author.
 * Description:
 * Date: 2018/6/16
 * Time: 22:35
 */
interface RankCategoryContract {
    interface View : BaseContract.BaseView {
        fun show(books: List<BillBookBean>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun load(rankName: String, gender: String, catId: String)
    }
}