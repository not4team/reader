package com.book.ireader.model.local.update;

import com.book.ireader.model.gen.CollBookBeanDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

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

    public void update(Database db) {
        updateCollBook(db);
    }

    private void updateCollBook(Database db) {
        Class<? extends AbstractDao<?, ?>> collBookClass = CollBookBeanDao.class;
        DaoConfig daoConfig = new DaoConfig(db, collBookClass);
        String tableName = daoConfig.tablename;
        String sql = String.format("ALTER TABLE %s ADD COLUMN CATEGORY char(50)", tableName);
        db.execSQL(sql);
    }
}
