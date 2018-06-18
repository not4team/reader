package com.book.ireader.model.local;

import android.database.sqlite.SQLiteException;

import com.book.ireader.App;
import com.book.ireader.BuildConfig;
import com.book.ireader.model.gen.DaoMaster;
import com.book.ireader.model.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by newbiechen on 17-4-26.
 */

public class DaoDbHelper {
    public static final String DB_NAME = "IReader_DB";

    private static volatile DaoDbHelper sInstance;
    private Database mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mSession;

    private DaoDbHelper() {
        //封装数据库的创建、更新、删除
        DaoMaster.DevOpenHelper openHelper = new MyOpenHelper(App.getContext(), DB_NAME, null);
        //获取数据库
        try {
            if (BuildConfig.DEBUG) {
                mDb = openHelper.getWritableDb();
            } else {
                mDb = openHelper.getEncryptedWritableDb("novel");
            }
        } catch (SQLiteException e) {
            //覆盖安装时因为之前存在未加密的数据库
            e.printStackTrace();
        }
        //封装数据库中表的创建、更新、删除
        mDaoMaster = new DaoMaster(mDb);  //合起来就是对数据库的操作
        //对表操作的对象。
        mSession = mDaoMaster.newSession(); //可以认为是对数据的操作
    }


    public static DaoDbHelper getInstance() {
        if (sInstance == null) {
            synchronized (DaoDbHelper.class) {
                if (sInstance == null) {
                    sInstance = new DaoDbHelper();
                }
            }
        }
        return sInstance;
    }

    public DaoSession getSession() {
        return mSession;
    }

    public Database getDatabase() {
        return mDb;
    }

    public DaoSession getNewSession() {
        return mDaoMaster.newSession();
    }
}
