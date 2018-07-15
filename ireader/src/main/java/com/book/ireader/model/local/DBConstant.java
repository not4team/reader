package com.book.ireader.model.local;

import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.ChapterInfoBean;
import com.book.ireader.model.bean.CollBookBean;

public interface DBConstant {
    public static String DB_NAME = "marketingsign-%s.db";
    /** 用来存储登录用户信息*/
    public static String DB_NAME_USER = "user.db";
    public static int DB_VERSION = 1;
    public static int DB_VERSION_USER = 1;
    public static final Class<?>[] DB_CLASSES = new Class[]{
            CollBookBean.class,
            BookChapterBean.class,
            ChapterInfoBean.class
    };
}
