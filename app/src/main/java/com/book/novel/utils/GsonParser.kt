package com.book.novel.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created with author.
 * Description:
 * Date: 2018-06-11
 * Time: 下午2:47
 */
class GsonParser {
    companion object {
        fun <T> jsonConvert(json: String, type: Class<T>): T {
            val adapter = Gson().getAdapter(TypeToken.get(type));
            return adapter.fromJson(json)
        }
    }
}