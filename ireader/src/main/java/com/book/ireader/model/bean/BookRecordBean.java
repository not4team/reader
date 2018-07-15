package com.book.ireader.model.bean;

import com.book.ireader.model.annotation.Column;
import com.book.ireader.model.annotation.Id;
import com.book.ireader.model.annotation.Table;

/**
 * Created by newbiechen on 17-5-20.
 */
@Table(name = BookRecordBean.TABLE_NAME)
public class BookRecordBean {
    public static final String TABLE_NAME = "BookRecordBean";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_CHAPTER = "chapter";
    public static final String COLUMN_PAGE_POS = "page_pos";
    //所属的书的id
    @Id
    @Column(name = COLUMN_BOOK_ID)
    private String bookId;
    //阅读到了第几章
    @Column(name = COLUMN_CHAPTER)
    private int chapter;
    //当前的页码
    @Column(name = COLUMN_PAGE_POS)
    private int pagePos;

    public BookRecordBean(String bookId, int chapter, int pagePos) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.pagePos = pagePos;
    }

    public BookRecordBean() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }
}
