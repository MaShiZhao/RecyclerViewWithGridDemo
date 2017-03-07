package com.recyclerview.gridview.test.base.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.recyclerview.gridview.test.base.recyclerview.adapter.PullRefreshAdapter;
import com.recyclerview.gridview.test.base.recyclerview.adapter.PullRefreshDelegate;

/**
 * Created by MaShiZhao on 2016/12/30
 */
public class MyPullRecyclerView extends MyRecyclerView
{

    private MyPullSwipeRefresh myPullSwipeRefresh;
    private PullRefreshAdapter myAdapter;

    private Boolean isLoading;
    private int totalPage;

    public MyPullRecyclerView(Context context)
    {
        super(context);
    }

    public MyPullRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyPullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(Adapter adapter)
    {
        super.setAdapter(adapter);
        if (adapter instanceof PullRefreshAdapter)
        {
            this.myAdapter = (PullRefreshAdapter) adapter;
        }

    }

    @Override
    void initView(Context context, AttributeSet attrs)
    {
        super.initView(context, attrs);
        this.isLoading = false;
        this.totalPage = Integer.MAX_VALUE;
    }

    /**
     * 当前加载页数
     *
     * @param page      页数
     * @param dataCount 当前数据总数量
     */
    public void addMoreSuccess(int page, int dataCount)
    {
        isLoading = false;
        ((PullRefreshDelegate) ((PullRefreshAdapter) getAdapter()).getBaseDelegate()).setDataCount(dataCount);
        getAdapter().notifyDataSetChanged();
        myPullSwipeRefresh.setEnabled(true);
        if (page == this.totalPage)
        {
            ((PullRefreshAdapter) getAdapter()).setFooterCount(0);
        }
    }

    public void setTotalPage(int totalPage)
    {
        this.totalPage = totalPage;
    }

    // 加载更多的回调
    private OnAddMoreListener addMoreListener;
    private boolean isInTheBottom = false;
    /**
     * reachBottomRow = 1;(default)
     * mean : when the lastVisibleRow is lastRow , call the onReachBottom();
     * reachBottomRow = 2;
     * mean : when the lastVisibleRow is Penultimate Row , call the onReachBottom();
     * And so on
     */
    private int reachBottomRow = 2;

    public Boolean beginLoading(int pageIndex)
    {
        // 正在加载或者刷新的 不执行
        if (this.myPullSwipeRefresh.isRefreshing() || isLoading)
        {
            return false;
        }

        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null)
        { //it maybe unnecessary
            throw new RuntimeException("LayoutManager is null,Please check it!");
        }
        Adapter adapter = getAdapter();
        if (adapter == null)
        { //it maybe unnecessary
            throw new RuntimeException("Adapter is null,Please check it!");
        }
        boolean isReachBottom = false;
        //is GridLayoutManager
        if (layoutManager instanceof GridLayoutManager)
        {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int rowCount = adapter.getItemCount() / gridLayoutManager.getSpanCount();
            int lastVisibleRowPosition = gridLayoutManager.findLastVisibleItemPosition() / gridLayoutManager.getSpanCount();
            isReachBottom = (lastVisibleRowPosition >= rowCount - reachBottomRow);
        }
        //is LinearLayoutManager
        else if (layoutManager instanceof LinearLayoutManager)
        {
            int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            int rowCount = adapter.getItemCount();
            if (reachBottomRow > rowCount)
                reachBottomRow = 1;
            isReachBottom = (lastVisibleItemPosition >= rowCount - reachBottomRow);
        }
        //is StaggeredGridLayoutManager
        else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            int[] into = new int[spanCount];
            int[] eachSpanListVisibleItemPosition = staggeredGridLayoutManager.findLastVisibleItemPositions(into);
            for (int i = 0; i < spanCount; i++)
            {
                if (eachSpanListVisibleItemPosition[i] > adapter.getItemCount() - reachBottomRow * spanCount)
                {
                    isReachBottom = true;
                    break;
                }
            }
        }

        if (!isReachBottom)
        {
            isInTheBottom = false;
        } else if (!isInTheBottom)
        {
            isInTheBottom = true;
            myPullSwipeRefresh.setEnabled(false);
            isLoading = true;
            return true;
        }

//        L.d("是否到达底部" + isInTheBottom);
        return isInTheBottom;

    }

    @Override
    public void onScrolled(int dx, int dy)
    {
        super.onScrolled(dx, dy);
        if (addMoreListener != null)
        {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null)
            { //it maybe unnecessary
                throw new RuntimeException("LayoutManager is null,Please check it!");
            }
            Adapter adapter = getAdapter();
            if (adapter == null)
            { //it maybe unnecessary
                throw new RuntimeException("Adapter is null,Please check it!");
            }
            boolean isReachBottom = false;
            //is GridLayoutManager
            if (layoutManager instanceof GridLayoutManager)
            {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                int rowCount = adapter.getItemCount() / gridLayoutManager.getSpanCount();
                int lastVisibleRowPosition = gridLayoutManager.findLastVisibleItemPosition() / gridLayoutManager.getSpanCount();
                isReachBottom = (lastVisibleRowPosition >= rowCount - reachBottomRow);
            }
            //is LinearLayoutManager
            else if (layoutManager instanceof LinearLayoutManager)
            {
                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int rowCount = adapter.getItemCount();
                if (reachBottomRow > rowCount)
                    reachBottomRow = 1;
                isReachBottom = (lastVisibleItemPosition >= rowCount - reachBottomRow);
            }
            //is StaggeredGridLayoutManager
            else if (layoutManager instanceof StaggeredGridLayoutManager)
            {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int spanCount = staggeredGridLayoutManager.getSpanCount();
                int[] into = new int[spanCount];
                int[] eachSpanListVisibleItemPosition = staggeredGridLayoutManager.findLastVisibleItemPositions(into);
                for (int i = 0; i < spanCount; i++)
                {
                    if (eachSpanListVisibleItemPosition[i] > adapter.getItemCount() - reachBottomRow * spanCount)
                    {
                        isReachBottom = true;
                        break;
                    }
                }
            }

            if (!isReachBottom)
            {
                isInTheBottom = false;
            } else if (!isInTheBottom)
            {
                isInTheBottom = true;

//                // 正在加载或者刷新的 不执行
//                if (!this.myPullSwipeRefresh.isRefreshing() && !isLoading)
//                {
//                    //设置不能刷新
//                    myPullSwipeRefresh.setEnabled(false);
//                    //设置加载状态
//                    isLoading = true;
//                    addMoreListener.addMoreListener();
//                }

                if (myAdapter.isBeginLoading())
                {
                    addMoreListener.addMoreListener();
                    myAdapter.setLoading(true);
                    myAdapter.getSwipeRefresh().setEnabled(false);
                }
            }

//            L.d("是否到达底部" + isInTheBottom);

        }
    }

    // get set 方法
    public void setOnAddMoreListener(OnAddMoreListener addMoreListener)
    {
        this.addMoreListener = addMoreListener;
    }

    public void setMyPullSwipeRefresh(MyPullSwipeRefresh myPullSwipeRefresh)
    {
        this.myPullSwipeRefresh = myPullSwipeRefresh;
    }

    public interface OnAddMoreListener
    {
        void addMoreListener();
    }


}
