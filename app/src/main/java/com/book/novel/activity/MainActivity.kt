package com.book.novel.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.book.ireader.ui.base.BaseActivity
import com.book.ireader.utils.Constant
import com.book.ireader.utils.SharedPreUtils
import com.book.novel.fragment.BookCityFragment
import com.book.novel.fragment.BookShelfFragment
import com.book.novel.fragment.RankFragment
import com.lereader.novel.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    val TAG = "MainActivity"
    private var mBookShelfFragment: Fragment? = null
    private var mBookCityFragment: Fragment? = null
    private var mRankFragment: Fragment? = null
    private var mCurrFragmentTag: String? = null
    private var mSearchView: SearchView? = null
    private var mSpinner: AppCompatSpinner? = null
    private var mSexPosition = 0
    val BUNDLE_FRAGMENT_TAG = "bundle_fragment_tag"
    val BOOKSHELF_TAG = "bookshelf_fragment_tag"
    val BOOKCITY_TAG = "bookcity_fragment_tag"
    val RANK_TAG = "rank_fragment_tag"
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_bookshelf -> {
                val mFragmentTransaction = supportFragmentManager.beginTransaction()
                if (mBookCityFragment != null) {
                    mFragmentTransaction.hide(mBookCityFragment)
                }
                if (mRankFragment != null) {
                    mFragmentTransaction.hide(mRankFragment)
                }
                mFragmentTransaction.show(mBookShelfFragment)
                mFragmentTransaction.commit()
                mCurrFragmentTag = BOOKSHELF_TAG
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bookcity -> {
                val mFragmentTransaction = supportFragmentManager.beginTransaction()
                if (mBookShelfFragment != null) {
                    mFragmentTransaction.hide(mBookShelfFragment)
                }
                if (mRankFragment != null) {
                    mFragmentTransaction.hide(mRankFragment)
                }
                if (supportFragmentManager.findFragmentByTag(BOOKCITY_TAG) == null) {
                    mBookCityFragment = BookCityFragment()
                    mFragmentTransaction.add(R.id.main_fl_content, mBookCityFragment, BOOKCITY_TAG)
                } else {
                    mFragmentTransaction.show(mBookCityFragment)
                }
                mFragmentTransaction.commit()
                mCurrFragmentTag = BOOKCITY_TAG
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_rank -> {
                val mFragmentTransaction = supportFragmentManager.beginTransaction()
                if (mBookShelfFragment != null) {
                    mFragmentTransaction.hide(mBookShelfFragment)
                }
                if (mBookCityFragment != null) {
                    mFragmentTransaction.hide(mBookCityFragment)
                }
                if (supportFragmentManager.findFragmentByTag(RANK_TAG) == null) {
                    mRankFragment = RankFragment()
                    mFragmentTransaction.add(R.id.main_fl_content, mRankFragment, RANK_TAG)
                } else {
                    mFragmentTransaction.show(mRankFragment)
                }
                mFragmentTransaction.commit()
                mCurrFragmentTag = RANK_TAG
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun getContentId(): Int {
        return R.layout.activity_main
    }

    override fun initToolbar() {
        mToolbar = findViewById(R.id.main_toolbar)
        if (mToolbar != null) {
            supportActionBar(mToolbar)
            setUpToolbar(mToolbar)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            val currFragmentTAG = savedInstanceState.getString(BUNDLE_FRAGMENT_TAG)
            if (currFragmentTAG != null) {
                mBookShelfFragment = supportFragmentManager.findFragmentByTag(BOOKSHELF_TAG)
                mBookCityFragment = supportFragmentManager.findFragmentByTag(BOOKCITY_TAG)
                mRankFragment = supportFragmentManager.findFragmentByTag(RANK_TAG)
                val mFragmentTransaction = supportFragmentManager.beginTransaction()
                when (currFragmentTAG) {
                    BOOKSHELF_TAG -> {
                        if (mBookCityFragment != null) {
                            mFragmentTransaction.hide(mBookCityFragment)
                        }
                        if (mRankFragment != null) {
                            mFragmentTransaction.hide(mRankFragment)
                        }
                        if (mBookShelfFragment == null) {
                            Log.e(TAG, "onRestoreInstanceState mBookShelfFragment == null")
                            mBookShelfFragment = BookShelfFragment()
                            mFragmentTransaction.add(R.id.main_fl_content, mBookShelfFragment, BOOKSHELF_TAG)
                        } else {
                            Log.e(TAG, "onRestoreInstanceState mBookShelfFragment show")
                            mFragmentTransaction.show(mBookShelfFragment)
                        }
                    }
                    BOOKCITY_TAG -> {
                        if (mRankFragment != null) {
                            mFragmentTransaction.hide(mRankFragment)
                        }
                        if (mBookShelfFragment != null) {
                            mFragmentTransaction.hide(mBookShelfFragment)
                        }
                        if (mBookCityFragment == null) {
                            mBookCityFragment = BookCityFragment()
                            mFragmentTransaction.add(R.id.main_fl_content, mBookCityFragment, BOOKCITY_TAG)
                        } else {
                            mFragmentTransaction.show(mBookCityFragment)
                        }
                    }
                    RANK_TAG -> {
                        if (mBookShelfFragment != null) {
                            mFragmentTransaction.hide(mBookShelfFragment)
                        }
                        if (mBookCityFragment != null) {
                            mFragmentTransaction.hide(mBookCityFragment)
                        }
                        if (mRankFragment == null) {
                            mRankFragment = RankFragment()
                            mFragmentTransaction.add(R.id.main_fl_content, mRankFragment, RANK_TAG)
                        } else {
                            mFragmentTransaction.show(mRankFragment)
                        }
                    }
                }
                mFragmentTransaction.commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_FRAGMENT_TAG, mCurrFragmentTag)
    }

    override fun setUpToolbar(toolbar: Toolbar?) {
        super.setUpToolbar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.app_name)
        toolbar!!.setNavigationOnClickListener {
            if (mSearchView!!.isIconified) {
                finish()
            } else {
                mSearchView!!.isIconified = true
            }
        }
        mSpinner = findViewById(R.id.main_toolbar_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.male_or_female, R.layout.toolbar_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        mSpinner?.adapter = adapter
        val currentGender = SharedPreUtils.getInstance().getString(Constant.SHARED_SEX)
        if (TextUtils.isEmpty(currentGender)) {
            mSpinner?.setSelection(0)
            mSexPosition = 0
        } else {
            if (currentGender == Constant.SEX_BOY) {
                mSpinner?.setSelection(0)
                mSexPosition = 0
            } else {
                mSpinner?.setSelection(1)
                mSexPosition = 1
            }
        }
        mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    SharedPreUtils.getInstance().putString(Constant.SHARED_SEX, Constant.SEX_BOY)
                } else {
                    SharedPreUtils.getInstance().putString(Constant.SHARED_SEX, Constant.SEX_GIRL)
                }
                if (mSexPosition != position) {
                    mSexPosition = position
                    if (mBookCityFragment != null) {
                        val fragment = mBookCityFragment as BookCityFragment
                        fragment.refresh()
                    }
                    if (mRankFragment != null) {
                        val fragment = mRankFragment as RankFragment
                        fragment.refresh()
                    }
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            mBookShelfFragment = BookShelfFragment()
            val mFragmentTransaction = supportFragmentManager.beginTransaction()
            Log.e(TAG, "initWidget mBookShelfFragment add")
            mFragmentTransaction.add(R.id.main_fl_content, mBookShelfFragment, BOOKSHELF_TAG)
            mFragmentTransaction.commit()
            mCurrFragmentTag = BOOKSHELF_TAG
        }
    }

    override fun initWidget() {
        super.initWidget()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView = menu.findItem(R.id.action_search).actionView as SearchView
        mSearchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        mSearchView?.setOnCloseListener {
            mSpinner?.visibility = View.VISIBLE
            false
        }
        mSearchView?.setOnSearchClickListener {
            mSpinner?.visibility = View.GONE
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
            R.id.change_source -> {
                startActivity(Intent(this, ChangeSourceActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        mSearchView?.isIconified = true
        mSearchView?.isIconified = true
    }
}
