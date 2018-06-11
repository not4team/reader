package com.book.novel.utils

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

        fun <T> jsonConvert(json: String, typeToken: TypeToken<T>): T {
            val adapter = Gson().getAdapter(typeToken);
            return adapter.fromJson(json)
        }
    }
}