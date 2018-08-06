package com.book.ireader.model.local;

import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookRecordBean;
import com.book.ireader.model.bean.CollBookBean;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018/7/15
 * Time: 22:19
 */
public interface IBookDao {

    //获取阅读记录
    public BookRecordBean getBookRecord(String bookId);

    public List<BookChapterBean> getBookChapterBeans(String bookId);

    //保存章节
    public void saveBookChapters(List<BookChapterBean> bookChapterBeans);

    public void saveBookRecord(BookRecordBean bookRecordBean);

    public void deleteBookChapter(String bookId);
}
