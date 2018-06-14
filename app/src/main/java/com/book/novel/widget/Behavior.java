package com.book.novel.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.book.novel.widget.MyConstraintLayout.FLAG_SNAP;
import static com.book.novel.widget.MyConstraintLayout.PENDING_ACTION_ANIMATE_ENABLED;
import static com.book.novel.widget.MyConstraintLayout.PENDING_ACTION_COLLAPSED;
import static com.book.novel.widget.MyConstraintLayout.PENDING_ACTION_EXPANDED;
import static com.book.novel.widget.MyConstraintLayout.PENDING_ACTION_FORCE;
import static com.book.novel.widget.MyConstraintLayout.PENDING_ACTION_NONE;

/**
 * The default {@link com.book.novel.widget.Behavior} for {@link MyConstraintLayout}. Implements the necessary nested
 * scroll handling with offsetting.
 */
public class Behavior extends HeaderBehavior<MyConstraintLayout> {
    private String TAG = "Behavior";
    private static final int MAX_OFFSET_ANIMATION_DURATION = 600; // ms
    private static final int INVALID_POSITION = -1;

    /**
     * Callback to allow control over any {@link MyConstraintLayout} dragging.
     */
    public static abstract class DragCallback {
        /**
         * Allows control over whether the given {@link MyConstraintLayout} can be dragged or not.
         * <p>
         * <p>Dragging is defined as a direct touch on the MyConstraintLayout with movement. This
         * call does not affect any nested scrolling.</p>
         *
         * @return true if we are in a position to scroll the MyConstraintLayout via a drag, false
         * if not.
         */
        public abstract boolean canDrag(@NonNull MyConstraintLayout MyConstraintLayout);
    }

    int mOffsetDelta;
    private ValueAnimator mOffsetAnimator;

    private int mOffsetToChildIndexOnLayout = INVALID_POSITION;
    private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
    private float mOffsetToChildIndexOnLayoutPerc;

    private WeakReference<View> mLastNestedScrollingChildRef;
    private Behavior.DragCallback mOnDragCallback;

    public Behavior() {

    }

