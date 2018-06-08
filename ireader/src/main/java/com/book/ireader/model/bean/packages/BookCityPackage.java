package com.book.ireader.model.bean.packages;

import com.book.ireader.model.bean.BillBookBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-06
 * Time: 下午2:40
 */
public class BookCityPackage {
    //热门小说推荐
    private List<BillBookBean> hotBooks = new ArrayList<>();
    //新书抢鲜
    private List<BillBookBean> newBooks = new ArrayList<>();
    //畅销完本
    private List<BillBookBean> finishedBooks = new ArrayList<>();

    public List<BillBookBean> getHotBooks() {
        return hotBooks;
    }

    public List<BillBookBean> getNewBooks() {
        return newBooks;
    }

    public List<BillBookBean> getFinishedBooks() {
        return finishedBooks;
    }

}
