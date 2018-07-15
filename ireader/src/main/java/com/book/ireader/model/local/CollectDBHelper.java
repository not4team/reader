package com.book.ireader.model.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shixq on 2017/7/2.
 */

public class CollectDBHelper extends SQLiteOpenHelper {

    public CollectDBHelper(Context context) {
        this(context, DBConstant.DB_NAME_USER, null, DBConstant.DB_VERSION_USER);
    }
    public CollectDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
