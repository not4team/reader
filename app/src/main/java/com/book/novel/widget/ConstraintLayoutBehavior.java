package com.book.novel.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-14
 * Time: 下午1:48
 */
public class ConstraintLayoutBehavior extends HeaderBehavior<ConstraintLayout> {
    private String TAG = "ConstraintLayoutBehavior";
    int mOffsetDelta;
    private ValueAnimator mOffsetAnimator;
    private WeakReference<View> mLastNestedScrollingChildRef;
    private RecyclerView mRecyclerView;
    private int mTotalScrollRange = INVALID_SCROLL_RANGE;
    private static final int INVALID_SCROLL_RANGE = -1;
    private static final int MAX_OFFSET_ANIMATION_DURATION = 600; // ms

    public ConstraintLayoutBehavior() {
    }

    public ConstraintLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ConstraintLayout child, View dependency) {
        if (dependency instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) dependency;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean recyclerfling(int velocityX, int velocityY) {
        return mRecyclerView.fling(velocityX, velocityY);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, ConstraintLayout child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec,
                                  int heightUsed) {
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.height == CoordinatorLayout.LayoutParams.WRAP_CONTENT) {
            // If the view is set to wrap on it's height, CoordinatorLayout by default will
            // cap the view at the CoL's height. Since the MyConstraintLayout can scroll, this isn't
            // what we actually want, so we measure it ourselves with an unspecified spec to
            // allow the child to be larger than it's parent
            parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), heightUsed);
            return true;
        }

        // Let the parent handle it as normal
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
    }

    @Override
    boolean canDragView(ConstraintLayout view) {
        // Else we'll use the default behaviour of seeing if it can scroll down
        if (mLastNestedScrollingChildRef != null) {
            // If we have a reference to a scrolling view, check it
            final View scrollingView = mLastNestedScrollingChildRef.get();
            return scrollingView != null && scrollingView.isShown()
                    && !scrollingView.canScrollVertically(-1);
        } else {
            // Otherwise we assume that the scrolling view hasn't been scrolled and can drag.
            return true;
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout parent, @NonNull ConstraintLayout child, @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes, int type) {
        // Return true if we're nested scrolling vertically, and we have scrollable children
        // and the scrolling view is big enough to scroll
        final boolean started = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0
                && parent.getHeight() - directTargetChild.getHeight() <= child.getHeight();
        if (started && mOffsetAnimator != null) {
            // Cancel any offset animation
            mOffsetAnimator.cancel();
        }

        // A new nested scroll has started so clear out the previous ref
        mLastNestedScrollingChildRef = null;

        return started;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ConstraintLayout child, @NonNull View target, int type) {
        Log.e(TAG, "onStopNestedScroll");
        if (type == ViewCompat.TYPE_TOUCH) {
            // If we haven't been flung then let's see if the current view has been set to snap
//            snapToChildIfNeeded(coordinatorLayout, child);
        }

        // Keep a reference to the previous nested scrolling child
        mLastNestedScrollingChildRef = new WeakReference<>(target);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ConstraintLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed < 0) {
            // If the scrolling view is scrolling down but not consuming, it's probably be at
            // the top of it's content
            scroll(coordinatorLayout, child, dyUnconsumed,
                    -child.getMeasuredHeight(), 0);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ConstraintLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy != 0) {
            int min, max;
            boolean canScroll;
            if (dy < 0) {
                // We're scrolling down
                min = -child.getMeasuredHeight();
                max = 0;
                RecyclerView recyclerView = (RecyclerView) target;
                if (recyclerView.computeVerticalScrollOffset() > 0) {
                    canScroll = false;
                } else {
                    canScroll = true;
                }
            } else {
                // We're scrolling up
                min = -child.getMeasuredHeight();
                max = 0;
                canScroll = true;
            }
            if (min != max && canScroll) {
                consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
            }
        }
    }

    private int getChildIndexOnOffset(ConstraintLayout cl, final int offset) {
        for (int i = 0, count = cl.getChildCount(); i < count; i++) {
            View child = cl.getChildAt(i);
            if (child.getTop() <= -offset && child.getBottom() >= -offset) {
                return i;
            }
        }
        return -1;
    }


    @Override
    void onFlingFinished(CoordinatorLayout parent, ConstraintLayout layout) {
        // At the end of a manual fling, check to see if we need to snap to the edge-child
//        snapToChildIfNeeded(parent, layout);
    }

    @Override
    int getMaxDragOffset(ConstraintLayout view) {
        return -view.getMeasuredHeight();
    }

    @Override
    int getScrollRangeForDragFling(ConstraintLayout view) {
        return view.getMeasuredHeight();
    }

    private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, ConstraintLayout cl) {
        final int offset = getTopBottomOffsetForScrollingSibling();
        final int offsetChildIndex = getChildIndexOnOffset(cl, offset);
        if (offsetChildIndex >= 0) {
            final View offsetChild = cl.getChildAt(offsetChildIndex);
            // We're set the snap, so animate the offset to the nearest edge
            int snapTop = -offsetChild.getTop();
            int snapBottom = -offsetChild.getBottom();

            final int newOffset = offset < (snapBottom + snapTop) / 2
                    ? snapBottom
                    : snapTop;
            animateOffsetTo(coordinatorLayout, cl,
                    MathUtils.clamp(newOffset, -cl.getMeasuredHeight(), 0), 0);
        }
    }

    private void animateOffsetTo(final CoordinatorLayout coordinatorLayout,
                                 final ConstraintLayout child, final int offset, float velocity) {
        final int distance = Math.abs(getTopBottomOffsetForScrollingSibling() - offset);

        final int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 3 * Math.round(1000 * (distance / velocity));
        } else {
            final float distanceRatio = (float) distance / child.getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }

        animateOffsetWithDuration(coordinatorLayout, child, offset, duration);
    }

    private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout,
                                           final ConstraintLayout child, final int offset, final int duration) {
        final int currentOffset = getTopBottomOffsetForScrollingSibling();
        if (currentOffset == offset) {
            if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
                mOffsetAnimator.cancel();
            }
            return;
        }

        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setHeaderTopBottomOffset(coordinatorLayout, child,
                            (int) animation.getAnimatedValue());
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }

        mOffsetAnimator.setDuration(Math.min(duration, MAX_OFFSET_ANIMATION_DURATION));
        mOffsetAnimator.setIntValues(currentOffset, offset);
        mOffsetAnimator.start();
    }

}
