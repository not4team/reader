package com.book.ireader.model.local;

import com.book.ireader.model.bean.AuthorBean;
import com.book.ireader.model.bean.BookCommentBean;
import com.book.ireader.model.bean.BookHelpfulBean;
import com.book.ireader.model.bean.BookHelpsBean;
import com.book.ireader.model.bean.BookReviewBean;
import com.book.ireader.model.bean.DownloadTaskBean;
import com.book.ireader.model.bean.ReviewBookBean;
import com.book.ireader.model.bean.packages.BillboardPackage;
import com.book.ireader.model.bean.packages.BookSortPackage;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by newbiechen on 17-4-28.
 */

public interface GetDbHelper {
    Single<List<BookCommentBean>> getBookComments(String block, String sort, int start, int limited, String distillate);

    Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limited, String distillate);

    Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate);

    BookSortPackage getBookSortPackage();

    BillboardPackage getBillboardPackage();

    AuthorBean getAuthor(String id);

    ReviewBookBean getReviewBook(String id);

    BookHelpfulBean getBookHelpful(String id);

    /******************************/
    List<DownloadTaskBean> getDownloadTaskList();
}
