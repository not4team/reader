package com.book.ireader.model.bean.packages;

import com.book.ireader.model.bean.BillBookBean;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午2:40
 */
public class BookCityPackage {
    //热门小说推荐
    private List<BillBookBean> hotBooks;
    //新书抢鲜
    private List<BillBookBean> newBooks;
    //畅销完本
    private List<BillBookBean> finishedBooks;

    public List<BillBookBean> getHotBooks() {
        return hotBooks;
    }

    public void setHotBooks(List<BillBookBean> hotBooks) {
        this.hotBooks = hotBooks;
    }

    public List<BillBookBean> getNewBooks() {
        return newBooks;
    }

    public void setNewBooks(List<BillBookBean> newBooks) {
        this.newBooks = newBooks;
    }

    public List<BillBookBean> getFinishedBooks() {
        return finishedBooks;
    }

    public void setFinishedBooks(List<BillBookBean> finishedBooks) {
        this.finishedBooks = finishedBooks;
    }
}
