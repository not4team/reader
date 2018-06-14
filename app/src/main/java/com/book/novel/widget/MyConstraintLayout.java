package com.book.novel.widget;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-14
 * Time: 上午10:50
 */
public class MyConstraintLayout extends ConstraintLayout {
    private WindowInsetsCompat mLastInsets;
    private static final int INVALID_SCROLL_RANGE = -1;

    private int mTotalScrollRange = INVALID_SCROLL_RANGE;
    private int mDownPreScrollRange = INVALID_SCROLL_RANGE;
    private int mDownScrollRange = INVALID_SCROLL_RANGE;

    /**
     * Internal flags which allows quick checking features
     */
    public static final int SCROLL_FLAG_SCROLL = 0x1;
    public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 0x2;
    public static final int SCROLL_FLAG_ENTER_ALWAYS = 0x4;
    public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 0x8;
    public static final int SCROLL_FLAG_SNAP = 0x10;
    static final int FLAG_QUICK_RETURN = SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS;
    static final int FLAG_SNAP = SCROLL_FLAG_SCROLL | SCROLL_FLAG_SNAP;
    static final int COLLAPSIBLE_FLAGS = SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            | SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED;

    int mScrollFlags = SCROLL_FLAG_SCROLL;
    static final int PENDING_ACTION_NONE = 0x0;
    static final int PENDING_ACTION_EXPANDED = 0x1;
    static final int PENDING_ACTION_COLLAPSED = 0x2;
    static final int PENDING_ACTION_ANIMATE_ENABLED = 0x4;
    static final int PENDING_ACTION_FORCE = 0x8;
    private boolean mHaveChildWithInterpolator;
    private List<OnOffsetChangedListener> mListeners;
    private int mPendingAction = PENDING_ACTION_NONE;
    Interpolator mScrollInterpolator;
    private boolean mCollapsible;
    private boolean mCollapsed;

    public interface OnOffsetChangedListener {
        /**
         * Called when the {@link MyConstraintLayout}'s layout offset has been changed. This allows
         * child views to implement custom behavior based on the offset (for instance pinning a
         * view at a certain y value).
         *
         * @param constraintLayout the {@link MyConstraintLayout} which offset has changed
         * @param verticalOffset   the vertical offset for the parent {@link MyConstraintLayout}, in px
         */
        void onOffsetChanged(MyConstraintLayout constraintLayout, int verticalOffset);
    }

    public MyConstraintLayout(Context context) {
        super(context);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int getPendingAction() {
        return mPendingAction;
    }

    void resetPendingAction() {
        mPendingAction = PENDING_ACTION_NONE;
    }

    private void invalidateScrollRanges() {
        // Invalidate the scroll ranges
        mTotalScrollRange = INVALID_SCROLL_RANGE;
        mDownPreScrollRange = INVALID_SCROLL_RANGE;
        mDownScrollRange = INVALID_SCROLL_RANGE;
    }

    /**
     * Returns the scroll range of all children.
     *
     * @return the scroll range in px
     */
    public final int getTotalScrollRange() {
        if (mTotalScrollRange != INVALID_SCROLL_RANGE) {
            return mTotalScrollRange;
        }
        int range = getMeasuredHeight();
        return mTotalScrollRange = Math.max(0, range - getTopInset());
    }

    @VisibleForTesting
    final int getTopInset() {
        return mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
    }

    boolean hasScrollableChildren() {
        return getTotalScrollRange() != 0;
    }

    /**
     * Return the scroll range when scrolling down from a nested pre-scroll.
     */
    int getDownNestedPreScrollRange() {
        if (mDownPreScrollRange != INVALID_SCROLL_RANGE) {
            // If we already have a valid value, return it
            return mDownPreScrollRange;
        }

        int range = getMeasuredHeight();
        return mDownPreScrollRange = Math.max(0, range);
    }

    /**
     * Return the scroll range when scrolling up from a nested pre-scroll.
     */
    int getUpNestedPreScrollRange() {
        return getTotalScrollRange();
    }

    /**
     * Return the scroll range when scrolling down from a nested scroll.
     */
    int getDownNestedScrollRange() {
        if (mDownScrollRange != INVALID_SCROLL_RANGE) {
            // If we already have a valid value, return it
            return mDownScrollRange;
        }

        int range = getMeasuredHeight();
        return mDownScrollRange = Math.max(0, range);
    }

    void dispatchOffsetUpdates(int offset) {
        // Iterate backwards through the list so that most recently added listeners
        // get the first chance to decide
        if (mListeners != null) {
            for (int i = 0, z = mListeners.size(); i < z; i++) {
                final OnOffsetChangedListener listener = mListeners.get(i);
                if (listener != null) {
                    listener.onOffsetChanged(this, offset);
                }
            }
        }
    }

    private void setExpanded(boolean expanded, boolean animate, boolean force) {
        mPendingAction = (expanded ? PENDING_ACTION_EXPANDED : PENDING_ACTION_COLLAPSED)
                | (animate ? PENDING_ACTION_ANIMATE_ENABLED : 0)
                | (force ? PENDING_ACTION_FORCE : 0);
        requestLayout();
    }

    public void setExpanded(boolean expanded, boolean animate) {
        setExpanded(expanded, animate, true);
    }

    boolean hasChildWithInterpolator() {
        return mHaveChildWithInterpolator;
    }

    public Interpolator getScrollInterpolator() {
        return mScrollInterpolator;
    }

    boolean setCollapsedState(boolean collapsed) {
        if (mCollapsed != collapsed) {
            mCollapsed = collapsed;
            refreshDrawableState();
            return true;
        }
        return false;
    }
}
