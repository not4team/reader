package com.book.ireader.model.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shixq on 2017/6/15.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        this(context, DBConstant.DB_NAME, null, DBConstant.DB_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableHelper.createTablesByClasses(db, DBConstant.DB_CLASSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级
    }
}
