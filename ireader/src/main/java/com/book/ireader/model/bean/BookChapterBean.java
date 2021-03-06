package com.book.ireader.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.book.ireader.model.annotation.Column;
import com.book.ireader.model.annotation.Id;
import com.book.ireader.model.annotation.Table;

import java.io.Serializable;

/**
 * Created by newbiechen on 17-5-10.
 * 书的章节链接(作为下载的进度数据)
 * 同时作为网络章节和本地章节 (没有找到更好分离两者的办法)
 */
@Table(name = BookChapterBean.TABLE_NAME)
public class BookChapterBean implements Parcelable {
    private static final long serialVersionUID = 56423411313L;
    /**
     * title : 第一章 他叫白小纯
     * link : http://read.qidian.com/chapter/rJgN8tJ_cVdRGoWu-UQg7Q2/6jr-buLIUJSaGfXRMrUjdw2
     * unreadble : false
     */
    public static final String TABLE_NAME = "BookChapterBean";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_UNREADBLE = "unreadble";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";
    @Id
    @Column(name = COLUMN_ID)
    private String id;
    @Column(name = COLUMN_LINK)
    private String link;
    @Column(name = COLUMN_TITLE)
    private String title;

    //所属的下载任务
    @Column(name = COLUMN_TASK_NAME)
    private String taskName;

    @Column(name = COLUMN_UNREADBLE)
    private boolean unreadble;

    //所属的书籍
    @Column(name = COLUMN_BOOK_ID)
    private String bookId;

    //在书籍文件中的起始位置
    @Column(name = COLUMN_START)
    private long start;

    //在书籍文件中的终止位置
    @Column(name = COLUMN_END)
    private long end;

    public BookChapterBean(String id, String link, String title, String taskName,
                           boolean unreadble, String bookId, long start, long end) {
        this.id = id;
        this.link = link;
        this.title = title;
        this.taskName = taskName;
        this.unreadble = unreadble;
        this.bookId = bookId;
        this.start = start;
        this.end = end;
    }

    public BookChapterBean() {
    }

    protected BookChapterBean(Parcel in) {
        id = in.readString();
        link = in.readString();
        title = in.readString();
        taskName = in.readString();
        unreadble = in.readByte() != 0;
        bookId = in.readString();
        start = in.readLong();
        end = in.readLong();
    }

    public static final Creator<BookChapterBean> CREATOR = new Creator<BookChapterBean>() {
        @Override
        public BookChapterBean createFromParcel(Parcel in) {
            return new BookChapterBean(in);
        }

        @Override
        public BookChapterBean[] newArray(int size) {
            return new BookChapterBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isUnreadble() {
        return unreadble;
    }

    public void setUnreadble(boolean unreadble) {
        this.unreadble = unreadble;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean getUnreadble() {
        return this.unreadble;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "BookChapterBean{" +
                "id='" + id + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", taskName='" + taskName + '\'' +
                ", unreadble=" + unreadble +
                ", bookId='" + bookId + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(link);
        dest.writeString(title);
        dest.writeString(taskName);
        dest.writeByte(this.unreadble ? (byte) 1 : (byte) 0);
        dest.writeString(bookId);
        dest.writeLong(start);
        dest.writeLong(end);
    }
}