package com.recyclerview.gridview.test.base.recyclerview.adapter;


import com.app.frame.R;

/**
 * Created by MaShiZhao on 2016/12/29
 */
public class PullRefreshDelegate implements BaseDelegate
{
    private int headerLayoutId;
    private int customLayoutId;
    private int dataCount;
    private int headerCount;

    public PullRefreshDelegate(int customLayoutId, int dataCount)
    {
        this.customLayoutId = customLayoutId;
        this.dataCount = dataCount;
        this.headerCount = 0;
        this.headerLayoutId = 0;
    }

    /**
     * 构造
     *
     * @param headerLayoutId 头布局的文件id
     * @param customLayoutId 普通布局的文件id
     * @param dataCount      总数量
     */

    public PullRefreshDelegate(int headerLayoutId, int customLayoutId, int dataCount)
    {
        this.headerLayoutId = headerLayoutId;
        this.customLayoutId = customLayoutId;
        this.dataCount = dataCount;
        this.headerCount = 1;
    }

    public void setDataCount(int dataCount)
    {
        this.dataCount = dataCount;
    }

    @Override
    public int getLayoutId(int viewType)
    {
        if (viewType == BaseRecyclerAdapter.RECYCLE_TYPE_HEADER)
        {
            return headerLayoutId;
        } else if (viewType == BaseRecyclerAdapter.RECYCLE_TYPE_FOOTER)
        {
            return R.layout.item_footer;
        } else
        {
            return customLayoutId;
        }
    }

    @Override
    public void initView(BaseViewHolder holder, Object obj, final int position)
    {
        if (position < headerCount)
        {
            initHeaderView(holder, obj, position);
        } else if (position < dataCount + headerCount)
        {
            initCustomView(holder, obj, position - headerCount);
        }
    }

    public void initHeaderView(BaseViewHolder holder, Object obj, int position)
    {
    }

    public void initCustomView(BaseViewHolder holder, Object obj, int position)
    {
    }
}
