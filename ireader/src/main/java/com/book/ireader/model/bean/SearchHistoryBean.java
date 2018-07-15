package com.book.ireader.model.bean;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-12
 * Time: 下午1:34
 */
public class SearchHistoryBean {
    private String content;
    private long searchTime;

    public SearchHistoryBean(String content, long searchTime) {
        this.content = content;
        this.searchTime = searchTime;
    }

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
