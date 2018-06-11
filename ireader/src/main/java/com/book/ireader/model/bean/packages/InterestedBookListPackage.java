package com.book.ireader.model.bean.packages;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-11
 * Time: 下午6:59
 */
public class InterestedBookListPackage {
    private List<BookRecommendBean> books;

    public List<BookRecommendBean> getBooks() {
        return books;
    }

    public void setBooks(List<BookRecommendBean> books) {
        this.books = books;
    }

    public static class BookRecommendBean {
        /**
         * _id	"5948c17d031fdaa005680400"
         * title	"牧神记"
         * author	"宅猪"
         * site	"zhuishuvip"
         * cover	"/agent/http%3A%2F%2Fimg.…953606f4a3ad8c68.jpg%2F"
         * shortIntro	"大墟的祖训说，天黑，别出门。\r\n大墟残老村的老弱病残们从江边捡到了一个婴儿，取名秦牧，含辛茹苦将他养大。这一天夜幕降临，黑暗笼罩大墟，秦牧走出了家门……\r\n做个春风中荡漾的反派吧！\r\n瞎子对他说。\r\n秦牧的反派之路，正在崛起！"
         * lastChapter	"第751章 鬼脸面具"
         * retentionRatio	71.96
         * latelyFollower	89937
         * majorCate	"玄幻"
         * minorCate	"东方玄幻"
         * allowMonthly	false
         * isSerial	true
         */
        private String _id;
        private String title;
        private String author;
        private String site;
        private String cover;
        private String shortIntro;
        private String lastChapter;
        private float retentionRatio;
        private int latelyFollower;
        private String majorCate;
        private String minorCate;
        private boolean allowMonthly;
        private boolean isSerial;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getShortIntro() {
            return shortIntro;
        }

        public void setShortIntro(String shortIntro) {
            this.shortIntro = shortIntro;
        }

        public String getLastChapter() {
            return lastChapter;
        }

        public void setLastChapter(String lastChapter) {
            this.lastChapter = lastChapter;
        }

        public float getRetentionRatio() {
            return retentionRatio;
        }

        public void setRetentionRatio(float retentionRatio) {
            this.retentionRatio = retentionRatio;
        }

        public int getLatelyFollower() {
            return latelyFollower;
        }

        public void setLatelyFollower(int latelyFollower) {
            this.latelyFollower = latelyFollower;
        }

        public String getMajorCate() {
            return majorCate;
        }

        public void setMajorCate(String majorCate) {
            this.majorCate = majorCate;
        }

        public String getMinorCate() {
            return minorCate;
        }

        public void setMinorCate(String minorCate) {
            this.minorCate = minorCate;
        }

        public boolean isAllowMonthly() {
            return allowMonthly;
        }

        public void setAllowMonthly(boolean allowMonthly) {
            this.allowMonthly = allowMonthly;
        }

        public boolean isIsSerial() {
            return isSerial;
        }

        public void setIsSerial(boolean isSerial) {
            this.isSerial = isSerial;
        }
    }

}
