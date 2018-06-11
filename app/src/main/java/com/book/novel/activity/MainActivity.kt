package com.book.novel.activity

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.book.ireader.ui.base.BaseActivity
import com.book.novel.R
import com.book.novel.fragment.BookCityFragment
import com.book.novel.fragment.BookShelfFragment
import com.book.novel.fragment.RankFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    lateinit var mBookShelfFragment: Fragment
    lateinit var mBookCityFragment: Fragment
    lateinit var mRankFragment: Fragment
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_bookshelf -> {
                val mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction.hide(mBookCityFragment)
                mFragmentTransaction.hide(mRankFragment)
                mFragmentTransaction.show(mBookShelfFragment)
                mFragmentTransaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bookcity -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_fl_content, mBookCityFragment).commit()
                val mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction.hide(mBookShelfFragment)
                mFragmentTransaction.hide(mRankFragment)
                mFragmentTransaction.show(mBookCityFragment)
                mFragmentTransaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_rank -> {
                val mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction.hide(mBookShelfFragment)
                mFragmentTransaction.hide(mBookCityFragment)
                mFragmentTransaction.show(mRankFragment)
                mFragmentTransaction.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun getContentId(): Int {
        return R.layout.activity_main
    }

    override fun initToolbar() {
        mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        if (mToolbar != null) {
            supportActionBar(mToolbar)
            setUpToolbar(mToolbar)
        }
    }

    override fun setUpToolbar(toolbar: Toolbar?) {
        super.setUpToolbar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.app_name)
    }

    override fun initWidget() {
        super.initWidget()
        mBookShelfFragment = BookShelfFragment() as Fragment
        mBookCityFragment = BookCityFragment() as Fragment
        mRankFragment = RankFragment() as Fragment
        supportFragmentManager.beginTransaction().add(R.id.main_fl_content, mBookShelfFragment).commit()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
