package com.recyclerview.gridview.test.base.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MaShiZhao on 2016/12/28.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder
{
    private View mView;
    private static Context mContext;

    public BaseViewHolder(Context context, View itemView)
    {
        super(itemView);
        this.mView = itemView;
        this.mContext = context;
    }

    public static BaseViewHolder creatViewHolder(Context context, ViewGroup parent, int layoutId)
    {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BaseViewHolder(context, view);
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T findViewById(int viewId)
    {
        View view = null;
        if (mView != null)
        {
            view = mView.findViewById(viewId);
        }
        return (T) view;
    }

    public View getConvertView()
    {
        return mView;
    }

    public void convert(BaseViewHolder holder, int position)
    {
    }


}
