package com.book.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

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

    @Generated(hash = 1087685592)
    public SearchHistoryBean(String content) {
        this.content = content;
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
}