    public Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, MyConstraintLayout child,
                                       View directTargetChild, View target, int nestedScrollAxes, int type) {
        // Return true if we're nested scrolling vertically, and we have scrollable children
        // and the scrolling view is big enough to scroll
        final boolean started = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0
                && child.hasScrollableChildren()
                && parent.getHeight() - directTargetChild.getHeight() <= child.getHeight();
        Log.e(TAG, "onStartNestedScroll started ");
        if (started && mOffsetAnimator != null) {
            // Cancel any offset animation
            mOffsetAnimator.cancel();
        }

        // A new nested scroll has started so clear out the previous ref
        mLastNestedScrollingChildRef = null;

        return started;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, MyConstraintLayout child,
                                  View target, int dx, int dy, int[] consumed, int type) {
        Log.e(TAG, "onNestedPreScroll");
        if (dy != 0) {
            int min, max;
            if (dy < 0) {
                // We're scrolling down
                min = -child.getTotalScrollRange();
                max = min + child.getDownNestedPreScrollRange();
            } else {
                // We're scrolling up
                min = -child.getUpNestedPreScrollRange();
                max = 0;
            }
            if (min != max) {
                consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
            }
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, MyConstraintLayout child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type) {
        Log.e(TAG, "onNestedScroll");
        if (dyUnconsumed < 0) {
            // If the scrolling view is scrolling down but not consuming, it's probably be at
            // the top of it's content
            scroll(coordinatorLayout, child, dyUnconsumed,
                    -child.getDownNestedScrollRange(), 0);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, MyConstraintLayout abl,
                                   View target, int type) {
        Log.e(TAG, "onStopNestedScroll");
        if (type == ViewCompat.TYPE_TOUCH) {
            // If we haven't been flung then let's see if the current view has been set to snap
            snapToChildIfNeeded(coordinatorLayout, abl);
        }

        // Keep a reference to the previous nested scrolling child
        mLastNestedScrollingChildRef = new WeakReference<>(target);
    }

    /**
     * Set a callback to control any {@link MyConstraintLayout} dragging.
     *
     * @param callback the callback to use, or {@code null} to use the default behavior.
     */
    public void setDragCallback(@Nullable Behavior.DragCallback callback) {
        mOnDragCallback = callback;
    }

    private void animateOffsetTo(final CoordinatorLayout coordinatorLayout,
                                 final MyConstraintLayout child, final int offset, float velocity) {
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
                                           final MyConstraintLayout child, final int offset, final int duration) {
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

    private int getChildIndexOnOffset(MyConstraintLayout abl, final int offset) {
        for (int i = 0, count = abl.getChildCount(); i < count; i++) {
            View child = abl.getChildAt(i);
            if (child.getTop() <= -offset && child.getBottom() >= -offset) {
                return i;
            }
        }
        return -1;
    }

    private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, MyConstraintLayout abl) {
        final int offset = getTopBottomOffsetForScrollingSibling();
        final int offsetChildIndex = getChildIndexOnOffset(abl, offset);
        if (offsetChildIndex >= 0) {
            final View offsetChild = abl.getChildAt(offsetChildIndex);
            final MyConstraintLayout.LayoutParams lp = (MyConstraintLayout.LayoutParams) offsetChild.getLayoutParams();
            final int flags = FLAG_SNAP;

            if ((flags & FLAG_SNAP) == FLAG_SNAP) {
                // We're set the snap, so animate the offset to the nearest edge
                int snapTop = -offsetChild.getTop();
                int snapBottom = -offsetChild.getBottom();

                if (offsetChildIndex == abl.getChildCount() - 1) {
                    // If this is the last child, we need to take the top inset into account
                    snapBottom += abl.getTopInset();
                }

                if (checkFlag(flags, MyConstraintLayout.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)) {
                    // If the view is set only exit until it is collapsed, we'll abide by that
                    snapBottom += ViewCompat.getMinimumHeight(offsetChild);
                } else if (checkFlag(flags, MyConstraintLayout.FLAG_QUICK_RETURN
                        | MyConstraintLayout.SCROLL_FLAG_ENTER_ALWAYS)) {
                    // If it's set to always enter collapsed, it actually has two states. We
                    // select the state and then snap within the state
                    final int seam = snapBottom + ViewCompat.getMinimumHeight(offsetChild);
                    if (offset < seam) {
                        snapTop = seam;
                    } else {
                        snapBottom = seam;
                    }
                }

                final int newOffset = offset < (snapBottom + snapTop) / 2
                        ? snapBottom
                        : snapTop;
                animateOffsetTo(coordinatorLayout, abl,
                        MathUtils.clamp(newOffset, -abl.getTotalScrollRange(), 0), 0);
            }
        }
    }

    private static boolean checkFlag(final int flags, final int check) {
        return (flags & check) == check;
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, MyConstraintLayout child,
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
    public boolean onLayoutChild(CoordinatorLayout parent, MyConstraintLayout abl,
                                 int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);

        // The priority for for actions here is (first which is true wins):
        // 1. forced pending actions
        // 2. offsets for restorations
        // 3. non-forced pending actions
        final int pendingAction = abl.getPendingAction();
        if (mOffsetToChildIndexOnLayout >= 0 && (pendingAction & PENDING_ACTION_FORCE) == 0) {
            View child = abl.getChildAt(mOffsetToChildIndexOnLayout);
            int offset = -child.getBottom();
            if (mOffsetToChildIndexOnLayoutIsMinHeight) {
                offset += ViewCompat.getMinimumHeight(child) + abl.getTopInset();
            } else {
                offset += Math.round(child.getHeight() * mOffsetToChildIndexOnLayoutPerc);
            }
            setHeaderTopBottomOffset(parent, abl, offset);
        } else if (pendingAction != PENDING_ACTION_NONE) {
            final boolean animate = (pendingAction & PENDING_ACTION_ANIMATE_ENABLED) != 0;
            if ((pendingAction & PENDING_ACTION_COLLAPSED) != 0) {
                final int offset = -abl.getUpNestedPreScrollRange();
                if (animate) {
                    animateOffsetTo(parent, abl, offset, 0);
                } else {
                    setHeaderTopBottomOffset(parent, abl, offset);
                }
            } else if ((pendingAction & PENDING_ACTION_EXPANDED) != 0) {
                if (animate) {
                    animateOffsetTo(parent, abl, 0, 0);
                } else {
                    setHeaderTopBottomOffset(parent, abl, 0);
                }
            }
        }

        // Finally reset any pending states
        abl.resetPendingAction();
        mOffsetToChildIndexOnLayout = INVALID_POSITION;

        // We may have changed size, so let's constrain the top and bottom offset correctly,
        // just in case we're out of the bounds
        setTopAndBottomOffset(
                MathUtils.clamp(getTopAndBottomOffset(), -abl.getTotalScrollRange(), 0));

        // Update the MyConstraintLayout's drawable state for any elevation changes.
        // This is needed so that the elevation is set in the first layout, so that
        // we don't get a visual elevation jump pre-N (due to the draw dispatch skip)
        updateMyConstraintLayoutDrawableState(parent, abl, getTopAndBottomOffset(), 0, true);

        // Make sure we dispatch the offset update
        abl.dispatchOffsetUpdates(getTopAndBottomOffset());

        return handled;
    }

    @Override
    boolean canDragView(MyConstraintLayout view) {
        if (mOnDragCallback != null) {
            // If there is a drag callback set, it's in control
            return mOnDragCallback.canDrag(view);
        }

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
    void onFlingFinished(CoordinatorLayout parent, MyConstraintLayout layout) {
        // At the end of a manual fling, check to see if we need to snap to the edge-child
        snapToChildIfNeeded(parent, layout);
    }

    @Override
    int getMaxDragOffset(MyConstraintLayout view) {
        return -view.getDownNestedScrollRange();
    }

    @Override
    int getScrollRangeForDragFling(MyConstraintLayout view) {
        return view.getTotalScrollRange();
    }

    @Override
    int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout,
                                 MyConstraintLayout MyConstraintLayout, int newOffset, int minOffset, int maxOffset) {
        final int curOffset = getTopBottomOffsetForScrollingSibling();
        int consumed = 0;

        if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
            // If we have some scrolling range, and we're currently within the min and max
            // offsets, calculate a new offset
            newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);
            if (curOffset != newOffset) {
                final int interpolatedOffset = MyConstraintLayout.hasChildWithInterpolator()
                        ? interpolateOffset(MyConstraintLayout, newOffset)
                        : newOffset;

                final boolean offsetChanged = setTopAndBottomOffset(interpolatedOffset);

                // Update how much dy we have consumed
                consumed = curOffset - newOffset;
                // Update the stored sibling offset
                mOffsetDelta = newOffset - interpolatedOffset;

                if (!offsetChanged && MyConstraintLayout.hasChildWithInterpolator()) {
                    // If the offset hasn't changed and we're using an interpolated scroll
                    // then we need to keep any dependent views updated. CoL will do this for
                    // us when we move, but we need to do it manually when we don't (as an
                    // interpolated scroll may finish early).
                    coordinatorLayout.dispatchDependentViewsChanged(MyConstraintLayout);
                }

                // Dispatch the updates to any listeners
                MyConstraintLayout.dispatchOffsetUpdates(getTopAndBottomOffset());

                // Update the MyConstraintLayout's drawable state (for any elevation changes)
                updateMyConstraintLayoutDrawableState(coordinatorLayout, MyConstraintLayout, newOffset,
                        newOffset < curOffset ? -1 : 1, false);
            }
        } else {
            // Reset the offset delta
            mOffsetDelta = 0;
        }

        return consumed;
    }

    @VisibleForTesting
    boolean isOffsetAnimatorRunning() {
        return mOffsetAnimator != null && mOffsetAnimator.isRunning();
    }

    private int interpolateOffset(MyConstraintLayout layout, final int offset) {
        final int absOffset = Math.abs(offset);

        for (int i = 0, z = layout.getChildCount(); i < z; i++) {
            final View child = layout.getChildAt(i);
            final MyConstraintLayout.LayoutParams childLp = (MyConstraintLayout.LayoutParams) child.getLayoutParams();
            final Interpolator interpolator = null;

            if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
                if (interpolator != null) {
                    int childScrollableHeight = 0;
                    final int flags = MyConstraintLayout.SCROLL_FLAG_SCROLL;
                    if ((flags & MyConstraintLayout.SCROLL_FLAG_SCROLL) != 0) {
                        // We're set to scroll so add the child's height plus margin
                        childScrollableHeight += child.getHeight() + childLp.topMargin
                                + childLp.bottomMargin;

                        if ((flags & MyConstraintLayout.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
                            // For a collapsing scroll, we to take the collapsed height
                            // into account.
                            childScrollableHeight -= ViewCompat.getMinimumHeight(child);
                        }
                    }

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        childScrollableHeight -= layout.getTopInset();
                    }

                    if (childScrollableHeight > 0) {
                        final int offsetForView = absOffset - child.getTop();
                        final int interpolatedDiff = Math.round(childScrollableHeight *
                                interpolator.getInterpolation(
                                        offsetForView / (float) childScrollableHeight));

                        return Integer.signum(offset) * (child.getTop() + interpolatedDiff);
                    }
                }

                // If we get to here then the view on the offset isn't suitable for interpolated
                // scrolling. So break out of the loop
                break;
            }
        }

        return offset;
    }

    private void updateMyConstraintLayoutDrawableState(final CoordinatorLayout parent,
                                                       final MyConstraintLayout layout, final int offset, final int direction,
                                                       final boolean forceJump) {
        final View child = getAppBarChildOnOffset(layout, offset);
        if (child != null) {
            final MyConstraintLayout.LayoutParams childLp = (MyConstraintLayout.LayoutParams) child.getLayoutParams();
            final int flags = MyConstraintLayout.SCROLL_FLAG_SCROLL;
            boolean collapsed = false;

            if ((flags & MyConstraintLayout.SCROLL_FLAG_SCROLL) != 0) {
                final int minHeight = ViewCompat.getMinimumHeight(child);

                if (direction > 0 && (flags & (MyConstraintLayout.SCROLL_FLAG_ENTER_ALWAYS
                        | MyConstraintLayout.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED)) != 0) {
                    // We're set to enter always collapsed so we are only collapsed when
                    // being scrolled down, and in a collapsed offset
                    collapsed = -offset >= child.getBottom() - minHeight - layout.getTopInset();
                } else if ((flags & MyConstraintLayout.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
                    // We're set to exit until collapsed, so any offset which results in
                    // the minimum height (or less) being shown is collapsed
                    collapsed = -offset >= child.getBottom() - minHeight - layout.getTopInset();
                }
            }

            final boolean changed = layout.setCollapsedState(collapsed);

            if (Build.VERSION.SDK_INT >= 11 && (forceJump
                    || (changed && shouldJumpElevationState(parent, layout)))) {
                // If the collapsed state changed, we may need to
                // jump to the current state if we have an overlapping view
                layout.jumpDrawablesToCurrentState();
            }
        }
    }

    private boolean shouldJumpElevationState(CoordinatorLayout parent, MyConstraintLayout layout) {
        // We should jump the elevated state if we have a dependent scrolling view which has
        // an overlapping top (i.e. overlaps us)
        final List<View> dependencies = parent.getDependents(layout);
        for (int i = 0, size = dependencies.size(); i < size; i++) {
            final View dependency = dependencies.get(i);
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
            final CoordinatorLayout.Behavior behavior = lp.getBehavior();

            if (behavior instanceof ScrollingViewBehavior) {
                return ((ScrollingViewBehavior) behavior).getOverlayTop() != 0;
            }
        }
        return false;
    }

    private static View getAppBarChildOnOffset(final MyConstraintLayout layout, final int offset) {
        final int absOffset = Math.abs(offset);
        for (int i = 0, z = layout.getChildCount(); i < z; i++) {
            final View child = layout.getChildAt(i);
            if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    @Override
    int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset() + mOffsetDelta;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, MyConstraintLayout abl) {
        final Parcelable superState = super.onSaveInstanceState(parent, abl);
        final int offset = getTopAndBottomOffset();

        // Try and find the first visible child...
        for (int i = 0, count = abl.getChildCount(); i < count; i++) {
            View child = abl.getChildAt(i);
            final int visBottom = child.getBottom() + offset;

            if (child.getTop() + offset <= 0 && visBottom >= 0) {
                final Behavior.SavedState ss = new Behavior.SavedState(superState);
                ss.firstVisibleChildIndex = i;
                ss.firstVisibleChildAtMinimumHeight =
                        visBottom == (ViewCompat.getMinimumHeight(child));
                ss.firstVisibleChildPercentageShown = visBottom / (float) child.getHeight();
                return ss;
            }
        }

        // Else we'll just return the super state
        return superState;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, MyConstraintLayout MyConstraintLayout,
                                       Parcelable state) {
        if (state instanceof Behavior.SavedState) {
            final Behavior.SavedState ss = (Behavior.SavedState) state;
            super.onRestoreInstanceState(parent, MyConstraintLayout, ss.getSuperState());
            mOffsetToChildIndexOnLayout = ss.firstVisibleChildIndex;
            mOffsetToChildIndexOnLayoutPerc = ss.firstVisibleChildPercentageShown;
            mOffsetToChildIndexOnLayoutIsMinHeight = ss.firstVisibleChildAtMinimumHeight;
        } else {
            super.onRestoreInstanceState(parent, MyConstraintLayout, state);
            mOffsetToChildIndexOnLayout = INVALID_POSITION;
        }
    }

    protected static class SavedState extends AbsSavedState {
        int firstVisibleChildIndex;
        float firstVisibleChildPercentageShown;
        boolean firstVisibleChildAtMinimumHeight;

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            firstVisibleChildIndex = source.readInt();
            firstVisibleChildPercentageShown = source.readFloat();
            firstVisibleChildAtMinimumHeight = source.readByte() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(firstVisibleChildIndex);
            dest.writeFloat(firstVisibleChildPercentageShown);
            dest.writeByte((byte) (firstVisibleChildAtMinimumHeight ? 1 : 0));
        }

        public static final Creator<Behavior.SavedState> CREATOR = new ClassLoaderCreator<Behavior.SavedState>() {
            @Override
            public Behavior.SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new Behavior.SavedState(source, loader);
            }

            @Override
            public Behavior.SavedState createFromParcel(Parcel source) {
                return new Behavior.SavedState(source, null);
            }

            @Override
            public Behavior.SavedState[] newArray(int size) {
                return new Behavior.SavedState[size];
            }
        };
    }
}