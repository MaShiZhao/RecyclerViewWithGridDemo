package com.recyclerview.gridview.test.base;

import android.content.Context;

import com.recyclerview.gridview.test.base.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

public class MyInformationAdapter extends PullRefreshAdapter<InformationResult.DataBean.ListBean>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public MyInformationAdapter(Context context, List<InformationResult.DataBean.ListBean> data, InformationDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


        /**
         *
         * 多模板的type返回
         */

    @Override
    public int getItemViewType(int position)
    {
        //先返回父类判断区分 头部 尾部
        int type = super.getItemViewType(position);
        //如果是普通的则根据实际情况去进行设置type
        if (type == RECYCLE_TYPE_ITEM)
        {
            if (mData.get(position).getImgs().size() == 1)
            {
                return PROMOTION_IMAGE_ONE;
            } else
            {
                return PROMOTION_IMAGE_OTHER;
            }
        }
        return type;
    }

}
