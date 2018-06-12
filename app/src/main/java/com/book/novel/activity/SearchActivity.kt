package com.book.novel.activity

import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.ui.base.BaseMVPActivity
import com.book.novel.R
import com.book.novel.adapter.KeyWordAdapter
import com.book.novel.adapter.SearchBookAdapter
import com.book.novel.presenter.SearchPresenter
import com.book.novel.presenter.contract.SearchContract
import me.gujun.android.taggroup.TagGroup


/**
 * Created with author.
 * Description:
 * Date: 2018-06-12
 * Time: 上午11:02
 */
class SearchActivity : BaseMVPActivity<SearchContract.Presenter>(), SearchContract.View {
    private lateinit var mTagHistory: TagGroup
    private lateinit var mRvSearch: RecyclerView
    private lateinit var mKeyWordAdapter: KeyWordAdapter
    private lateinit var mSearchBookAdapter: SearchBookAdapter
    override fun getContentId(): Int {
        return R.layout.activity_search
    }

    override fun bindPresenter(): SearchContract.Presenter {
        return SearchPresenter()
    }

    override fun initToolbar() {
        mToolbar = findViewById(R.id.search_toolbar)
        if (mToolbar != null) {
            supportActionBar(mToolbar)
            setUpToolbar(mToolbar)
        }
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        super.setUpToolbar(toolbar)
    }

    override fun initWidget() {
        super.initWidget()
    }

    override fun processLogic() {
        super.processLogic()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun finishHistory(history: List<String>) {

    }

    override fun finishKeyWords(keyWords: List<String>) {

    }

    override fun finishBooks(books: List<SearchBookPackage.BooksBean>) {

    }

    override fun errorBooks() {

    }

    override fun showError() {

    }

    override fun complete() {

    }

}