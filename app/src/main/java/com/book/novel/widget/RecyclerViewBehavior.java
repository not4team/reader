package com.book.novel.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created with author.
 * Description:
 * Date: 2018/6/13
 * Time: 23:09
 */
public class RecyclerViewBehavior<V extends RecyclerView> extends CoordinatorLayout.Behavior<V> {
    private String TAG = "RecyclerViewBehavior";
    final Rect mTempRect1 = new Rect();
    final Rect mTempRect2 = new Rect();

    private int mVerticalLayoutGap = 0;
    private int mOverlayTop;
    private View header;
    private int dx, dy;

    public RecyclerViewBehavior() {
        super();
    }

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        Log.e(TAG, "layoutDependsOn");
        return dependency instanceof ConstraintLayout;
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            final List<View> dependencies = parent.getDependencies(child);
            final View header = findFirstDependency(dependencies);
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header)
                        && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    ViewCompat.setFitsSystemWindows(child, true);

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout();
                        return true;
                    }
                }

                int availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.getHeight();
                }

                final int height = availableHeight - header.getMeasuredHeight()
                        + getScrollRange(header);
                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                        childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                                ? View.MeasureSpec.EXACTLY
                                : View.MeasureSpec.AT_MOST);

                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed);

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        final List<View> dependencies = parent.getDependencies(child);
        header = findFirstDependency(dependencies);

        if (header != null) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            final Rect available = mTempRect1;
            available.set(parent.getPaddingLeft() + lp.leftMargin,
                    header.getBottom() + lp.topMargin,
                    parent.getWidth() - parent.getPaddingRight() - lp.rightMargin,
                    parent.getHeight() + header.getBottom()
                            - parent.getPaddingBottom() - lp.bottomMargin);
            final Rect out = mTempRect2;
            GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
                    child.getMeasuredHeight(), available, out, layoutDirection);

            final int overlap = getOverlapPixelsForOffset(header);
            child.layout(out.left, out.top - overlap, out.right, out.bottom - overlap);
            mVerticalLayoutGap = out.top - header.getBottom();
        } else {
            // If we don't have a dependency, let super handle it
            mVerticalLayoutGap = 0;
            return super.onLayoutChild(parent, child, layoutDirection);
        }
        return true;
    }

    ConstraintLayout findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof ConstraintLayout) {
                return (ConstraintLayout) view;
            }
        }
        return null;
    }

    private int resolveGravity(int gravity) {
        return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
    }

    final int getOverlapPixelsForOffset(final View header) {
        return mOverlayTop == 0 ? 0 : MathUtils.clamp(
                (int) (getOverlapRatioForOffset(header) * mOverlayTop), 0, mOverlayTop);
    }

    float getOverlapRatioForOffset(final View header) {
        return 1f;
    }

    int getScrollRange(View v) {
        return v.getMeasuredHeight();
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        Log.e(TAG, "onDependentViewChanged");
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        Log.e(TAG, "onStartNestedScroll");
        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        Log.e(TAG, "onNestedScrollAccepted");
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        Log.e(TAG, "onStopNestedScroll");
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.e(TAG, "onNestedScroll dxConsumed:" + dxConsumed + ",dyConsumed" + dyConsumed);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        header.scrollTo(dx + dxConsumed, dy - dyConsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.e(TAG, "onNestedPreScroll");
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling");
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
