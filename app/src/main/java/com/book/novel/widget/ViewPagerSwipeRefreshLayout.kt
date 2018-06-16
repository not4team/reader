package com.book.novel.widget

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

/**
 * Created with author.
 * Description:
 * Date: 2018/6/17
 * Time: 3:08
 */
class ViewPagerSwipeRefreshLayout : SwipeRefreshLayout {
    private var mTouchSlop: Int = 0
    private var mPreX: Float = 0f
    private var mPreY: Float = 0f

    constructor(context: Context) : super(context) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val action = ev!!.action

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            return false; // Do not intercept touch event, let the child handle it
        }
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mPreX = ev.x
                mPreY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                var mDistanceX = Math.abs(ev.x - mPreX)
                var mDistanceY = Math.abs(ev.y - mPreY)
                if (mDistanceX > mTouchSlop && mDistanceX > mDistanceY) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}