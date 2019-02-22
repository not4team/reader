package com.book.ireader.widget.page;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.book.ireader.utils.SystemBarUtils;

/**
 * Created with author.
 * Description:
 * Date: 2019-02-22
 * Time: 下午3:35
 */
public class FullRelativeLayout extends RelativeLayout {
    private final String TAG = "FullRelativeLayout";
    private boolean flag;

    public FullRelativeLayout(Context context) {
        super(context);
    }

    public FullRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //限制PageView高度不变
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "widthSize:" + widthSize);
        Log.e(TAG, "heightSize:" + heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout");
//        if (flag) {
//            //限制PageView位置不变
//            PageView pageView = (PageView) getChildAt(0);
//            pageView.layout(l, t + SystemBarUtils.getStatusBarHeight(getContext()), r, b);
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged h:" + h + ",oldh:" + oldh);
        flag = h > oldh;
    }
}
