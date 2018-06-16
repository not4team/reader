package com.book.ireader.model.bean;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018/6/17
 * Time: 0:35
 */
public class RankTabBean {
    private String tab;
    private String rank;
    private String gender;
    private String catId;
    private List<BillBookBean> billBookBeans;

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public List<BillBookBean> getBillBookBeans() {
        return billBookBeans;
    }

    public void setBillBookBeans(List<BillBookBean> billBookBeans) {
        this.billBookBeans = billBookBeans;
    }
}
