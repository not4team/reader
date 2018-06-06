package com.book.novel.fragment

import com.book.ireader.ui.base.BaseMVPFragment
import com.book.novel.R
import com.book.novel.presenter.RankPresenter

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午4:42
 */
class RankFragment : BaseMVPFragment<RankPresenter>() {
    override fun getContentId(): Int {
        return R.layout.fragment_rank
    }

    override fun complete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindPresenter(): RankPresenter {
        return RankPresenter()
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}