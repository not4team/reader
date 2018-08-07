package com.book.ireader.presenter.contract;

import com.book.ireader.model.bean.BookChapterBean;
import com.book.ireader.ui.base.BaseContract;
import com.book.ireader.widget.page.TxtChapter;

import java.util.List;

/**
 * Created by newbiechen on 17-5-16.
 */

public interface ReadContract extends BaseContract {
    interface View extends BaseContract.BaseView {
        void showCategory(List<BookChapterBean> bookChapterList);

        void finishChapter();

        void errorChapter();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadCategory(String bookId, String bookLink) throws Exception;

        void loadChapter(String bookId, List<TxtChapter> bookChapterList) throws Exception;
    }
}
