package com.book.ireader.model.local;

import android.content.Context;

/**
 * Created with author.
 * Description:
 * Date: 2018/7/15
 * Time: 21:24
 */
public class CollectDao extends CollectDBHelper {
    private static volatile CollectDao mCollectDao;

    private CollectDao(Context context) {
        super(context);
    }

    public static CollectDao getInstance(Context context) {
        if (mCollectDao == null) {
            synchronized (CollectDao.class) {
                if (mCollectDao == null) {
                    mCollectDao = new CollectDao(context);
                }
            }
        }
        return mCollectDao;
    }
}
