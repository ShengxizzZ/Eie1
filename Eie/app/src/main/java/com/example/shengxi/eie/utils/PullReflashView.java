package com.example.shengxi.eie.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.shengxi.eie.R;

/**
 * swipereflashview to make loadmore data
 * Created by ShengXi on 2018-05-04.
 */

public class PullReflashView extends SwipeRefreshLayout {

    private View root;
    private int mScaledTouchSlop;

    private RecyclerView mRecyclerView;
    private boolean isLoading;
    private int mItemCount;

    public PullReflashView(Context context) {
        super(context);
    }

    public PullReflashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        root = View.inflate(context, R.layout.view_footer, null);
        //表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mRecyclerView == null) {
            if (getChildCount() > 0) {
                if (getChildAt(0) instanceof RecyclerView) {
                    mRecyclerView = (RecyclerView) getChildAt(0);
                    setRecyclerViewOnScroll();
                }
            }
        }
    }


    private float mDownY;
    private float mUpY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //移动的起点

                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (canLaod()) {
                    loadData();
                }
            case MotionEvent.ACTION_UP:
                mUpY = ev.getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //移动过程中判断能加载？
                if (canLaod()) {
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        if (mListener != null) {
            setLoading(true);
            mListener.onLoadMore();
        }
    }

    public void setLoading(boolean b) {
        isLoading = b;
        if (isLoading) {
            mRecyclerView.addView(root, mRecyclerView.getAdapter().getItemCount());
        } else {
            mRecyclerView.removeView(root);
            mDownY = 0;
            mUpY = 0;
        }
    }

    /**
     * 判断是否满足上啦加载的条件
     *
     * @return ture or false
     */
    private boolean canLaod() {
        boolean condition = (mDownY - mUpY) >= mScaledTouchSlop;
        if (condition) {

        }
        boolean condition0 = false;
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            if (mItemCount > 0) {
                if (mRecyclerView.getAdapter().getItemCount() < mItemCount) {
                    //第一页未满禁止上啦
                    condition0 = false;
                } else {
                    condition0 = mRecyclerView.getVerticalScrollbarPosition() == (mRecyclerView.getAdapter().getItemCount() - 1);
                }
            } else {
                condition0 = mRecyclerView.getVerticalScrollbarPosition() == (mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }

        boolean condition1 = !isLoading;
        if (condition1) {

        }
        return condition && condition0 && condition1;
    }

    public void setItemCount(int count){
        this.mItemCount = count;
    }

    /**
     * 回调
     */

    private OnLoadMoreListener mListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }
}
