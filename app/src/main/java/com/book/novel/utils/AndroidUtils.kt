package com.book.novel.utils

import android.content.Context
import android.support.annotation.DrawableRes
import android.view.View
import com.lereader.novel.R

/**
 * Created with author.
 * Description:
 * Date: 2018/7/15
 * Time: 16:35
 */
class AndroidUtils {
    companion object {
        @DrawableRes
        fun getSelectableItemBackground(context: Context): Int {
            val attrs = intArrayOf(R.attr.selectableItemBackground)
            val typedArray = context.obtainStyledAttributes(attrs)
            val backgroundResource = typedArray.getResourceId(0, 0)
            typedArray.recycle()

            return backgroundResource
        }

        fun setViewWidth(view: View, width: Int) {
            val layoutParams = view.layoutParams
            layoutParams.width = width
            view.layoutParams = layoutParams
        }
    }
}
