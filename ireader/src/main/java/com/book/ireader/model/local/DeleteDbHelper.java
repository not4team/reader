package com.book.ireader.model.local;

import com.book.ireader.model.bean.AuthorBean;
import com.book.ireader.model.bean.BookCommentBean;
import com.book.ireader.model.bean.BookHelpfulBean;
import com.book.ireader.model.bean.BookHelpsBean;
import com.book.ireader.model.bean.BookReviewBean;
import com.book.ireader.model.bean.ReviewBookBean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-28.
 */

public interface DeleteDbHelper {
    void deleteBookComments(List<BookCommentBean> beans);

    void deleteBookReviews(List<BookReviewBean> beans);

    void deleteBookHelps(List<BookHelpsBean> beans);

    void deleteAuthors(List<AuthorBean> beans);

    void deleteBooks(List<ReviewBookBean> beans);

    void deleteBookHelpful(List<BookHelpfulBean> beans);

    void deleteAll();
}
