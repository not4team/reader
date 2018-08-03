package com.book.ireader.model.bean;

/**
 * Created with author.
 * Description:
 * Date: 2018-08-03
 * Time: 下午4:17
 */
public interface Source {
    public Class<?> getSourceClass();

    public String getSourceName();

    public String getSourceBaseUrl();

    public String getSourceSearchUrl();

    public String getSourceEncode();
}
