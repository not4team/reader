package com.book.ireader.model.local;

import android.content.ContentValues;
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
import com.book.ireader.utils.RxUtils;

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

    public static BookDao getInstance(Context context) {
        if (mBookDao == null) {
            synchronized (BookDao.class) {
                if (mBookDao == null) {
                    mBookDao = new BookDao(context);
                }
            }
        }
        return mBookDao;
    }

    @Override
    public CollBookBean getCollBook(String bookId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(CollBookBean.TABLE_NAME, null, CollBookBean.COLUMN_ID + " = ?", new String[]{bookId}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex(CollBookBean.COLUMN_ID));
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
                CollBookBean book = new CollBookBean(id, title, author, shortIntro, cover, category, true, 0,
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

    public void insertOrReplaceCollBook(CollBookBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CollBookBean.COLUMN_ID, bean.get_id());
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
        contentValues.put(BookChapterBean.COLUMN_ID, bookChapterBean.COLUMN_ID);
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
    public void deleteCollBook(String id) {
        getWritableDatabase().execSQL("DELETE FROM " + CollBookBean.TABLE_NAME + " WHERE " + CollBookBean.COLUMN_ID + " = " + id);
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
    public void deleteBookChapter(String bookId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + BookChapterBean.TABLE_NAME + " WHERE " + BookChapterBean.COLUMN_BOOK_ID + " = " + bookId;
        db.execSQL(sql);
    }

    public Single<Void> deleteCollBookInRx(CollBookBean bean) {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                //查看文本中是否存在删除的数据
                deleteBook(bean.get_id());
                //删除目录
                deleteBookChapter(bean.get_id());
                //删除CollBook
                deleteCollBook(bean.get_id());
                e.onSuccess(new Void());
            }
        });
    }

    //删除书籍
    public void deleteBook(String bookId) {
        FileUtils.deleteFile(Constant.BOOK_CACHE_PATH + bookId);
    }

}
