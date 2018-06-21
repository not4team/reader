package com.book.novel.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.book.ireader.model.bean.packages.SearchBookPackage
import com.book.ireader.ui.base.BaseMVPActivity
import com.book.ireader.ui.base.adapter.BaseListAdapter
import com.book.ireader.widget.RefreshLayout
import com.lereader.novel.R
import com.book.novel.adapter.KeyWordAdapter
import com.book.novel.adapter.SearchBookAdapter
import com.book.novel.presenter.SearchPresenter
import com.book.novel.presenter.contract.SearchContract
import com.book.novel.provider.MySuggestionProvider


/**
 * Created with author.
 * Description:
 * Date: 2018-06-12
 * Time: 上午11:02
 */
class SearchActivity : BaseMVPActivity<SearchContract.Presenter>(), SearchContract.View {
    private lateinit var mQuery: String
    private lateinit var mSearchView: SearchView
    private lateinit var mRefreshLayout: RefreshLayout
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
        toolbar!!.setNavigationOnClickListener {
            if (mSearchView.isIconified) {
                finish()
            } else {
                mSearchView.isIconified = true
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
    }

    override fun initWidget() {
        super.initWidget()
        mRefreshLayout = findViewById(R.id.refresh_layout)
        mRvSearch = findViewById(R.id.refresh_rv_content)
        mRvSearch.layoutManager = LinearLayoutManager(this)
        mSearchBookAdapter = SearchBookAdapter()
        mRvSearch.adapter = mSearchBookAdapter
    }

    override fun initClick() {
        super.initClick()
        mSearchBookAdapter.setOnItemClickListener(object : BaseListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                val mIntent = Intent(this@SearchActivity, BookDetailActivity::class.java)
                mIntent.putExtra(BookDetailActivity.BOOK_ID_INTENT_KEY, mSearchBookAdapter.getItem(pos)._id)
                mIntent.putExtra(BookDetailActivity.BOOK_TILTE_INTENT_KEY, mSearchBookAdapter.getItem(pos).title)
                mIntent.putExtra(BookDetailActivity.BOOK_AUTHOR_INTENT_KEY, mSearchBookAdapter.getItem(pos).author)
                startActivity(mIntent)
            }

        })
    }

    override fun processLogic() {
        super.processLogic()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            mQuery = intent.getStringExtra(SearchManager.QUERY)
            if (mQuery != null) {
                val suggestions = SearchRecentSuggestions(this,
                        MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                suggestions.saveRecentQuery(mQuery, null)
                doMySearch(mQuery)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView = menu.findItem(R.id.action_search).actionView as SearchView
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        mSearchView.isIconified = false
        if (mQuery != null) {
            mSearchView.setQuery(mQuery, false);
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> {
                clearHistory()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun doMySearch(query: String) {
        mRefreshLayout.showLoading()
        mPresenter.searchBook(query)
    }

    fun clearHistory() {
        val suggestions = SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
        suggestions.clearHistory()
    }

    override fun finishHistory(history: List<String>) {

    }

    override fun finishKeyWords(keyWords: List<String>) {

    }

    override fun finishBooks(books: List<SearchBookPackage.BooksBean>) {
        mRefreshLayout.showFinish()
        mSearchBookAdapter.refreshItems(books)
    }

    override fun errorBooks() {
        mRefreshLayout.showError()
    }

    override fun showError() {
        mRefreshLayout.showError()
    }

    override fun complete() {

    }

}