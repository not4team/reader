package com.book.novel.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lereader.novel.R;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-14
 * Time: 下午1:13
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    private String TAG = "MySwipeRefreshLayout";

    private ConstraintLayout mConstraintLayout;

    public MySwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mConstraintLayout = findViewById(R.id.bookdetail_cl_content);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = super.onInterceptTouchEvent(ev);
        return intercept && mConstraintLayout.getTop() >= 0;
    }

    @Override
    public boolean canChildScrollUp() {
        return mConstraintLayout.getTop() < 0;
    }
}
