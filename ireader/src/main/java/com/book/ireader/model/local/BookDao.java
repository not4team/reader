package com.book.ireader.model.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookRecordBean;
import com.book.ireader.model.bean.CollBookBean;
import com.book.ireader.utils.BookManager;
import com.book.ireader.utils.Constant;
import com.book.ireader.utils.FileUtils;
import com.book.ireader.utils.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created with author.
 * Description:
 * Date: 2018/7/15
 * Time: 21:23
 */
public class BookDao extends DBHelper implements IBookDao {
    private static volatile BookDao mBookDao;

    private BookDao(Context context) {
        super(context);
    }

    public BookDao getInstance(Context context) {
        if (mBookDao == null) {
            synchronized (BookDao.class) {
                if (mBookDao == null) {
                    mBookDao = new BookDao(context);
                }
            }
        }
        return mBookDao;
    }

    /**
     * 存储章节
     *
     * @param folderName
     * @param fileName
     * @param content
     */
    public void saveChapterInfo(String folderName, String fileName, String content) {
        File file = BookManager.getBookFile(folderName, fileName);
        //获取流并存储
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.close(writer);
        }
    }

    @Override
    public void saveCollBookWithAsync(CollBookBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            if (bean.getBookChapterList() != null) {
                // 存储BookChapterBean
                saveBookChapters(bean.getBookChapterList());
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

    @Override
    public void saveBookChapters(List<BookChapterBean> bookChapterBeans) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void deleteCollBook(String id) {
        getWritableDatabase().execSQL("DELETE FROM " + CollBookBean.TABLE_NAME + " WHERE " + CollBookBean.COLUMN_ID + " = " + id);
    }

    public void insertOrReplaceCollBook(CollBookBean bean) {

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

    @Override
    public void swapCollBookOrder(CollBookBean from, CollBookBean to) {
        updateCollBookOrder(from.get_id(), from.getBookOrder());
        updateCollBookOrder(to.get_id(), to.getBookOrder());
    }

    public void updateCollBookOrder(String bookId, int bookOrder) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + CollBookBean.TABLE_NAME + " SET " + CollBookBean.COLUMN_BOOK_ORDER + " = " + bookOrder + " WHERE " + CollBookBean.COLUMN_ID + " ='" + bookId + "'";
        db.execSQL(sql);
    }

    @Override
    public CollBookBean getCollBook(String bookId) {
        return null;
    }

    @Override
    public List<CollBookBean> getOrderBooks() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + CollBookBean.TABLE_NAME + " ORDER BY " + CollBookBean.COLUMN_BOOK_ORDER + " DESC";
        Cursor cursor = null;
        List<CollBookBean> list = new ArrayList<>();
        try {
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_ID));
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
                CollBookBean book = new CollBookBean(id, title, author, shortIntro, cover, category, true, 0,
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

    @Override
    public BookRecordBean getBookRecord(String bookId) {
        return null;
    }

    @Override
    public void deleteBookChapter(String bookId) {

    }

    public Single<Void> deleteCollBookInRx(CollBookBean bean) {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                //查看文本中是否存在删除的数据
                deleteBook(bean.get_id());
                //删除目录
//                deleteBookChapter(bean.get_id());
                //删除CollBook

                e.onSuccess(new Void());
            }
        });
    }

    //删除书籍
    public void deleteBook(String bookId) {
        FileUtils.deleteFile(Constant.BOOK_CACHE_PATH + bookId);
    }

}
