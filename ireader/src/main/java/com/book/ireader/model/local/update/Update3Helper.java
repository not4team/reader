package com.book.ireader.model.local.update;

import android.database.sqlite.SQLiteDatabase;

import com.book.ireader.model.bean.CollBookBean;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-15
 * Time: 下午4:02
 */
public class Update3Helper {
    private static Update3Helper instance;

    public static Update3Helper getInstance() {
        if (instance == null) {
            instance = new Update3Helper();
        }
        return instance;
    }

    public void update(SQLiteDatabase db) {
        updateCollBook(db);
    }

    private void updateCollBook(SQLiteDatabase db) {
        String tableName = CollBookBean.TABLE_NAME;
        String sql = String.format("ALTER TABLE %s ADD COLUMN CATEGORY char(50)", tableName);
        db.execSQL(sql);
    }
}
