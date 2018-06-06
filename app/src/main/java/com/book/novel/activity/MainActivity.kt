package com.book.novel.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.book.novel.R
import com.book.novel.fragment.BookCityFragment
import com.book.novel.fragment.BookShelfFragment
import com.book.novel.fragment.RankFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mBookShelfFragment: Fragment
    lateinit var mBookCityFragment: Fragment
    lateinit var mRankFragment: Fragment
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_bookshelf -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_fl_content, mBookShelfFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bookcity -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_fl_content, mBookCityFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_rank -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_fl_content, mRankFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBookShelfFragment = BookShelfFragment() as Fragment
        mBookCityFragment = BookCityFragment() as Fragment
        mRankFragment = RankFragment() as Fragment
        supportFragmentManager.beginTransaction().add(R.id.main_fl_content, mBookShelfFragment).commit()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
