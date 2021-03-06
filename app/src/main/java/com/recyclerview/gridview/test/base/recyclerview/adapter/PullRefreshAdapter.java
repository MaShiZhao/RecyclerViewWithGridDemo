package com.recyclerview.gridview.test.base.recyclerview.adapter;

import android.content.Context;

import com.recyclerview.gridview.test.base.recyclerview.MyPullSwipeRefresh;

import java.util.List;

/**
 * Created by MaShiZhao on 2016/12/28
 */
public class PullRefreshAdapter<T> extends BaseRecyclerAdapter<T>
{

    //当前页数
    private int pageIndex;
    //总页数
    private int totalPage;

    private static final int DEFAULT_PAGE = 1;

    private MyPullSwipeRefresh mySwipeRefresh;

    private PullRefreshDelegate myPullRefreshDelegate;

    //是否加载更多种
    private Boolean isLoading;

    public PullRefreshAdapter(Context context, List<T> strings, PullRefreshDelegate pullRefreshDelegate)
    {
        this(context, strings, 0, pullRefreshDelegate);
    }


    public PullRefreshAdapter(Context context, List<T> strings, int headerCount, PullRefreshDelegate pullRefreshDelegate)
    {
        this.mContext = context;
        this.mData = strings;
        //footerCount设置0  是防止第一次进来就显示
        this.footerCount = 0;
        this.headerCount = headerCount;
        this.pageIndex = DEFAULT_PAGE;
        this.totalPage = DEFAULT_PAGE;
        this.isLoading = false;
        this.myPullRefreshDelegate = pullRefreshDelegate;
        setBaseDelegate(pullRefreshDelegate);
    }

    public int getPageIndex()
    {
        return pageIndex;
    }

    public PullRefreshAdapter setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
        return this;
    }

    public PullRefreshAdapter setTotalPage(int totalPage)
    {
        this.totalPage = totalPage;
        return this;
    }

    public Boolean getLoading()
    {
        return isLoading;
    }

    public void setLoading(Boolean loading)
    {
        isLoading = loading;
    }

    //摄者swiperefresh 用于刷新判断
    public MyPullSwipeRefresh getSwipeRefresh()
    {
        return mySwipeRefresh;
    }

    public void setSwipeRefresh(MyPullSwipeRefresh mySwipeRefresh)
    {
        this.mySwipeRefresh = mySwipeRefresh;
    }

    //刷新时调用
    public void resetPageIndex()
    {
        this.pageIndex = DEFAULT_PAGE;
    }

    //加载时调用
    public void addPageIndex()
    {
        this.pageIndex++;
    }

    //判断是否可以刷新 刷新和加载更多时不可以
//    public Boolean isBeginRefresh()
//    {
//        if (mySwipeRefresh == null || mySwipeRefresh.isRefreshing() || isLoading)
//        {
//            L.v("查看是否设置了swipeRefresh");
//            return false;
//        }
//        return true;
//    }

    //判断是否可以加载更多 刷新和加载更多时,已经是最后一页时不可以
    public Boolean isBeginLoading()
    {
        if (mySwipeRefresh == null || mySwipeRefresh.isRefreshing() || isLoading || pageIndex == totalPage)
        {
             return false;
        }
        return true;
    }

    /**
     * 接收数据源 根据页面进行设置
     *
     * @param data 新数据源
     */
    public void setPullData(List<T> data)
    {
        // TODO: 2017/3/4
        // 当前页面小于总页数 则需要显示footer 否则就不显示了
        // 需要考虑 就一页并且当前数据不足以覆盖手机屏幕
        // 当前弊端：有多页时,第一页的加载更多会不显示footer 因为当时还没有totalPage
        if (pageIndex < totalPage)
        {
            setFooterCount(1);
        } else
        {
            setFooterCount(0);
        }

        //是否是第一页的数据
        if (DEFAULT_PAGE == pageIndex)
        {
            this.myPullRefreshDelegate.setDataCount(data.size());
            resetData(data);
            if (mySwipeRefresh != null)
            {
                mySwipeRefresh.setRefreshing(false);
            }
        } else
        {
            this.myPullRefreshDelegate.setDataCount(this.mData.size() + data.size());
            addData(data);
            isLoading = false;
            mySwipeRefresh.setEnabled(true);
        }
    }


    public void setFail()
    {
        mySwipeRefresh.setEnabled(true);
        isLoading = false;
    }

}
