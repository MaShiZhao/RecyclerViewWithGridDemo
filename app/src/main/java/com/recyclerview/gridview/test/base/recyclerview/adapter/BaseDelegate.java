package com.recyclerview.gridview.test.base.recyclerview.adapter;

/**
 * Created by MaShiZhao on 2016/12/29
 * <p>
 * 设置布局文件  绑定view 以及响应事件
 */
public interface BaseDelegate<T>
{

    /**
     * 通过ViewType获取布局文件
     *
     * @param viewType 类型
     * @return layout id
     */
    public int getLayoutId(int viewType);


    /**
     * 设置view 响应事件
     *
     * @param holder   viewHolder
     * @param t        参数
     * @param position 位置
     */
    public void initView(BaseViewHolder holder, T t, int position);


}
