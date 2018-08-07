package com.book.ireader.model.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.book.ireader.App;
import com.book.ireader.model.bean.CollBookBean;
import com.book.ireader.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

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

    //获取收藏书籍
    public CollBookBean getCollBook(String bookId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(CollBookBean.TABLE_NAME, null, CollBookBean.COLUMN_ID + " = ?", new String[]{bookId}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_ID));
                String link = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_LINK));
                String title = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_AUTHOR));
                String shortIntro = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_SHORT_INTRO));
                String cover = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_COVER));
                String category = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_CATEGORY));
                String updateTime = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_UPDATED_TIME));
                String lastReadTime = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_LAST_READ_TIME));
                int chaptersCount = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_CHAPTERS_COUNT));
                String lastChapter = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_LAST_CHAPTER));
                boolean isUpdate = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_IS_UPDATE)) == 1 ? true : false;
                boolean isLocal = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_IS_LOCAL)) == 1 ? true : false;
                int bookOrder = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_BOOK_ORDER));
                CollBookBean book = new CollBookBean(id, link, title, author, shortIntro, cover, category, true, 0,
                        0, updateTime, lastReadTime, chaptersCount, lastChapter, isUpdate, isLocal, bookOrder);
                return book;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    //获取排序后的收藏书籍
    public List<CollBookBean> getOrderBooks() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + CollBookBean.TABLE_NAME + " ORDER BY " + CollBookBean.COLUMN_BOOK_ORDER + " DESC";
        Cursor cursor = null;
        List<CollBookBean> list = new ArrayList<>();
        try {
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_ID));
                String link = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_LINK));
                String title = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_AUTHOR));
                String shortIntro = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_SHORT_INTRO));
                String cover = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_COVER));
                String category = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_CATEGORY));
                String updated = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_UPDATED_TIME));
                String lastRead = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_LAST_READ_TIME));
                int chaptersCount = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_CHAPTERS_COUNT));
                String lastChapter = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_LAST_CHAPTER));
                int isUpdate = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_IS_UPDATE));
                int isLocal = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_IS_LOCAL));
                int bookOrder = cursor.getInt(cursor.getColumnIndex(CollBookBean.COLUMN_BOOK_ORDER));
                CollBookBean book = new CollBookBean(id, link, title, author, shortIntro, cover, category, true, 0,
                        0, updated, lastRead, chaptersCount, lastChapter, isUpdate == 1 ? true : false, isLocal == 1 ? true : false, bookOrder);
                list.add(book);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void insertOrReplaceCollBook(CollBookBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //title + author当主键
        String id = StringUtils.base64Encode(bean.getTitle() + "," + bean.getAuthor());
        contentValues.put(CollBookBean.COLUMN_ID, id);
        contentValues.put(CollBookBean.COLUMN_LINK, bean.getLink());
        contentValues.put(CollBookBean.COLUMN_TITLE, bean.getTitle());
        contentValues.put(CollBookBean.COLUMN_AUTHOR, bean.getAuthor());
        contentValues.put(CollBookBean.COLUMN_SHORT_INTRO, bean.getShortIntro());
        contentValues.put(CollBookBean.COLUMN_COVER, bean.getCover());
        contentValues.put(CollBookBean.COLUMN_CATEGORY, bean.getCategory());
        contentValues.put(CollBookBean.COLUMN_UPDATED_TIME, bean.getUpdated());
        contentValues.put(CollBookBean.COLUMN_LAST_READ_TIME, bean.getLastRead());
        contentValues.put(CollBookBean.COLUMN_CHAPTERS_COUNT, bean.getChaptersCount());
        contentValues.put(CollBookBean.COLUMN_LAST_CHAPTER, bean.getLastChapter());
        contentValues.put(CollBookBean.COLUMN_IS_UPDATE, bean.getIsUpdate());
        contentValues.put(CollBookBean.COLUMN_IS_LOCAL, bean.getIsLocal());
        contentValues.put(CollBookBean.COLUMN_BOOK_ORDER, bean.getBookOrder());
        db.replace(CollBookBean.TABLE_NAME, null, contentValues);
    }

    //交换书架顺序
    public void swapCollBookOrder(CollBookBean from, CollBookBean to) {
        updateCollBookOrder(from.get_id(), from.getBookOrder());
        updateCollBookOrder(to.get_id(), to.getBookOrder());
    }

    public void updateCollBookOrder(String bookId, int bookOrder) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + CollBookBean.TABLE_NAME + " SET " + CollBookBean.COLUMN_BOOK_ORDER + " = " + bookOrder + " WHERE " + CollBookBean.COLUMN_ID + " ='" + bookId + "'";
        db.execSQL(sql);
    }

    public void updateCollBooks(List<CollBookBean> bookBeans) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (CollBookBean book : bookBeans) {
                insertOrReplaceCollBook(book);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //收藏书籍
    public void saveCollBookWithAsync(CollBookBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            if (bean.getBookChapterList() != null) {
                // 存储BookChapterBean
                BookDao.getInstance(App.getContext()).saveBookChapters(bean.getBookChapterList());
            }
            //存储CollBook (确保先后顺序，否则出错)
            int maxOrder = getMaxCollBookOrder();
            bean.setBookOrder(++maxOrder);
            insertOrReplaceCollBook(bean);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public int getMaxCollBookOrder() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT MAX(" + CollBookBean.COLUMN_BOOK_ORDER + ") FROM " + CollBookBean.TABLE_NAME;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public void deleteCollBook(String bookId) {
        String sql = "DELETE FROM " + CollBookBean.TABLE_NAME + " WHERE " + CollBookBean.COLUMN_ID + " = '" + bookId + "'";
        getWritableDatabase().execSQL(sql);
    }

    public Single<Void> deleteCollBookInRx(CollBookBean bean) {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                //查看文本中是否存在删除的数据
                BookDao.getInstance(App.getContext()).deleteBook(bean.get_id());
                //删除目录
                BookDao.getInstance(App.getContext()).deleteBookChapter(bean.get_id());
                //删除CollBook
                deleteCollBook(bean.get_id());
                e.onSuccess(new Void());
            }
        });
    }
}
