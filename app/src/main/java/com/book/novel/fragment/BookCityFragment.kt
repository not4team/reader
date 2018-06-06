package com.book.novel.fragment

import android.os.Bundle
import com.book.ireader.model.bean.packages.BookCityPackage
import com.book.ireader.ui.base.BaseMVPFragment
import com.book.novel.R
import com.book.novel.presenter.BookCityPresenter
import com.book.novel.presenter.contract.BookCityContract

/**
 * Created with author.
 * Description:
 * Date: 2018-06-05
 * Time: 下午4:58
 */
class BookCityFragment : BaseMVPFragment<BookCityPresenter>(), BookCityContract.View {
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mPresenter.load("male")
    }

    override fun show(bookCityPackage: BookCityPackage) {

    }

    override fun getContentId(): Int {
        return R.layout.fragment_bookcity
    }

    override fun bindPresenter(): BookCityPresenter {
        return BookCityPresenter()
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun complete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}