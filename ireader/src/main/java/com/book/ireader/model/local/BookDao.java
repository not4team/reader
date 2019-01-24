package com.book.ireader.model.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookRecordBean;
import com.book.ireader.utils.BookManager;
import com.book.ireader.utils.Constant;
import com.book.ireader.utils.FileUtils;
import com.book.ireader.utils.IOUtils;
import com.book.ireader.utils.RxUtils;
import com.book.ireader.utils.SharedPreUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
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

    private BookDao(Context context, String DBName) {
        super(context, DBName, null, DBConstant.DB_VERSION);
    }

    public static BookDao getInstance(Context context) {
        if (mBookDao == null) {
            synchronized (BookDao.class) {
                if (mBookDao == null) {
                    try {
                        String DBName = getDBName();
                        mBookDao = new BookDao(context, DBName);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mBookDao;
    }

    public static void releaseDB() {
        mBookDao.close();
        mBookDao = null;
    }

    public static String getDBName() throws MalformedURLException, UnsupportedEncodingException {
        //获取当前来源
        String bookSource = SharedPreUtils.getInstance().getString(Constant.SHARED_BOOK_SOURCE);
        if (bookSource != null) {
            URL url = new URL(bookSource);
            String encodeHost = Base64.encodeToString(url.getHost().getBytes("utf-8"), Base64.NO_WRAP);
            String dbName = String.format(DBConstant.DB_NAME, encodeHost);
            return dbName;
        } else {
            return DBConstant.DB_NAME;
        }
    }

    //获取书籍列表
    public Single<List<BookChapterBean>> getBookChaptersInRx(String bookId) {
        return Single.create(new SingleOnSubscribe<List<BookChapterBean>>() {
            @Override
            public void subscribe(SingleEmitter<List<BookChapterBean>> e) throws Exception {
                List<BookChapterBean> beans = getBookChapterBeans(bookId);
                e.onSuccess(beans);
            }
        });
    }

    @Override
    public BookRecordBean getBookRecord(String bookId) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + BookRecordBean.TABLE_NAME + " WHERE " + BookRecordBean.COLUMN_BOOK_ID + " = ?";
        BookRecordBean bookRecordBean = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{bookId});
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex(BookRecordBean.COLUMN_BOOK_ID));
                int chapter = cursor.getInt(cursor.getColumnIndex(BookRecordBean.COLUMN_CHAPTER));
                int pagePos = cursor.getInt(cursor.getColumnIndex(BookRecordBean.COLUMN_PAGE_POS));
                bookRecordBean = new BookRecordBean(id, chapter, pagePos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bookRecordBean;
    }

    @Override
    public List<BookChapterBean> getBookChapterBeans(String bookId) {
        SQLiteDatabase db = getReadableDatabase();
        List<BookChapterBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(BookChapterBean.TABLE_NAME, null, BookChapterBean.COLUMN_BOOK_ID + " = ?", new String[]{bookId}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(BookChapterBean.COLUMN_ID));
                    String link = cursor.getString(cursor.getColumnIndex(BookChapterBean.COLUMN_LINK));
                    String title = cursor.getString(cursor.getColumnIndex(BookChapterBean.COLUMN_TITLE));
                    String taskName = cursor.getString(cursor.getColumnIndex(BookChapterBean.COLUMN_TASK_NAME));
                    boolean unreadble = cursor.getInt(cursor.getColumnIndex(BookChapterBean.COLUMN_UNREADBLE)) == 1 ? true : false;
                    String _bookId = cursor.getString(cursor.getColumnIndex(BookChapterBean.COLUMN_BOOK_ID));
                    long start = cursor.getLong(cursor.getColumnIndex(BookChapterBean.COLUMN_START));
                    long end = cursor.getLong(cursor.getColumnIndex(BookChapterBean.COLUMN_END));
                    list.add(new BookChapterBean(id, link, title, taskName, unreadble, _bookId, start, end));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
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
    public void saveBookChapters(List<BookChapterBean> bookChapterBeans) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (BookChapterBean bookChapterBean : bookChapterBeans) {
                saveBookChapter(bookChapterBean);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void saveBookChapter(BookChapterBean bookChapterBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookChapterBean.COLUMN_ID, bookChapterBean.getId());
        contentValues.put(BookChapterBean.COLUMN_LINK, bookChapterBean.getLink());
        contentValues.put(BookChapterBean.COLUMN_TITLE, bookChapterBean.getTitle());
        contentValues.put(BookChapterBean.COLUMN_TASK_NAME, bookChapterBean.getTaskName());
        contentValues.put(BookChapterBean.COLUMN_UNREADBLE, bookChapterBean.getUnreadble());
        contentValues.put(BookChapterBean.COLUMN_BOOK_ID, bookChapterBean.getBookId());
        contentValues.put(BookChapterBean.COLUMN_START, bookChapterBean.getStart());
        contentValues.put(BookChapterBean.COLUMN_END, bookChapterBean.getEnd());
        db.replace(BookChapterBean.TABLE_NAME, null, contentValues);
    }

    @Override
    public void saveBookRecord(BookRecordBean bookRecordBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookRecordBean.COLUMN_BOOK_ID, bookRecordBean.getBookId());
        contentValues.put(BookRecordBean.COLUMN_CHAPTER, bookRecordBean.getChapter());
        contentValues.put(BookRecordBean.COLUMN_PAGE_POS, bookRecordBean.getPagePos());
        db.replace(BookRecordBean.TABLE_NAME, null, contentValues);
    }

    public void saveBookChaptersWithAsync(List<BookChapterBean> bookChapterBeans) {
        Single.create(new SingleOnSubscribe<Void>() {

            @Override
            public void subscribe(SingleEmitter<Void> singleEmitter) throws Exception {
                saveBookChapters(bookChapterBeans);
            }
        }).compose(RxUtils::toSimpleSingle)
                .subscribe();
    }

    @Override
    public void deleteBookChapter(String bookId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + BookChapterBean.TABLE_NAME + " WHERE " + BookChapterBean.COLUMN_BOOK_ID + " = '" + bookId + "'";
        db.execSQL(sql);
    }

    //删除书籍
    public void deleteBook(String bookId) {
        FileUtils.deleteFile(Constant.BOOK_CACHE_PATH + bookId);
    }

}
