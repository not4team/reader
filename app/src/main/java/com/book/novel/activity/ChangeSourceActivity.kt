package com.book.novel.activity

import android.content.Intent
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.book.ireader.ui.base.BaseMVPActivity
import com.book.ireader.utils.Constant
import com.book.ireader.utils.SharedPreUtils
import com.book.ireader.utils.ToastUtils
import com.book.novel.presenter.ChangeSourcePresenter
import com.book.novel.presenter.contract.ChangeSourceContract
import com.book.novel.utils.ParserManager
import com.book.novel.utils.SourceRegistry
import com.lereader.novel.R

/**
 * Created with author.
 * Description:
 * Date: 2018-08-06
 * Time: 下午5:13
 */
class ChangeSourceActivity : BaseMVPActivity<ChangeSourceContract.Presenter>(), ChangeSourceContract.View {
    private lateinit var mSpinner: AppCompatSpinner
    private lateinit var mSources: List<SourceRegistry.Entry>
    private lateinit var mLoading: LinearLayout
    private var mCurrentPosition = 0
    override fun getContentId(): Int {
        return R.layout.activity_change_source
    }

    override fun bindPresenter(): ChangeSourceContract.Presenter {
        return ChangeSourcePresenter()
    }

    override fun showError() {
        mLoading.visibility = View.GONE
        setDefaultSelection()
        ToastUtils.show("切换失败!")
    }

    override fun complete() {
        mLoading.visibility = View.GONE
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun initToolbar() {
        mToolbar = findViewById(R.id.change_source_toolbar)
        if (mToolbar != null) {
            supportActionBar(mToolbar)
            setUpToolbar(mToolbar)
        }
    }

    override fun setUpToolbar(toolbar: Toolbar?) {
        super.setUpToolbar(toolbar)
        supportActionBar?.title = resources.getString(R.string.change_source)
    }

    override fun initWidget() {
        super.initWidget()
        mLoading = findViewById(R.id.change_source_loading)
        mSpinner = findViewById(R.id.change_source_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        mSources = ParserManager.getAllSources()
        val adapter = ArrayAdapter<SourceRegistry.Entry>(this,
                android.R.layout.simple_spinner_item, mSources)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        mSpinner.adapter = adapter
        setDefaultSelection()
    }

    fun setDefaultSelection() {
        val currentSource = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
        var index = 0
        for (i in mSources.indices) {
            if (mSources[i].equals(currentSource)) {
                index = i
                break
            }
        }
        mCurrentPosition = index
        mSpinner.setSelection(index)
    }

    override fun initClick() {
        super.initClick()
        mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (mCurrentPosition != position) {
                    val oldEntry = mSources[mCurrentPosition]
                    val newEntry = mSources[position]
                    mLoading.visibility = View.VISIBLE
                    SharedPreUtils.getInstance().putString(Constant.SHARED_BOOK_SOURCE, newEntry.url)
                    //替换书架书籍链接
                    mPresenter.startChange(oldEntry.source, newEntry.source)
                    mCurrentPosition = position
                }
            }

        }
    }
}