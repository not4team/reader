package com.book.ireader.model.bean.packages;

import com.book.ireader.model.bean.BillBookBean;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018/6/18
 * Time: 19:22
 */
public class RankCategoryPackage {
    private int total;
    private int isLast;
    private int pageNum;
    private List<BillBookBean> recoders;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIsLast() {
        return isLast;
    }

    public void setIsLast(int isLast) {
        this.isLast = isLast;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public List<BillBookBean> getRecoders() {
        return recoders;
    }

    public void setRecoders(List<BillBookBean> recoders) {
        this.recoders = recoders;
    }
}
