package com.book.novel.widget

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created with author.
 * Description:
 * Date: 2018/6/20
 * Time: 22:30
 */
class BookShelfSwipeRefreshLayout : SwipeRefreshLayout {
    val TAG = "BookShelfSwipeRefresh"
    var mGestureDetector: GestureDetectorCompat? = null
    private var mTouchHelperGestureListener: TouchHelperGestureListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mTouchHelperGestureListener = TouchHelperGestureListener()
        mGestureDetector = GestureDetectorCompat(context,
                mTouchHelperGestureListener)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        mGestureDetector!!.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    inner class TouchHelperGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return super.onDown(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
        }
    }
}