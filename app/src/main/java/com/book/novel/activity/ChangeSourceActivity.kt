package com.book.novel.activity

import android.support.v7.widget.AppCompatSpinner
import android.widget.ArrayAdapter
import com.book.ireader.ui.base.BaseActivity
import com.book.ireader.utils.Constant
import com.book.ireader.utils.SharedPreUtils
import com.book.novel.utils.ParserManager
import com.book.novel.utils.SourceRegistry
import com.lereader.novel.R

/**
 * Created with author.
 * Description:
 * Date: 2018-08-06
 * Time: 下午5:13
 */
class ChangeSourceActivity : BaseActivity() {
    private lateinit var mSpinner: AppCompatSpinner
    override fun getContentId(): Int {
        return R.layout.activity_change_source
    }

    override fun initWidget() {
        super.initWidget()
        mSpinner = findViewById(R.id.change_source_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val sources = ParserManager.getAllSources()
        val adapter = ArrayAdapter<SourceRegistry.Entry>(this,
                android.R.layout.simple_spinner_item, sources)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        mSpinner.adapter = adapter
        val currentSource = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE)
        var index = 0
        for (i in sources.indices) {
            if (sources[i].equals(currentSource)) {
                index = i
                break
            }
        }
        mSpinner.setSelection(index)
    }

    override fun initClick() {
        super.initClick()
    }
}