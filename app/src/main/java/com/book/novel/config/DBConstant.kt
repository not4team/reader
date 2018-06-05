package com.book.novel.config

interface DBConstant {
    companion object {
        val DB_NAME = "marketingsign-%s.db"
        /** 用来存储登录用户信息 */
        val DB_NAME_USER = "user.db"
        val DB_VERSION = 1
        val DB_VERSION_USER = 1
        val DB_CLASSES = arrayOf<Class<*>>(BookshelfModel::class.java)
    }
}
