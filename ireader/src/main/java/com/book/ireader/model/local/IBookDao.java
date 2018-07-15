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
    //获取收藏书籍
    public CollBookBean getCollBook(String bookId);

    //获取排序后的收藏书籍
    public List<CollBookBean> getOrderBooks();

    //获取阅读记录
    public BookRecordBean getBookRecord(String bookId);

    public List<BookChapterBean> getBookChapterBeans(String bookId);

    //交换书架顺序
    public void swapCollBookOrder(CollBookBean from, CollBookBean to);

    //收藏书籍
    public void saveCollBookWithAsync(CollBookBean bean);

    //保存章节
    public void saveBookChapters(List<BookChapterBean> bookChapterBeans);

    public void saveBookRecord(BookRecordBean bookRecordBean);

    public void deleteCollBook(String bookId);

    public void deleteBookChapter(String bookId);
}
