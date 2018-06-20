package com.book.ireader.event;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-19
 * Time: 下午6:16
 */
public class BookShelfRefreshEvent {
    public String _id;
    public int position = -1;
    public int type;
    public static int EVENT_TYPE_ADD = 0x1;
    public static int EVENT_TYPE_DELETE = 0x1;
    public static int EVENT_TYPE_UPDATE = 0x1;

    public BookShelfRefreshEvent setId(String id) {
        this._id = id;
        return this;
    }

    public BookShelfRefreshEvent setPostion(int position) {
        this.position = position;
        return this;
    }

    public BookShelfRefreshEvent setType(int type) {
        this.type = type;
        return this;
    }
}
