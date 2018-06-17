package com.book.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-12
 * Time: 下午1:34
 */
@Entity
public class SearchHistoryBean {
    @Unique
    private String content;
    private long searchTime;

    @Generated(hash = 416821812)
    public SearchHistoryBean(String content, long searchTime) {
        this.content = content;
        this.searchTime = searchTime;
    }

    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }

    @Override
    public String toString() {
        return "SearchHistoryBean{" +
                "content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(int searchTime) {
        this.searchTime = searchTime;
    }

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }
}
