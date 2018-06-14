package com.book.novel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Behavior which should be used by {@link View}s which can scroll vertically and support
 * nested scrolling to automatically scroll any {@link MyConstraintLayout} siblings.
 */
public class ScrollingViewBehavior extends HeaderScrollingViewBehavior<View> {
    private String TAG = "ScrollingViewBehavior";

    public ScrollingViewBehavior() {
    }

    public ScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                android.support.design.R.styleable.ScrollingViewBehavior_Layout);
        setOverlayTop(a.getDimensionPixelSize(
                android.support.design.R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // We depend on any MyConstraintLayouts
        return dependency instanceof MyConstraintLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                          View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    @Override
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout parent, View child,
                                                   Rect rectangle, boolean immediate) {
        Log.e(TAG, "onRequestChildRectangleOnScreen");
        final MyConstraintLayout header = findFirstDependency(parent.getDependencies(child));
        if (header != null) {
            // Offset the rect by the child's left/top
            rectangle.offset(child.getLeft(), child.getTop());

            final Rect parentRect = mTempRect1;
            parentRect.set(0, 0, parent.getWidth(), parent.getHeight());

            if (!parentRect.contains(rectangle)) {
                // If the rectangle can not be fully seen the visible bounds, collapse
                // the MyConstraintLayout
                header.setExpanded(false, !immediate);
                return true;
            }
        }
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        Log.e(TAG, "offsetChildAsNeeded");
        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
        if (behavior instanceof Behavior) {
            // Offset the child, pinning it to the bottom the header-dependency, maintaining
            // any vertical gap and overlap
            final Behavior ablBehavior = (Behavior) behavior;
            ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop())
                    + ablBehavior.mOffsetDelta
                    + getVerticalLayoutGap()
                    - getOverlapPixelsForOffset(dependency));
        }
    }

    @Override
    float getOverlapRatioForOffset(final View header) {
        if (header instanceof MyConstraintLayout) {
            final MyConstraintLayout abl = (MyConstraintLayout) header;
            final int totalScrollRange = abl.getTotalScrollRange();
            final int preScrollDown = abl.getDownNestedPreScrollRange();
            final int offset = getMyConstraintLayoutOffset(abl);

            if (preScrollDown != 0 && (totalScrollRange + offset) <= preScrollDown) {
                // If we're in a pre-scroll down. Don't use the offset at all.
                return 0;
            } else {
                final int availScrollRange = totalScrollRange - preScrollDown;
                if (availScrollRange != 0) {
                    // Else we'll use a interpolated ratio of the overlap, depending on offset
                    return 1f + (offset / (float) availScrollRange);
                }
            }
        }
        return 0f;
    }

    private static int getMyConstraintLayoutOffset(MyConstraintLayout abl) {
        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) abl.getLayoutParams()).getBehavior();
        if (behavior instanceof Behavior) {
            return ((Behavior) behavior).getTopBottomOffsetForScrollingSibling();
        }
        return 0;
    }

    @Override
    MyConstraintLayout findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof MyConstraintLayout) {
                return (MyConstraintLayout) view;
            }
        }
        return null;
    }

    @Override
    int getScrollRange(View v) {
        if (v instanceof MyConstraintLayout) {
            return ((MyConstraintLayout) v).getTotalScrollRange();
        } else {
            return super.getScrollRange(v);
        }
    }
}