package com.book.ireader.model.gen;

import com.book.ireader.model.bean.AuthorBean;
import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.model.bean.BookCommentBean;
import com.book.ireader.model.bean.BookHelpfulBean;
import com.book.ireader.model.bean.BookHelpsBean;
import com.book.ireader.model.bean.BookRecordBean;
import com.book.ireader.model.bean.BookReviewBean;
import com.book.ireader.model.bean.CollBookBean;
import com.book.ireader.model.bean.DownloadTaskBean;
import com.book.ireader.model.bean.ReviewBookBean;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookReviewBeanDaoConfig;
    private final DaoConfig authorBeanDaoConfig;
    private final DaoConfig downloadTaskBeanDaoConfig;
    private final DaoConfig bookCommentBeanDaoConfig;
    private final DaoConfig bookChapterBeanDaoConfig;
    private final DaoConfig reviewBookBeanDaoConfig;
    private final DaoConfig bookHelpfulBeanDaoConfig;
    private final DaoConfig bookRecordBeanDaoConfig;
    private final DaoConfig bookHelpsBeanDaoConfig;
    private final DaoConfig collBookBeanDaoConfig;

    private final BookReviewBeanDao bookReviewBeanDao;
    private final AuthorBeanDao authorBeanDao;
    private final DownloadTaskBeanDao downloadTaskBeanDao;
    private final BookCommentBeanDao bookCommentBeanDao;
    private final BookChapterBeanDao bookChapterBeanDao;
    private final ReviewBookBeanDao reviewBookBeanDao;
    private final BookHelpfulBeanDao bookHelpfulBeanDao;
    private final BookRecordBeanDao bookRecordBeanDao;
    private final BookHelpsBeanDao bookHelpsBeanDao;
    private final CollBookBeanDao collBookBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookReviewBeanDaoConfig = daoConfigMap.get(BookReviewBeanDao.class).clone();
        bookReviewBeanDaoConfig.initIdentityScope(type);

        authorBeanDaoConfig = daoConfigMap.get(AuthorBeanDao.class).clone();
        authorBeanDaoConfig.initIdentityScope(type);

        downloadTaskBeanDaoConfig = daoConfigMap.get(DownloadTaskBeanDao.class).clone();
        downloadTaskBeanDaoConfig.initIdentityScope(type);

        bookCommentBeanDaoConfig = daoConfigMap.get(BookCommentBeanDao.class).clone();
        bookCommentBeanDaoConfig.initIdentityScope(type);

        bookChapterBeanDaoConfig = daoConfigMap.get(BookChapterBeanDao.class).clone();
        bookChapterBeanDaoConfig.initIdentityScope(type);

        reviewBookBeanDaoConfig = daoConfigMap.get(ReviewBookBeanDao.class).clone();
        reviewBookBeanDaoConfig.initIdentityScope(type);

        bookHelpfulBeanDaoConfig = daoConfigMap.get(BookHelpfulBeanDao.class).clone();
        bookHelpfulBeanDaoConfig.initIdentityScope(type);

        bookRecordBeanDaoConfig = daoConfigMap.get(BookRecordBeanDao.class).clone();
        bookRecordBeanDaoConfig.initIdentityScope(type);

        bookHelpsBeanDaoConfig = daoConfigMap.get(BookHelpsBeanDao.class).clone();
        bookHelpsBeanDaoConfig.initIdentityScope(type);

        collBookBeanDaoConfig = daoConfigMap.get(CollBookBeanDao.class).clone();
        collBookBeanDaoConfig.initIdentityScope(type);

        bookReviewBeanDao = new BookReviewBeanDao(bookReviewBeanDaoConfig, this);
        authorBeanDao = new AuthorBeanDao(authorBeanDaoConfig, this);
        downloadTaskBeanDao = new DownloadTaskBeanDao(downloadTaskBeanDaoConfig, this);
        bookCommentBeanDao = new BookCommentBeanDao(bookCommentBeanDaoConfig, this);
        bookChapterBeanDao = new BookChapterBeanDao(bookChapterBeanDaoConfig, this);
        reviewBookBeanDao = new ReviewBookBeanDao(reviewBookBeanDaoConfig, this);
        bookHelpfulBeanDao = new BookHelpfulBeanDao(bookHelpfulBeanDaoConfig, this);
        bookRecordBeanDao = new BookRecordBeanDao(bookRecordBeanDaoConfig, this);
        bookHelpsBeanDao = new BookHelpsBeanDao(bookHelpsBeanDaoConfig, this);
        collBookBeanDao = new CollBookBeanDao(collBookBeanDaoConfig, this);

        registerDao(BookReviewBean.class, bookReviewBeanDao);
        registerDao(AuthorBean.class, authorBeanDao);
        registerDao(DownloadTaskBean.class, downloadTaskBeanDao);
        registerDao(BookCommentBean.class, bookCommentBeanDao);
        registerDao(BookChapterBean.class, bookChapterBeanDao);
        registerDao(ReviewBookBean.class, reviewBookBeanDao);
        registerDao(BookHelpfulBean.class, bookHelpfulBeanDao);
        registerDao(BookRecordBean.class, bookRecordBeanDao);
        registerDao(BookHelpsBean.class, bookHelpsBeanDao);
        registerDao(CollBookBean.class, collBookBeanDao);
    }

    public void clear() {
        bookReviewBeanDaoConfig.clearIdentityScope();
        authorBeanDaoConfig.clearIdentityScope();
        downloadTaskBeanDaoConfig.clearIdentityScope();
        bookCommentBeanDaoConfig.clearIdentityScope();
        bookChapterBeanDaoConfig.clearIdentityScope();
        reviewBookBeanDaoConfig.clearIdentityScope();
        bookHelpfulBeanDaoConfig.clearIdentityScope();
        bookRecordBeanDaoConfig.clearIdentityScope();
        bookHelpsBeanDaoConfig.clearIdentityScope();
        collBookBeanDaoConfig.clearIdentityScope();
    }

    public BookReviewBeanDao getBookReviewBeanDao() {
        return bookReviewBeanDao;
    }

    public AuthorBeanDao getAuthorBeanDao() {
        return authorBeanDao;
    }

    public DownloadTaskBeanDao getDownloadTaskBeanDao() {
        return downloadTaskBeanDao;
    }

    public BookCommentBeanDao getBookCommentBeanDao() {
        return bookCommentBeanDao;
    }

    public BookChapterBeanDao getBookChapterBeanDao() {
        return bookChapterBeanDao;
    }

    public ReviewBookBeanDao getReviewBookBeanDao() {
        return reviewBookBeanDao;
    }

    public BookHelpfulBeanDao getBookHelpfulBeanDao() {
        return bookHelpfulBeanDao;
    }

    public BookRecordBeanDao getBookRecordBeanDao() {
        return bookRecordBeanDao;
    }

    public BookHelpsBeanDao getBookHelpsBeanDao() {
        return bookHelpsBeanDao;
    }

    public CollBookBeanDao getCollBookBeanDao() {
        return collBookBeanDao;
    }

}
