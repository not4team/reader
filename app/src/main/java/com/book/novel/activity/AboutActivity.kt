package com.book.novel.activity

import android.support.v7.widget.Toolbar
import com.book.ireader.ui.base.BaseActivity
import com.book.novel.R

/**
 * Created with author.
 * Description:
 * Date: 2018/6/17
 * Time: 18:44
 */
class AboutActivity : BaseActivity() {
    override fun getContentId(): Int {
        return R.layout.activity_about
    }

    override fun initToolbar() {
        mToolbar = findViewById(R.id.about_toolbar)
        if (mToolbar != null) {
            supportActionBar(mToolbar)
            setUpToolbar(mToolbar)
        }
    }

    override fun setUpToolbar(toolbar: Toolbar?) {
        super.setUpToolbar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.about)
    }
}