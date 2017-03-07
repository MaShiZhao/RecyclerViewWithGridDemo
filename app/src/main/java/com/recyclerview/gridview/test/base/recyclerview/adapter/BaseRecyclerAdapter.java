package com.recyclerview.gridview.test.base.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder>
{

    public static final int RECYCLE_TYPE_ITEM = 1000;
    public static final int RECYCLE_TYPE_HEADER = 1001;
    public static final int RECYCLE_TYPE_FOOTER = 1002;

    protected Context mContext;
    protected List<T> mData;

    private OnItemClickListener onItemClickListener;

    /**
     * 头部数量 默认没有 涉及头部现阶段需要添加一个即可
     */
    protected int headerCount = 0;
    /**
     * 底部数量 默认没有 涉及底部现阶段需要添加一个即可
     */
    protected int footerCount = 0;
    /**
     * 设置显示数量 默认-1 显示全部
     */
    protected int showCount = -1;

    /**
     * 设置deleate
     */
    private BaseDelegate baseDelegate;


    private int selectPosition = 0;

    /**
     * 添加新的数据
     *
     * @param newData 新数据
     */
    public void addData(List<T> newData)
    {
        if (this.mData != null && newData != null)
        {
            mData.addAll(newData);
            notifyDataSetChanged();
//            notifyItemInserted(this.mData.size() - newData.size());
//            notifyItemRangeChanged(this.mData.size() - newData.size(), this.mData.size());
        }
    }

    /**
     * 只需要添加一条数据
     *
     * @param bean 新数据
     */
    public void addData(T bean)
    {
        if (this.mData != null && bean != null)
        {
            mData.add(bean);
            notifyDataSetChanged();
 //            notifyItemInserted(this.mData.size() - 1);
//            notifyItemRangeChanged(this.mData.size() - 1, this.mData.size());
        }
    }

    /**
     * 重置数据
     *
     * @param newData 新数据
     */
    public void resetData(List<T> newData)
    {
        if (this.mData != null && newData != null)
        {
            this.mData.clear();
            this.mData.addAll(newData);
            notifyDataSetChanged();
//            notifyItemInserted(0);
//            notifyItemRangeChanged(0, this.mData.size());
        }
    }

    /**
     * 清空数据
     */
    public void clear()
    {
        if (this.mData != null)
        {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 删除某个对象
     *
     * @param position 位置
     */
    public void deletItem(int position)
    {
        if (this.mData != null && this.mData.size() > position)
        {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size() - position);
        }
    }

    /**
     * 设置头部数量
     *
     * @param headerCount 数量
     */
    public void setHeaderCount(int headerCount)
    {
        this.headerCount = headerCount;
    }

    /**
     * 设置底部数量
     *
     * @param footerCount 数量
     */
    public void setFooterCount(int footerCount)
    {
        this.footerCount = footerCount;
    }

    /**
     * 设置显示数量 不包含头部和底部
     *
     * @param showCount 显示数量
     */
    public void setShowCount(int showCount)
    {
        this.showCount = showCount;
    }

    /**
     * 返回数据内容+头部和底部
     *
     * @return
     */
    @Override
    public int getItemCount()
    {
        int count = mData == null ? 0 : showCount != -1 ? Math.min(mData.size(), showCount) : mData.size();
        return count + footerCount + headerCount;
    }

    /**
     * 是否属于头部
     *
     * @param position 位置
     */
    public Boolean isHeaderItem(int position)
    {
        return position < headerCount;
    }

    /**
     * 是否属于底部
     *
     * @param position 位置
     */
    public Boolean isFooterItem(int position)
    {
        return position >= getItemCount() - footerCount;
    }

    /**
     * 点击相应事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    protected BaseDelegate setBaseDelegate(BaseDelegate baseDelegate)
    {
        this.baseDelegate = baseDelegate;
        return this.baseDelegate;
    }

    public BaseDelegate getBaseDelegate()
    {
        return this.baseDelegate;
    }


    public int getSelectPosition()
    {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition)
    {
        this.selectPosition = selectPosition;
    }

    public T getSelectItem()
    {
        if (mData.size() > this.selectPosition)
        {
            return mData.get(this.getSelectPosition());
        }

        return null;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return BaseViewHolder.creatViewHolder(mContext, parent, this.baseDelegate.getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position)
    {

        this.baseDelegate.initView(holder, mData, position);

        if (onItemClickListener != null)
        {
            holder.getConvertView().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    onItemClickListener.onItemClick(holder.getConvertView(), position);
                }
            });
        }

    }


    @Override
    public int getItemViewType(int position)
    {
        if (isHeaderItem(position))
        {
            return RECYCLE_TYPE_HEADER;
        } else if (isFooterItem(position))
        {
            return RECYCLE_TYPE_FOOTER;
        } else
        {
            return RECYCLE_TYPE_ITEM;
        }
    }


    public interface OnItemClickListener
    {
        public void onItemClick(View view, int position);
    }

}
