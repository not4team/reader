package com.book.novel.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018/6/13
 * Time: 23:09
 */
public class RecyclerViewBehavior extends HeaderScrollingViewBehavior<RecyclerView> {
    private String TAG = "RecyclerViewBehavior";

    public RecyclerViewBehavior() {
    }

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    ConstraintLayout findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof ConstraintLayout) {
                return (ConstraintLayout) view;
            }
        }
        return null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return dependency instanceof ConstraintLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
        if (behavior instanceof ConstraintLayoutBehavior) {
            // Offset the child, pinning it to the bottom the header-dependency, maintaining
            // any vertical gap and overlap
            final ConstraintLayoutBehavior clBehavior = (ConstraintLayoutBehavior) behavior;
            ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop())
                    + clBehavior.mOffsetDelta
                    + getVerticalLayoutGap()
                    - getOverlapPixelsForOffset(dependency));
        }
    }
}
