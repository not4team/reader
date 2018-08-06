package com.book.novel.utils

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.Base64
import android.view.View
import com.book.ireader.utils.Constant
import com.book.ireader.utils.SharedPreUtils
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
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

        fun base64Encode(content: String): String {
            return Base64.encodeToString(content.toByteArray(charset("utf-8")), Base64.NO_WRAP)
        }

        fun base64Decode(content: String): String {
            return String(Base64.decode(content.toByteArray(charset("utf-8")), Base64.NO_WRAP), charset("utf-8"))
        }

        fun generateGlideUrl(url: String): GlideUrl {
            return GlideUrl(url, LazyHeaders.Builder()
                    .addHeader("Accept", "*/*")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3")
                    .addHeader("Cache-Control", "max-age=0")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Referer", url)
                    .build())
        }
    }
}
