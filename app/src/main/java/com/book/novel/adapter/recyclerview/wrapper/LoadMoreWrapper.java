package com.book.novel.adapter.recyclerview.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.book.novel.adapter.recyclerview.base.ViewHolder;
import com.book.novel.adapter.recyclerview.utils.WrapperUtils;

/**
 * Created by zhy on 16/6/23.
 */
public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_NONE = Integer.MAX_VALUE - 1;
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    public static final int ITEM_TYPE_NO_MORE = Integer.MAX_VALUE - 3;
    public static final int ITEM_TYPE_FAILED = Integer.MAX_VALUE - 4;

    private RecyclerView.Adapter mInnerAdapter;
    private int mLoadMoreLayoutId;
    private View mLoadMoreView;
    private int mNoMoreLayoutId;
    private View mNoMoreView;
    private int mFailedLayoutId;
    private View mFailedView;
    private int mCurrentItemType = ITEM_TYPE_NONE;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasFooterView() {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0
                || mNoMoreView != null || mNoMoreLayoutId != 0
                || mFailedView != null || mFailedLayoutId != 0;
    }


    private boolean isShowFooterView(int position) {
        return hasFooterView() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowFooterView(position)) {
            return mCurrentItemType;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
            }
            return holder;
        } else if (viewType == ITEM_TYPE_FAILED) {
            ViewHolder holder;
            if (mFailedView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mFailedView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mFailedLayoutId);
            }
            return holder;
        } else if (viewType == ITEM_TYPE_NO_MORE) {
            ViewHolder holder;
            if (mNoMoreView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mNoMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mNoMoreLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowFooterView(position)) {
            if (mOnLoadFooterListener != null) {
                mOnLoadFooterListener.onLoadFooterRequested(mCurrentItemType);
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowFooterView(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowFooterView(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        if (mCurrentItemType == ITEM_TYPE_NONE) {
            return 0;
        }
        return mInnerAdapter.getItemCount() + (hasFooterView() ? 1 : 0);
    }


    public interface OnLoadFooterListener {
        void onLoadFooterRequested(int itemType);
    }

    private OnLoadFooterListener mOnLoadFooterListener;

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadFooterListener loadFooterListener) {
        if (loadFooterListener != null) {
            mOnLoadFooterListener = loadFooterListener;
        }
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    public void setNoMoreLayoutId(int mNoMoreLayoutId) {
        this.mNoMoreLayoutId = mNoMoreLayoutId;
    }

    public void setNoMoreView(View mNoMoreView) {
        this.mNoMoreView = mNoMoreView;
    }

    public void setFailedLayoutId(int mFailedLayoutId) {
        this.mFailedLayoutId = mFailedLayoutId;
    }

    public void setFailedView(View mFailedView) {
        this.mFailedView = mFailedView;
    }

    public void setCurrentItemType(int mCurrentItemType) {
        this.mCurrentItemType = mCurrentItemType;
    }

}
