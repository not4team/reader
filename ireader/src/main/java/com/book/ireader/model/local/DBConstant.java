package com.book.ireader.model.local;

import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookRecordBean;
import com.book.ireader.model.bean.ChapterInfoBean;
import com.book.ireader.model.bean.CollBookBean;

public interface DBConstant {
    public static String DB_NAME = "lereader-%s.db";
    public static String DB_NAME_USER = "collect.db";
    public static int DB_VERSION = 1;
    public static int DB_VERSION_COLLECT = 1;
    public static final Class<?>[] DB_CLASSES = new Class[]{
            CollBookBean.class,
            BookChapterBean.class,
            ChapterInfoBean.class,
            BookRecordBean.class
    };
}
